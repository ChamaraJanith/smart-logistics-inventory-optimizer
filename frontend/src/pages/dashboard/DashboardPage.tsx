import { useEffect, useState } from 'react'
import '../../styles/dashboard.css'
import DeliveryTrendsChart, { DriverPerformanceChart } from '../../components/dashboard/Charts'
import DashboardLiveMap from '../../components/dashboard/DashboardLiveMap'

async function safeJsonFetch(url: string) {
  const res = await fetch(url)
  const ct = res.headers.get('content-type') || ''
  if (!res.ok) {
    // try to surface body text for debugging
    const body = await res.text().catch(() => '')
    throw new Error(`${res.status} ${res.statusText} - ${body.slice(0, 200)}`)
  }
  if (!ct.includes('application/json')) {
    const body = await res.text().catch(() => '')
    throw new Error(`Expected JSON from ${url} (content-type: ${ct || 'unknown'}) — response: ${body.slice(0,200)}`)
  }
  return res.json()
}

function TrendIndicator({ value, isPositive = true }: { value: string; isPositive?: boolean }) {
  return (
    <div className={`db-card-trend ${isPositive ? 'up' : 'down'}`}>
      <span className="db-trend-arrow">{isPositive ? '↑' : '↓'}</span>
      {value}
    </div>
  )
}

function SummaryCard({ title, value, icon, trend }: { title: string; value: string; icon: string; trend?: { value: string; up: boolean } }) {
  return (
    <div className="db-card">
      <div className="db-card-header">
        <div>
          <div className="db-card-title">{title}</div>
          <div className="db-card-value">{value}</div>
          {trend && <TrendIndicator value={trend.value} isPositive={trend.up} />}
        </div>
        <div className="db-card-icon">{icon}</div>
      </div>
    </div>
  )
}

