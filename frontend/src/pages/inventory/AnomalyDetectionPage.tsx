import { useEffect, useState } from 'react'
import { anomalyApi, warehouseApi, type AnomalyLogResponse, type WarehouseResponse } from '../../inventory/api'
import { formatDateTime, fmtNum, statusClass } from '../../inventory/utils'
import '../../styles/inventory.css'

export default function AnomalyDetectionPage() {
  const [rows, setRows] = useState<AnomalyLogResponse[]>([])
  const [warehouses, setWarehouses] = useState<WarehouseResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [whFilter, setWhFilter] = useState<number | ''>('')
  const [resolving, setResolving] = useState<number | null>(null)

  async function load(wid?: number) {
    try {
      setLoading(true); setError(null)
      const data = wid ? await anomalyApi.getByWarehouse(wid) : await anomalyApi.getAll()
      setRows(data)
    } catch (e: any) {
      setError(e.message || 'Failed to load anomalies')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
    warehouseApi.getAll().then(setWarehouses).catch(() => {})
  }, [])

  async function handleResolve(anomalyId: number) {
    try {
      setResolving(anomalyId)
      await anomalyApi.resolve(anomalyId)
      setRows(prev => prev.filter(a => a.anomalyId !== anomalyId))
    } catch (ex: any) {
      alert(ex.message || 'Resolve failed')
    } finally {
      setResolving(null)
    }
  }

  return (
    <div className="inv-page">
      <div className="inv-header">
        <h2>Anomaly Detection</h2>
        <div className="inv-header-actions">
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => load(whFilter || undefined)}>Refresh</button>
        </div>
      </div>

      <div className="inv-filters">
        <select value={whFilter} onChange={e => { const v = e.target.value ? Number(e.target.value) : ''; setWhFilter(v); load(v || undefined) }}>
          <option value="">All Warehouses</option>
          {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
        </select>
      </div>

      {error && <div className="inv-error">{error}</div>}

      <div className="inv-table-wrap">
        {loading ? (
          <div className="inv-empty">Loading anomalies…</div>
        ) : rows.length === 0 ? (
          <div className="inv-empty">No open anomalies detected. ✅</div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table className="inv-table">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>SKU</th>
                  <th>Warehouse</th>
                  <th>Anomaly Type</th>
                  <th>Description</th>
                  <th>Deviation Score</th>
                  <th>Status</th>
                  <th>Detected At</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {rows.map(a => (
                  <tr key={a.anomalyId}>
                    <td style={{ fontWeight: 600 }}>{a.itemName}</td>
                    <td style={{ color: 'var(--cyan)', fontWeight: 600 }}>{a.sku}</td>
                    <td className="muted">{a.warehouseName}</td>
                    <td>
                      <span style={{ background: 'rgba(239,68,68,0.1)', color: 'var(--rose)', padding: '3px 8px', borderRadius: 6, fontSize: 12, fontWeight: 700 }}>
                        {a.anomalyType}
                      </span>
                    </td>
                    <td className="muted" style={{ maxWidth: 260, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                      {a.description || '—'}
                    </td>
                    <td style={{ color: 'var(--rose)', fontWeight: 700 }}>{fmtNum(a.deviationScore)}</td>
                    <td><span className={`badge ${statusClass(a.status)}`}>{a.status}</span></td>
                    <td className="muted">{formatDateTime(a.detectedAt)}</td>
                    <td>
                      {a.status === 'OPEN' && (
                        <button
                          className="inv-action-btn resolve"
                          onClick={() => handleResolve(a.anomalyId)}
                          disabled={resolving === a.anomalyId}
                        >
                          {resolving === a.anomalyId ? '…' : '✓ Resolve'}
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
