import { useEffect, useState } from 'react'
import {
  PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer,
  BarChart, Bar, XAxis, YAxis, CartesianGrid,
} from 'recharts'
import {
  invDashboard,
  type InventoryDashboardSummary,
  type StatusCount,
  type WarehouseStockSummary,
  type ReorderAlertResponse,
  type AnomalyLogResponse,
} from '../../inventory/api'
import { formatDateTime, fmtNum, severityClass } from '../../inventory/utils'
import '../../styles/inventory.css'

const STATUS_COLORS: Record<string, string> = {
  NORMAL: '#34d399',
  LOW: '#fbbf24',
  CRITICAL: '#fb923c',
  OUT_OF_STOCK: '#f87171',
}

function KpiCard({ label, value, cls }: { label: string; value: number | string; cls?: string }) {
  return (
    <div className="inv-kpi-card">
      <div className="inv-kpi-label">{label}</div>
      <div className={`inv-kpi-value ${cls ?? ''}`}>{value}</div>
    </div>
  )
}

export default function InventoryDashboard() {
  const [summary, setSummary] = useState<InventoryDashboardSummary | null>(null)
  const [statusCounts, setStatusCounts] = useState<StatusCount[]>([])
  const [whSummary, setWhSummary] = useState<WarehouseStockSummary[]>([])
  const [recentAlerts, setRecentAlerts] = useState<ReorderAlertResponse[]>([])
  const [recentAnomalies, setRecentAnomalies] = useState<AnomalyLogResponse[]>([])

  const [loadingSummary, setLoadingSummary] = useState(true)
  const [loadingCharts, setLoadingCharts] = useState(true)
  const [errSummary, setErrSummary] = useState<string | null>(null)

  useEffect(() => {
    let alive = true
    async function load() {
      try {
        setLoadingSummary(true)
        const s = await invDashboard.getSummary()
        if (alive) { setSummary(s); setErrSummary(null) }
      } catch (e: any) {
        if (alive) setErrSummary(e.message || 'Failed to load summary')
      } finally {
        if (alive) setLoadingSummary(false)
      }
    }
    load()
    return () => { alive = false }
  }, [])

  useEffect(() => {
    let alive = true
    async function load() {
      try {
        setLoadingCharts(true)
        const [sc, ws, ra, an] = await Promise.allSettled([
          invDashboard.getStockStatusCounts(),
          invDashboard.getWarehouseSummary(),
          invDashboard.getRecentAlerts(),
          invDashboard.getRecentAnomalies(),
        ])
        if (!alive) return
        if (sc.status === 'fulfilled') setStatusCounts(sc.value)
        if (ws.status === 'fulfilled') setWhSummary(ws.value)
        if (ra.status === 'fulfilled') setRecentAlerts(ra.value)
        if (an.status === 'fulfilled') setRecentAnomalies(an.value)
      } finally {
        if (alive) setLoadingCharts(false)
      }
    }
    load()
    return () => { alive = false }
  }, [])

  return (
    <div className="inv-page">
      <div className="inv-header">
        <h2>Inventory Dashboard</h2>
      </div>

      {/* KPI cards */}
      {loadingSummary && <div className="inv-empty">Loading summary…</div>}
      {errSummary && <div className="inv-error">{errSummary}</div>}
      {!loadingSummary && !errSummary && summary && (
        <div className="inv-kpi-strip" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))', gap: 16, marginBottom: 28 }}>
          <KpiCard label="Warehouses" value={summary.totalWarehouses} cls="ok" />
          <KpiCard label="Total Items" value={summary.totalItems} />
          <KpiCard label="Low Stock" value={summary.lowStockCount} cls={summary.lowStockCount > 0 ? 'warn' : 'ok'} />
          <KpiCard label="Out of Stock" value={summary.outOfStockCount} cls={summary.outOfStockCount > 0 ? 'danger' : 'ok'} />
          <KpiCard label="Open Alerts" value={summary.openAlertsCount} cls={summary.openAlertsCount > 0 ? 'warn' : 'ok'} />
          <KpiCard label="Critical Alerts" value={summary.criticalAlertsCount} cls={summary.criticalAlertsCount > 0 ? 'danger' : 'ok'} />
          <KpiCard label="Open Anomalies" value={summary.openAnomaliesCount} cls={summary.openAnomaliesCount > 0 ? 'warn' : 'ok'} />
          <KpiCard label="Today's Txns" value={summary.todayTransactionsCount} />
        </div>
      )}

      {/* Charts row */}
      {loadingCharts ? (
        <div className="inv-empty" style={{ padding: '40px 0' }}>Loading charts…</div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 20, marginBottom: 28 }}>
          {/* Pie chart */}
          <div className="forecast-section" style={{ padding: 24 }}>
            <div style={{ fontWeight: 700, fontSize: 16, marginBottom: 16, color: 'var(--text)' }}>Stock Status</div>
            {statusCounts.length === 0 ? (
              <div className="inv-empty">No data</div>
            ) : (
              <ResponsiveContainer width="100%" height={220}>
                <PieChart>
                  <Pie data={statusCounts} dataKey="count" nameKey="status" cx="50%" cy="50%" outerRadius={80} label={false}>
                    {statusCounts.map(entry => (
                      <Cell key={entry.status} fill={STATUS_COLORS[entry.status] ?? '#60a5fa'} />
                    ))}
                  </Pie>
                  <Tooltip contentStyle={{ background: 'var(--bg-2)', border: '1px solid var(--border)', borderRadius: 10, color: 'var(--text)' }} />
                  <Legend wrapperStyle={{ fontSize: 12, color: 'var(--text-2)' }} />
                </PieChart>
              </ResponsiveContainer>
            )}
          </div>

          {/* Bar chart */}
          <div className="forecast-section" style={{ padding: 24 }}>
            <div style={{ fontWeight: 700, fontSize: 16, marginBottom: 16, color: 'var(--text)' }}>Warehouse Stock Breakdown</div>
            {whSummary.length === 0 ? (
              <div className="inv-empty">No data</div>
            ) : (
              <ResponsiveContainer width="100%" height={220}>
                <BarChart data={whSummary} margin={{ top: 4, right: 4, left: -16, bottom: 0 }}>
                  <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.06)" />
                  <XAxis dataKey="warehouseName" tick={{ fill: 'var(--text-3)', fontSize: 11 }} />
                  <YAxis tick={{ fill: 'var(--text-3)', fontSize: 11 }} />
                  <Tooltip contentStyle={{ background: 'var(--bg-2)', border: '1px solid var(--border)', borderRadius: 10, color: 'var(--text)' }} />
                  <Legend wrapperStyle={{ fontSize: 12, color: 'var(--text-2)' }} />
                  <Bar dataKey="normalStockCount" fill="#34d399" name="Normal" stackId="a" />
                  <Bar dataKey="lowStockCount" fill="#fbbf24" name="Low" stackId="a" />
                  <Bar dataKey="criticalStockCount" fill="#fb923c" name="Critical" stackId="a" />
                  <Bar dataKey="outOfStockCount" fill="#f87171" name="Out of Stock" stackId="a" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            )}
          </div>
        </div>
      )}

      {/* Recent tables */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20 }}>
        {/* Recent Alerts */}
        <div className="forecast-section">
          <div style={{ fontWeight: 700, fontSize: 16, marginBottom: 16, color: 'var(--text)' }}>Recent Reorder Alerts</div>
          {recentAlerts.length === 0
            ? <div className="inv-empty" style={{ padding: '20px 0' }}>No open alerts</div>
            : (
              <table className="inv-table" style={{ fontSize: 13 }}>
                <thead>
                  <tr>
                    <th>Item</th>
                    <th>Stock / Level</th>
                    <th>Severity</th>
                  </tr>
                </thead>
                <tbody>
                  {recentAlerts.map(a => (
                    <tr key={a.alertId}>
                      <td>
                        <div style={{ fontWeight: 600 }}>{a.itemName}</div>
                        <div style={{ fontSize: 11, color: 'var(--text-3)' }}>{a.warehouseName}</div>
                      </td>
                      <td className="muted">{fmtNum(a.currentStock)} / {fmtNum(a.reorderLevel)}</td>
                      <td><span className={`badge ${severityClass(a.severity)}`}>{a.severity}</span></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
        </div>

        {/* Recent Anomalies */}
        <div className="forecast-section">
          <div style={{ fontWeight: 700, fontSize: 16, marginBottom: 16, color: 'var(--text)' }}>Recent Anomalies</div>
          {recentAnomalies.length === 0
            ? <div className="inv-empty" style={{ padding: '20px 0' }}>No open anomalies</div>
            : (
              <table className="inv-table" style={{ fontSize: 13 }}>
                <thead>
                  <tr>
                    <th>Item</th>
                    <th>Type</th>
                    <th>Score</th>
                    <th>Detected</th>
                  </tr>
                </thead>
                <tbody>
                  {recentAnomalies.map(a => (
                    <tr key={a.anomalyId}>
                      <td>
                        <div style={{ fontWeight: 600 }}>{a.itemName}</div>
                        <div style={{ fontSize: 11, color: 'var(--text-3)' }}>{a.warehouseName}</div>
                      </td>
                      <td className="muted">{a.anomalyType}</td>
                      <td style={{ color: 'var(--rose)' }}>{fmtNum(a.deviationScore)}</td>
                      <td className="muted">{formatDateTime(a.detectedAt)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
        </div>
      </div>
    </div>
  )
}
