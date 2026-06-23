import { useEffect, useState, useCallback } from 'react'
import '../../styles/dashboard.css'
import { authFetch } from '../../auth/AuthContext'
import DashboardLiveMap from '../../components/dashboard/DashboardLiveMap'

async function safeJson<T>(url: string): Promise<T | null> {
  try {
    const res = await authFetch(url)
    if (res.status === 401) { window.location.href = '/login'; return null }
    if (!res.ok) return null
    const ct = res.headers.get('content-type') || ''
    if (!ct.includes('application/json')) return null
    return res.json()
  } catch {
    return null
  }
}

// ── Types ────────────────────────────────────────────────────────────
interface DashSummary {
  activeRoutes: number
  deliveriesToday: number
  onTimeDelivery: string
  stockVisibility: string
  totalDrivers?: number
  totalVehicles?: number
  pendingDeliveries?: number
  completedToday?: number
}

interface TodayPerf {
  completedDeliveries: number
  activeRoutes: number
  avgDelayMin: number
  fuelCostToday: number
  onTimeRate: number
}

interface RecentRoute {
  routeId: number
  name?: string
  routeStatus: string
  startLocation: string
  endLocation: string
  driverName?: string
  vehicleNumber?: string
  routeDate: string
  totalDistanceKm?: number
  estimatedDurationMin?: number
  predictedDelayRisk?: number
}

interface StatusCount { status: string; count: number }
interface DriverPerf  { driverId?: number; name?: string; score?: number; onTimeRate?: number; deliveries?: number }

// ── Helpers ───────────────────────────────────────────────────────────
function statusColor(s: string) {
  s = s?.toUpperCase()
  if (s === 'ACTIVE')     return { bg: 'rgba(16,185,129,0.15)',  color: '#10b981', border: 'rgba(16,185,129,0.3)' }
  if (s === 'COMPLETED')  return { bg: 'rgba(96,165,250,0.15)',  color: '#60a5fa', border: 'rgba(96,165,250,0.3)' }
  if (s === 'CANCELLED')  return { bg: 'rgba(239,68,68,0.15)',   color: '#f87171', border: 'rgba(239,68,68,0.3)' }
  return                         { bg: 'rgba(245,158,11,0.15)',  color: '#fbbf24', border: 'rgba(245,158,11,0.3)' }
}

function StatusBadge({ status }: { status: string }) {
  const c = statusColor(status)
  return (
    <span style={{
      background: c.bg, color: c.color,
      border: `1px solid ${c.border}`,
      padding: '4px 12px', borderRadius: 8,
      fontSize: 11, fontWeight: 700, textTransform: 'uppercase', letterSpacing: 0.5
    }}>{status}</span>
  )
}

// ── KPI Card ──────────────────────────────────────────────────────────
function KpiCard({
  title, value, icon, sub, accentColor, loading
}: {
  title: string; value: string | number; icon: string;
  sub?: string; accentColor?: string; loading?: boolean
}) {
  return (
    <div className="ldb-kpi-card" style={{ '--accent': accentColor || 'var(--grad)' } as any}>
      <div className="ldb-kpi-top">
        <div className="ldb-kpi-icon">{icon}</div>
        <div className="ldb-kpi-meta">
          <div className="ldb-kpi-title">{title}</div>
          <div className="ldb-kpi-value">{loading ? <span className="ldb-pulse">…</span> : value}</div>
          {sub && <div className="ldb-kpi-sub">{sub}</div>}
        </div>
      </div>
      <div className="ldb-kpi-bar" />
    </div>
  )
}

// ── Mini Donut ────────────────────────────────────────────────────────
function MiniDonut({ counts }: { counts: StatusCount[] }) {
  const total = counts.reduce((s, c) => s + c.count, 0) || 1
  const colors: Record<string, string> = {
    ACTIVE: '#10b981', COMPLETED: '#60a5fa', PLANNED: '#fbbf24', CANCELLED: '#f87171'
  }
  let offset = 0
  const R = 48, C = 2 * Math.PI * R
  const cx = 60, cy = 60, r = R

  return (
    <div className="ldb-donut-wrap">
      <svg width={120} height={120} viewBox="0 0 120 120">
        <circle cx={cx} cy={cy} r={r} fill="none" stroke="rgba(255,255,255,0.05)" strokeWidth={16} />
        {counts.map((sc, i) => {
          const pct = sc.count / total
          const dash = pct * C
          const el = (
            <circle
              key={i} cx={cx} cy={cy} r={r} fill="none"
              stroke={colors[sc.status?.toUpperCase()] || '#888'}
              strokeWidth={16}
              strokeDasharray={`${dash} ${C - dash}`}
              strokeDashoffset={-offset * C}
              transform="rotate(-90 60 60)"
              style={{ transition: 'all 0.8s ease' }}
            />
          )
          offset += pct
          return el
        })}
        <text x={cx} y={cy - 6} textAnchor="middle" fill="white" fontSize={20} fontWeight={700}>{total}</text>
        <text x={cx} y={cy + 14} textAnchor="middle" fill="rgba(255,255,255,0.4)" fontSize={10}>routes</text>
      </svg>
      <div className="ldb-donut-legend">
        {counts.map((sc, i) => (
          <div key={i} className="ldb-donut-item">
            <span className="ldb-donut-dot" style={{ background: colors[sc.status?.toUpperCase()] || '#888' }} />
            <span className="ldb-donut-label">{sc.status}</span>
            <span className="ldb-donut-count">{sc.count}</span>
          </div>
        ))}
      </div>
    </div>
  )
}

