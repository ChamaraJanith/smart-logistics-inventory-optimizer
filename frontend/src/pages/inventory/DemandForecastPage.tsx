import { useEffect, useState } from 'react'
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid,
  Tooltip, Legend, ResponsiveContainer,
} from 'recharts'
import { forecastApi, itemApi, warehouseApi, type DemandForecastResponse, type InventoryItemResponse, type WarehouseResponse } from '../../inventory/api'
import { formatDate, fmtNum } from '../../inventory/utils'
import '../../styles/inventory.css'

export default function DemandForecastPage() {
  const [items, setItems] = useState<InventoryItemResponse[]>([])
  const [warehouses, setWarehouses] = useState<WarehouseResponse[]>([])

  const [selItem, setSelItem] = useState<number | ''>('')
  const [selWh, setSelWh] = useState<number | ''>('')
  const [forecastDays, setForecastDays] = useState(7)

  const [forecasts, setForecasts] = useState<DemandForecastResponse[]>([])
  const [generating, setGenerating] = useState(false)
  const [genErr, setGenErr] = useState<string | null>(null)

  const [history, setHistory] = useState<DemandForecastResponse[]>([])
  const [loadingHistory, setLoadingHistory] = useState(false)

  const [whBulkId, setWhBulkId] = useState<number | ''>('')
  const [generating2, setGenerating2] = useState(false)

  useEffect(() => {
    itemApi.getAll().then(setItems).catch(() => {})
    warehouseApi.getAll().then(setWarehouses).catch(() => {})
  }, [])

  async function handleGenerate(e: React.FormEvent) {
    e.preventDefault()
    if (!selItem) { setGenErr('Please select an item'); return }
    if (!selWh) { setGenErr('Please select a warehouse'); return }
    try {
      setGenerating(true); setGenErr(null)
      const data = await forecastApi.generate({ itemId: Number(selItem), warehouseId: Number(selWh), forecastDays })
      setForecasts(data)
      // Load history too
      const hist = await forecastApi.getByItem(Number(selItem)).catch(() => [])
      setHistory(hist)
    } catch (ex: any) {
      setGenErr(ex.message || 'Forecast generation failed')
    } finally {
      setGenerating(false)
    }
  }

  async function handleLoadHistory() {
    if (!selItem) return
    try {
      setLoadingHistory(true)
      const hist = await forecastApi.getByItem(Number(selItem))
      setHistory(hist)
    } catch { /* ignore */ } finally {
      setLoadingHistory(false)
    }
  }

  async function handleWarehouseForecast(e: React.FormEvent) {
    e.preventDefault()
    if (!whBulkId) return
    try {
      setGenerating2(true)
      await forecastApi.generateForWarehouse(Number(whBulkId), forecastDays)
      alert('Warehouse forecast generated successfully.')
    } catch (ex: any) {
      alert(ex.message || 'Failed')
    } finally {
      setGenerating2(false)
    }
  }

  const chartMeta = forecasts.length > 0 ? forecasts[0] : null

  return (
    <div className="inv-page">
      <div className="inv-header">
        <h2>Demand Forecast</h2>
      </div>

      {/* Control panel */}
      <div className="forecast-section" style={{ marginBottom: 24 }}>
        <div style={{ fontWeight: 700, fontSize: 16, marginBottom: 16, color: 'var(--text)' }}>Generate Forecast</div>
        <form onSubmit={handleGenerate} style={{ display: 'flex', gap: 12, flexWrap: 'wrap', alignItems: 'flex-end' }}>
          <div className="inv-field" style={{ marginBottom: 0, minWidth: 200 }}>
            <label>Item *</label>
            <select value={selItem} onChange={e => setSelItem(e.target.value ? Number(e.target.value) : '')}>
              <option value="">Select item…</option>
              {items.map(i => <option key={i.itemId} value={i.itemId}>{i.itemName} ({i.sku})</option>)}
            </select>
          </div>
          <div className="inv-field" style={{ marginBottom: 0, minWidth: 180 }}>
            <label>Warehouse *</label>
            <select value={selWh} onChange={e => setSelWh(e.target.value ? Number(e.target.value) : '')}>
              <option value="">Select warehouse…</option>
              {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
            </select>
          </div>
          <div className="inv-field" style={{ marginBottom: 0, minWidth: 100 }}>
            <label>Days</label>
            <input type="number" min="1" max="30" value={forecastDays} onChange={e => setForecastDays(Number(e.target.value))} style={{ width: '100%' }} />
          </div>
          <button type="submit" className="lp-btn lp-btn--primary lp-btn--sm" disabled={generating} style={{ height: 42 }}>
            {generating ? 'Generating…' : 'Generate Forecast'}
          </button>
          {selItem && (
            <button type="button" className="lp-btn lp-btn--ghost lp-btn--sm" onClick={handleLoadHistory} disabled={loadingHistory} style={{ height: 42 }}>
              {loadingHistory ? '…' : 'Load History'}
            </button>
          )}
        </form>
        {genErr && <div className="inv-field-error" style={{ marginTop: 12 }}>{genErr}</div>}
      </div>

      {/* Chart */}
      {forecasts.length > 0 && (
        <div className="forecast-section" style={{ marginBottom: 24 }}>
          <div style={{ fontWeight: 700, fontSize: 16, marginBottom: 16, color: 'var(--text)' }}>
            Demand Forecast — {chartMeta?.itemName} ({chartMeta?.sku})
          </div>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={forecasts} margin={{ top: 4, right: 40, left: -8, bottom: 0 }}>
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.06)" />
              <XAxis dataKey="forecastDate" tick={{ fill: 'var(--text-3)', fontSize: 11 }} />
              <YAxis yAxisId="left" tick={{ fill: 'var(--text-3)', fontSize: 11 }} />
              <YAxis yAxisId="right" orientation="right" domain={[0, 1]} tick={{ fill: 'var(--text-3)', fontSize: 11 }} tickFormatter={v => `${Math.round(v * 100)}%`} />
              <Tooltip
                contentStyle={{ background: 'var(--bg-2)', border: '1px solid var(--border)', borderRadius: 10, color: 'var(--text)' }}
                formatter={(val: number, name: string) => name === 'Confidence' ? `${Math.round(Number(val) * 100)}%` : fmtNum(val)}
              />
              <Legend wrapperStyle={{ fontSize: 13, color: 'var(--text-2)' }} />
              <Line yAxisId="left" type="monotone" dataKey="predictedDemand" stroke="#3b82f6" strokeWidth={2} dot={{ r: 4 }} name="Predicted Demand" />
              <Line yAxisId="right" type="monotone" dataKey="confidenceScore" stroke="#fb923c" strokeWidth={2} strokeDasharray="5 3" dot={false} name="Confidence" />
            </LineChart>
          </ResponsiveContainer>

          {/* Metadata */}
          <div className="forecast-meta">
            <div className="forecast-meta-item">
              <span>Warehouse</span><span>{chartMeta?.warehouseName}</span>
            </div>
            <div className="forecast-meta-item">
              <span>Season Pattern</span><span>{chartMeta?.seasonPattern || '—'}</span>
            </div>
            <div className="forecast-meta-item">
              <span>Model Version</span><span>{chartMeta?.modelVersion || '—'}</span>
            </div>
            <div className="forecast-meta-item">
              <span>Data Points</span><span>{forecasts.length}</span>
            </div>
          </div>
        </div>
      )}

      {/* Warehouse bulk forecast */}
      <div className="forecast-section" style={{ marginBottom: 24 }}>
        <div style={{ fontWeight: 700, fontSize: 16, marginBottom: 16, color: 'var(--text)' }}>Generate Warehouse Forecast (Bulk)</div>
        <form onSubmit={handleWarehouseForecast} style={{ display: 'flex', gap: 12, flexWrap: 'wrap', alignItems: 'flex-end' }}>
          <div className="inv-field" style={{ marginBottom: 0, minWidth: 200 }}>
            <label>Warehouse</label>
            <select value={whBulkId} onChange={e => setWhBulkId(e.target.value ? Number(e.target.value) : '')}>
              <option value="">Select warehouse…</option>
              {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
            </select>
          </div>
          <button type="submit" className="lp-btn lp-btn--ghost lp-btn--sm" disabled={generating2 || !whBulkId} style={{ height: 42 }}>
            {generating2 ? 'Generating…' : 'Generate for Warehouse'}
          </button>
        </form>
      </div>

      {/* History table */}
      {history.length > 0 && (
        <div className="inv-table-wrap">
          <div style={{ padding: '16px 20px', fontWeight: 700, fontSize: 15, color: 'var(--text)', borderBottom: '1px solid var(--border)' }}>
            Saved Forecasts ({history.length})
          </div>
          <div style={{ overflowX: 'auto' }}>
            <table className="inv-table">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>Warehouse</th>
                  <th>Forecast Date</th>
                  <th>Predicted Demand</th>
                  <th>Confidence</th>
                  <th>Season</th>
                  <th>Model</th>
                  <th>Created</th>
                </tr>
              </thead>
              <tbody>
                {history.map(f => (
                  <tr key={f.forecastId}>
                    <td style={{ fontWeight: 600 }}>{f.itemName}</td>
                    <td className="muted">{f.warehouseName}</td>
                    <td>{formatDate(f.forecastDate)}</td>
                    <td style={{ fontWeight: 700 }}>{fmtNum(f.predictedDemand)}</td>
                    <td className="muted">{Math.round((f.confidenceScore ?? 0) * 100)}%</td>
                    <td className="muted">{f.seasonPattern || '—'}</td>
                    <td className="muted">{f.modelVersion || '—'}</td>
                    <td className="muted">{formatDate(f.createdAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}
