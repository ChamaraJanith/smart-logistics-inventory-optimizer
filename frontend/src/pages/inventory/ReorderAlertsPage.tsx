import { useEffect, useState } from 'react'
import { alertApi, warehouseApi, type ReorderAlertResponse, type WarehouseResponse } from '../../inventory/api'
import { formatDateTime, severityClass, fmtNum } from '../../inventory/utils'
import '../../styles/inventory.css'

const SEVERITIES = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL']

export default function ReorderAlertsPage() {
  const [rows, setRows] = useState<ReorderAlertResponse[]>([])
  const [warehouses, setWarehouses] = useState<WarehouseResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [sevFilter, setSevFilter] = useState('')
  const [whFilter, setWhFilter] = useState<number | ''>('')
  const [resolving, setResolving] = useState<number | null>(null)

  async function load(sev?: string, wid?: number) {
    try {
      setLoading(true); setError(null)
      let data: ReorderAlertResponse[]
      if (sev) data = await alertApi.getBySeverity(sev)
      else if (wid) data = await alertApi.getByWarehouse(wid)
      else data = await alertApi.getAll()
      setRows(data)
    } catch (e: any) {
      setError(e.message || 'Failed to load alerts')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
    warehouseApi.getAll().then(setWarehouses).catch(() => {})
  }, [])

  function applyFilter(sev: string, wid: number | '') {
    setSevFilter(sev); setWhFilter(wid)
    load(sev || undefined, wid || undefined)
  }

  async function handleResolve(alertId: number) {
    try {
      setResolving(alertId)
      await alertApi.resolve(alertId)
      setRows(prev => prev.filter(a => a.alertId !== alertId))
    } catch (ex: any) {
      alert(ex.message || 'Resolve failed')
    } finally {
      setResolving(null)
    }
  }

  return (
    <div className="inv-page">
      <div className="inv-header">
        <h2>Reorder Alerts</h2>
        <div className="inv-header-actions">
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => load(sevFilter || undefined, whFilter || undefined)}>Refresh</button>
        </div>
      </div>

      <div className="inv-filters">
        <select value={sevFilter} onChange={e => applyFilter(e.target.value, '')}>
          <option value="">All Severities</option>
          {SEVERITIES.map(s => <option key={s} value={s}>{s}</option>)}
        </select>
        <select value={whFilter} onChange={e => applyFilter('', e.target.value ? Number(e.target.value) : '')}>
          <option value="">All Warehouses</option>
          {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
        </select>
        {(sevFilter || whFilter) && (
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => applyFilter('', '')}>Clear</button>
        )}
      </div>

      {error && <div className="inv-error">{error}</div>}

      <div className="inv-table-wrap">
        {loading ? (
          <div className="inv-empty">Loading alerts…</div>
        ) : rows.length === 0 ? (
          <div className="inv-empty">No open reorder alerts. 🎉</div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table className="inv-table">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>SKU</th>
                  <th>Warehouse</th>
                  <th>Alert Type</th>
                  <th>Current Stock</th>
                  <th>Reorder Lvl</th>
                  <th>Suggested Qty</th>
                  <th>Predicted 7d</th>
                  <th>Severity</th>
                  <th>Triggered At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {rows.map(a => (
                  <tr key={a.alertId}>
                    <td style={{ fontWeight: 600 }}>{a.itemName}</td>
                    <td style={{ color: 'var(--cyan)', fontWeight: 600 }}>{a.sku}</td>
                    <td className="muted">{a.warehouseName}</td>
                    <td className="muted">{a.alertType}</td>
                    <td style={{ color: 'var(--rose)', fontWeight: 700 }}>{fmtNum(a.currentStock)}</td>
                    <td className="muted">{fmtNum(a.reorderLevel)}</td>
                    <td>{fmtNum(a.suggestedReorderQty)}</td>
                    <td className="muted">{a.predictedDemand7d != null ? fmtNum(a.predictedDemand7d) : 'N/A'}</td>
                    <td><span className={`badge ${severityClass(a.severity)}`}>{a.severity}</span></td>
                    <td className="muted">{formatDateTime(a.triggeredAt)}</td>
                    <td>
                      {a.status === 'OPEN' && (
                        <button
                          className="inv-action-btn resolve"
                          onClick={() => handleResolve(a.alertId)}
                          disabled={resolving === a.alertId}
                        >
                          {resolving === a.alertId ? '…' : '✓ Resolve'}
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