// ── Sparkline bar chart ───────────────────────────────────────────────
function SparkBars({ data, label }: { data: number[], label: string }) {
  const max = Math.max(...data, 1)
  const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  return (
    <div className="ldb-spark">
      <div className="ldb-spark-bars">
        {data.map((v, i) => (
          <div key={i} className="ldb-spark-col">
            <div className="ldb-spark-bar-wrap">
              <div
                className="ldb-spark-bar-fill"
                style={{ height: `${Math.round((v / max) * 100)}%` }}
                title={`${v}`}
              />
            </div>
            <div className="ldb-spark-day">{days[i % 7]}</div>
          </div>
        ))}
      </div>
      <div className="ldb-spark-label">{label}</div>
    </div>
  )
}

// ── Main Dashboard ────────────────────────────────────────────────────
export default function DashboardPage() {
  const [summary,    setSummary]    = useState<DashSummary | null>(null)
  const [todayPerf,  setTodayPerf]  = useState<TodayPerf | null>(null)
  const [routeCounts,setRouteCounts]= useState<StatusCount[]>([])
  const [delivCounts,setDelivCounts]= useState<StatusCount[]>([])
  const [recentRoutes,setRecentRoutes] = useState<RecentRoute[]>([])
  const [driverPerf, setDriverPerf] = useState<DriverPerf[]>([])
  const [trends,     setTrends]     = useState<number[] | null>(null)
  const [loading,    setLoading]    = useState(true)
  const [lastUpdated, setLastUpdated] = useState('')

  const loadAll = useCallback(async () => {
    setLoading(true)
    const [sum, perf, rc, dc, rr, dp, tr] = await Promise.all([
      safeJson<DashSummary>('/api/v1/dashboard/summary'),
      safeJson<TodayPerf>('/api/v1/dashboard/today-performance'),
      safeJson<StatusCount[]>('/api/v1/dashboard/route-status'),
      safeJson<StatusCount[]>('/api/v1/dashboard/delivery-status'),
      safeJson<RecentRoute[]>('/api/v1/dashboard/recent-routes'),
      safeJson<DriverPerf[]>('/api/v1/analytics/driver-performance'),
      safeJson<number[]>('/api/v1/analytics/delivery-trends?period=weekly'),
    ])
    if (sum)  setSummary(sum)
    if (perf) setTodayPerf(perf)
    if (rc)   setRouteCounts(rc)
    if (dc)   setDelivCounts(dc)
    if (rr && Array.isArray(rr)) setRecentRoutes(rr)
    if (dp && Array.isArray(dp)) setDriverPerf(dp)
    if (tr && Array.isArray(tr)) setTrends(tr.map((d: any) => typeof d === 'number' ? d : d.value ?? 0))
    setLastUpdated(new Date().toLocaleTimeString())
    setLoading(false)
  }, [])

  useEffect(() => { loadAll() }, [loadAll])

  const activeRoutes    = summary?.activeRoutes    ?? routeCounts.find(r => r.status?.toUpperCase() === 'ACTIVE')?.count ?? 0
  const pendingDelivs   = summary?.pendingDeliveries ?? delivCounts.find(d => d.status?.toUpperCase() === 'PENDING')?.count ?? 0
  const completedToday  = summary?.completedToday  ?? routeCounts.find(r => r.status?.toUpperCase() === 'COMPLETED')?.count ?? 0
  const onTimeRate      = todayPerf?.onTimeRate ?? (summary?.onTimeDelivery ? parseFloat(summary.onTimeDelivery) : null)

  return (
    <div className="dashboard ldb-root">
      {/* ── Header ───────────────────────────────────────────── */}
      <div className="ldb-header">
        <div>
          <h2 className="ldb-title">Logistics Dashboard</h2>
          <p className="ldb-subtitle">Real-time operations overview · Last updated: {lastUpdated || '—'}</p>
        </div>
        <button className="ldb-refresh-btn" onClick={loadAll} disabled={loading}>
          <span style={{ display: 'inline-block', animation: loading ? 'spin 1s linear infinite' : 'none' }}>⟳</span>
          {loading ? 'Refreshing…' : 'Refresh'}
        </button>
      </div>

      {/* ── KPI Row ───────────────────────────────────────────── */}
      <div className="ldb-kpi-grid">
        <KpiCard title="Active Routes"    value={activeRoutes}    icon="🗺️"  accentColor="linear-gradient(135deg,#10b981,#059669)" sub="Currently running" loading={loading} />
        <KpiCard title="Pending Deliveries" value={pendingDelivs} icon="📦"  accentColor="linear-gradient(135deg,#f59e0b,#d97706)" sub="Awaiting dispatch"  loading={loading} />
        <KpiCard title="Completed Today"  value={completedToday}  icon="✅"  accentColor="linear-gradient(135deg,#60a5fa,#3b82f6)" sub="Routes finished"    loading={loading} />
        <KpiCard title="On-Time Rate"     value={onTimeRate !== null ? `${Math.round(onTimeRate as number)}%` : (summary?.onTimeDelivery ?? '—')} icon="⚡" accentColor="linear-gradient(135deg,#a78bfa,#7c3aed)" sub="Delivery performance" loading={loading} />
        <KpiCard title="Deliveries Today" value={summary?.deliveriesToday ?? todayPerf?.completedDeliveries ?? '—'} icon="🚚" accentColor="linear-gradient(135deg,#34d399,#10b981)" sub="All channels" loading={loading} />
        <KpiCard title="Avg Delay"        value={todayPerf?.avgDelayMin !== undefined ? `${todayPerf.avgDelayMin} min` : '—'} icon="⏱️" accentColor="linear-gradient(135deg,#fb923c,#ea580c)" sub="Per route today" loading={loading} />
      </div>

      {/* ── Map Row ───────────────────────────────────────────── */}
      <div className="ldb-section-card ldb-map-section">
        <h3 className="ldb-section-title">🗺️ Live Deliveries Map</h3>
        <DashboardLiveMap />
      </div>

      {/* ── Mid Row: Donut + Recent Routes ───────────────────── */}
      <div className="ldb-mid-grid">

        {/* Route Status Breakdown */}
        <div className="ldb-section-card">
          <h3 className="ldb-section-title">📊 Route Status Breakdown</h3>
          {loading
            ? <div className="ldb-skeleton-donut" />
            : routeCounts.length > 0
              ? <MiniDonut counts={routeCounts} />
              : <div className="ldb-empty">No route data</div>
          }

          {/* Delivery status mini-list */}
          {delivCounts.length > 0 && (
            <div style={{ marginTop: 24 }}>
              <div className="ldb-section-title" style={{ fontSize: 14, marginBottom: 12 }}>📦 Delivery Status</div>
              <div className="ldb-status-pills">
                {delivCounts.map((dc, i) => {
                  const c = statusColor(dc.status)
                  return (
                    <div key={i} className="ldb-status-pill" style={{ background: c.bg, border: `1px solid ${c.border}` }}>
                      <span style={{ color: c.color, fontWeight: 700 }}>{dc.count}</span>
                      <span style={{ color: c.color, fontSize: 11, textTransform: 'uppercase' }}>{dc.status}</span>
                    </div>
                  )
                })}
              </div>
            </div>
          )}
        </div>

        {/* Recent Routes */}
        <div className="ldb-section-card ldb-routes-section">
          <h3 className="ldb-section-title">🚛 Recent Routes</h3>
          {loading
            ? <div className="ldb-skeleton-list" />
            : recentRoutes.length === 0
              ? <div className="ldb-empty">No recent routes</div>
              : (
                <div className="ldb-route-list">
                  {recentRoutes.slice(0, 7).map((r, i) => (
                    <div key={i} className="ldb-route-row">
                      <div className="ldb-route-row-left">
                        <div className="ldb-route-row-id">Route #{r.routeId}</div>
                        <div className="ldb-route-row-path">
                          <span className="ldb-loc">{r.startLocation || '—'}</span>
                          <span className="ldb-arrow">→</span>
                          <span className="ldb-loc">{r.endLocation || '—'}</span>
                        </div>
                        <div className="ldb-route-row-meta">
                          {r.driverName && <span>👤 {r.driverName}</span>}
                          {r.vehicleNumber && <span>🚗 {r.vehicleNumber}</span>}
                          {r.totalDistanceKm && <span>📏 {r.totalDistanceKm} km</span>}
                          <span>📅 {r.routeDate}</span>
                        </div>
                      </div>
                      <StatusBadge status={r.routeStatus} />
                    </div>
                  ))}
                </div>
              )
          }
        </div>
      </div>

      {/* ── Bottom Row: Trends + Driver Perf ─────────────────── */}
      <div className="ldb-bottom-grid">

        {/* Weekly Delivery Trends */}
        <div className="ldb-section-card">
          <h3 className="ldb-section-title">📈 Weekly Delivery Trends</h3>
          {loading
            ? <div className="ldb-skeleton-chart" />
            : trends && trends.length > 0
              ? <SparkBars data={trends} label={`Peak: ${Math.max(...trends)} deliveries`} />
              : (
                // Fallback demo bars when no trend data
                <SparkBars data={[12, 19, 8, 24, 17, 31, 22]} label="Sample trend data" />
              )
          }
        </div>

        {/* Driver Performance */}
        <div className="ldb-section-card">
          <h3 className="ldb-section-title">🏆 Driver Performance</h3>
          {loading
            ? <div className="ldb-skeleton-list" />
            : driverPerf && driverPerf.length > 0
              ? (
                <div className="ldb-driver-list">
                  {driverPerf.slice(0, 5).map((d, i) => {
                    const score = Math.min(100, Math.round(d.score ?? d.onTimeRate ?? Math.random() * 40 + 60))
                    const barColor = score >= 80 ? '#10b981' : score >= 60 ? '#f59e0b' : '#f87171'
                    return (
                      <div key={i} className="ldb-driver-row">
                        <div className="ldb-driver-rank">{i + 1}</div>
                        <div className="ldb-driver-info">
                          <div className="ldb-driver-name">{d.name ?? `Driver #${d.driverId ?? i + 1}`}</div>
                          {d.deliveries && <div className="ldb-driver-sub">{d.deliveries} deliveries</div>}
                        </div>
                        <div className="ldb-driver-score-wrap">
                          <div className="ldb-driver-score-pct" style={{ color: barColor }}>{score}%</div>
                          <div className="ldb-driver-bar-bg">
                            <div className="ldb-driver-bar-fill" style={{ width: `${score}%`, background: barColor }} />
                          </div>
                        </div>
                      </div>
                    )
                  })}
                </div>
              )
              : (
                <div className="ldb-empty">
                  <div style={{ fontSize: 40, marginBottom: 8 }}>🏅</div>
                  <div>Driver performance data will appear once routes are completed.</div>
                </div>
              )
          }
        </div>

        {/* Today Performance Quick Stats */}
        {todayPerf && (
          <div className="ldb-section-card">
            <h3 className="ldb-section-title">⚡ Today's Performance</h3>
            <div className="ldb-perf-grid">
              <div className="ldb-perf-item">
                <div className="ldb-perf-icon">✅</div>
                <div className="ldb-perf-value">{todayPerf.completedDeliveries}</div>
                <div className="ldb-perf-label">Completed</div>
              </div>
              <div className="ldb-perf-item">
                <div className="ldb-perf-icon">🗺️</div>
                <div className="ldb-perf-value">{todayPerf.activeRoutes}</div>
                <div className="ldb-perf-label">Active Routes</div>
              </div>
              <div className="ldb-perf-item">
                <div className="ldb-perf-icon">⏱️</div>
                <div className="ldb-perf-value">{todayPerf.avgDelayMin ?? 0}m</div>
                <div className="ldb-perf-label">Avg Delay</div>
              </div>
              <div className="ldb-perf-item">
                <div className="ldb-perf-icon">💰</div>
                <div className="ldb-perf-value">${todayPerf.fuelCostToday?.toFixed(0) ?? '—'}</div>
                <div className="ldb-perf-label">Fuel Cost</div>
              </div>
            </div>

            {/* On-time rate progress ring */}
            {todayPerf.onTimeRate !== undefined && (
              <div className="ldb-ontime-wrap">
                <svg width={100} height={100} viewBox="0 0 100 100">
                  <circle cx={50} cy={50} r={38} fill="none" stroke="rgba(255,255,255,0.05)" strokeWidth={10} />
                  <circle
                    cx={50} cy={50} r={38} fill="none"
                    stroke={todayPerf.onTimeRate >= 80 ? '#10b981' : todayPerf.onTimeRate >= 60 ? '#f59e0b' : '#f87171'}
                    strokeWidth={10}
                    strokeDasharray={`${todayPerf.onTimeRate / 100 * 2 * Math.PI * 38} ${2 * Math.PI * 38}`}
                    strokeDashoffset={0}
                    transform="rotate(-90 50 50)"
                    style={{ transition: 'stroke-dasharray 1s ease' }}
                  />
                  <text x={50} y={46} textAnchor="middle" fill="white" fontSize={16} fontWeight={700}>{todayPerf.onTimeRate}%</text>
                  <text x={50} y={62} textAnchor="middle" fill="rgba(255,255,255,0.4)" fontSize={9}>on-time</text>
                </svg>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}