export default function DashboardPage() {
  const [summary, setSummary] = useState<any>(null)
  const [loadingSummary, setLoadingSummary] = useState(true)
  const [errorSummary, setErrorSummary] = useState<string | null>(null)

  const [recentRoutes, setRecentRoutes] = useState<any[]>([])
  const [loadingRoutes, setLoadingRoutes] = useState(true)

  const [deliveryTrends, setDeliveryTrends] = useState<number[] | null>(null)
  const [loadingTrends, setLoadingTrends] = useState(true)

  const [driverPerf, setDriverPerf] = useState<any[] | null>(null)
  const [loadingDriverPerf, setLoadingDriverPerf] = useState(true)

  useEffect(() => {
    let mounted = true
    async function load() {
      try {
        setLoadingSummary(true)
        const data = await safeJsonFetch('/api/v1/dashboard/summary')
        if (mounted) setSummary(data)
      } catch (err: any) {
        if (mounted) setErrorSummary(err.message || 'Error')
      } finally {
        if (mounted) setLoadingSummary(false)
      }
    }
    load()
    return () => { mounted = false }
  }, [])

  useEffect(() => {
    let mounted = true
    async function loadRoutes() {
      try {
        setLoadingRoutes(true)
        try {
          const data = await safeJsonFetch('/api/v1/dashboard/recent-routes')
          if (mounted && Array.isArray(data)) setRecentRoutes(data)
        } catch (err) {
          // keep recentRoutes empty on error; caller sees loading=false
        }
      } catch (_) {
      } finally { if (mounted) setLoadingRoutes(false) }
    }
    loadRoutes()
    return () => { mounted = false }
  }, [])

  useEffect(() => {
    let mounted = true
    async function loadTrends() {
      try {
        setLoadingTrends(true)
        try {
          const data = await safeJsonFetch('/api/v1/analytics/delivery-trends?period=weekly')
        // expect array of numbers or objects
          if (mounted) {
            if (Array.isArray(data)) setDeliveryTrends(data.map((d: any) => typeof d === 'number' ? d : d.value ?? 0))
            else setDeliveryTrends(null)
          }
        } catch (err) {
          if (mounted) setDeliveryTrends(null)
        }
      } catch (_) {
        if (mounted) setDeliveryTrends(null)
      } finally { if (mounted) setLoadingTrends(false) }
    }
    loadTrends()
    return () => { mounted = false }
  }, [])

  useEffect(() => {
    let mounted = true
    async function loadDrivers() {
      try {
        setLoadingDriverPerf(true)
        try {
          const data = await safeJsonFetch('/api/v1/analytics/driver-performance')
          if (mounted && Array.isArray(data)) setDriverPerf(data)
        } catch (err) {
          // ignore
        }
      } catch (_) {
      } finally { if (mounted) setLoadingDriverPerf(false) }
    }
    loadDrivers()
    return () => { mounted = false }
  }, [])

  return (
    <div className="dashboard">
      <h2>Dashboard</h2>

      {/* SUMMARY CARDS */}
      {loadingSummary && <p>Loading summary...</p>}
      {errorSummary && <p style={{ color: '#f87171' }}>{errorSummary}</p>}
      {!loadingSummary && !errorSummary && (
        <div className="db-summary-grid">
          <SummaryCard title="On-Time Delivery" value={summary?.onTimeDelivery ?? '—'} icon="✨" trend={{ value: '+2.5%', up: true }} />
          <SummaryCard title="Deliveries Today" value={summary?.deliveriesToday ?? '—'} icon="📦" trend={{ value: '+12%', up: true }} />
          <SummaryCard title="Active Routes" value={summary?.activeRoutes ?? '—'} icon="📍" />
          <SummaryCard title="Stock Visibility" value={summary?.stockVisibility ?? '—'} icon="📊" />
        </div>
      )}

      {/* Live Logistics Map Widget */}
      <div style={{ marginTop: 24, marginBottom: 16 }}>
        <h3 className="db-section-title">Live Deliveries Map</h3>
        <DashboardLiveMap />
      </div>

      <div className="db-main-grid">
        <div>
          <div className="db-card-section">
            <h3 className="db-section-title">Recent Routes</h3>
            {loadingRoutes ? <div>Loading...</div> : (
              recentRoutes.length === 0 ? <div style={{ color: 'var(--app-text-3)' }}>No recent routes</div> : (
                <ul className="db-recent-list">
                  {recentRoutes.slice(0,6).map((r: any, i: number) => {
                    const status = (r.status ?? r.state ?? 'pending').toLowerCase()
                    const badgeClass = status === 'completed' ? 'completed' : status === 'active' ? 'active' : 'pending'
                    return (
                      <li key={i} className="db-route-item">
                        <div className="db-route-info">
                          <div className="db-route-title">{r.name ?? r.routeId ?? `Route ${i+1}`}</div>
                          <div className="db-route-meta">ETA: {r.eta ?? r.updatedAt ?? '—'}</div>
                        </div>
                        <div className={`db-route-badge ${badgeClass}`}>{status}</div>
                      </li>
                    )
                  })}
                </ul>
              )
            )}
          </div>

          <div className="db-chart-container" style={{ marginTop: 16 }}>
            <h3 className="db-section-title">Delivery Trends (weekly)</h3>
            {loadingTrends ? <div>Loading trends...</div> : (
              deliveryTrends ? (
                <div className="db-sparkline">
                  <DeliveryTrendsChart data={deliveryTrends} />
                  <div style={{ fontSize: 13, color: 'var(--app-text-3)', marginTop: 12 }}>Last week: {deliveryTrends[deliveryTrends.length-1]} deliveries</div>
                </div>
              ) : <div style={{ color: 'var(--app-text-3)' }}>No trend data</div>
            )}
          </div>
        </div>

        <div>
          <div className="db-chart-container">
            <h3 className="db-section-title">Driver Performance</h3>
            {loadingDriverPerf ? <div>Loading...</div> : (
              driverPerf && driverPerf.length ? (
                <div>
                  <div className="db-driver-list">
                    {driverPerf.slice(0,5).map((d: any, idx: number) => {
                      const scoreVal = Math.min(100, Math.round(d.score ?? d.onTimeRate ?? 0))
                      return (
                        <div key={idx} className="db-driver-row">
                          <div className="db-driver-head">
                            <span>{d.name ?? d.driverId ?? `Driver ${idx+1}`}</span>
                            <span className="db-driver-score">{scoreVal}%</span>
                          </div>
                          <div className="db-bar"><div className="db-bar-fill" style={{ width: `${scoreVal}%` }} /></div>
                        </div>
                      )
                    })}
                  </div>
                </div>
              ) : <div style={{ color: 'var(--app-text-3)' }}>No driver data</div>
            )}
          </div>
        </div>
      </div>

    </div>
  )
}
