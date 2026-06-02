import { useEffect, useState } from 'react'
import { txApi, itemApi, warehouseApi, type StockTransactionResponse, type InventoryItemResponse, type WarehouseResponse } from '../../inventory/api'
import { formatDateTime, txTypeClass, fmtNum } from '../../inventory/utils'
import '../../styles/inventory.css'

const TX_TYPES = ['RESTOCK', 'DISPATCH', 'ADJUSTMENT', 'RESERVE', 'RELEASE_RESERVE']
const PAGE_SIZE = 50

export default function StockTransactionsPage() {
  const [rows, setRows] = useState<StockTransactionResponse[]>([])
  const [items, setItems] = useState<InventoryItemResponse[]>([])
  const [warehouses, setWarehouses] = useState<WarehouseResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const [typeFilter, setTypeFilter] = useState('')
  const [whFilter, setWhFilter] = useState<number | ''>('')
  const [itemFilter, setItemFilter] = useState<number | ''>('')
  const [page, setPage] = useState(0)

  async function load(type?: string, wid?: number, iid?: number) {
    try {
      setLoading(true); setError(null)
      let data: StockTransactionResponse[]
      if (type) data = await txApi.getByType(type)
      else if (wid) data = await txApi.getByWarehouse(wid)
      else if (iid) data = await txApi.getByItem(iid)
      else data = await txApi.getAll()
      // Sort descending by createdAt
      data.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
      setRows(data)
      setPage(0)
    } catch (e: any) {
      setError(e.message || 'Failed to load transactions')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
    itemApi.getAll().then(setItems).catch(() => {})
    warehouseApi.getAll().then(setWarehouses).catch(() => {})
  }, [])

  function applyFilter(type: string, wid: number | '', iid: number | '') {
    setTypeFilter(type); setWhFilter(wid); setItemFilter(iid)
    load(type || undefined, wid || undefined, iid || undefined)
  }

  const totalPages = Math.ceil(rows.length / PAGE_SIZE)
  const pageRows = rows.slice(page * PAGE_SIZE, (page + 1) * PAGE_SIZE)

  return (
    <div className="inv-page">
      <div className="inv-header">
        <h2>Stock Transactions</h2>
        <div className="inv-header-actions">
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => load(typeFilter || undefined, whFilter || undefined, itemFilter || undefined)}>Refresh</button>
        </div>
      </div>

      <div className="inv-filters">
        <select value={typeFilter} onChange={e => applyFilter(e.target.value, '', '')}>
          <option value="">All Types</option>
          {TX_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
        </select>
        <select value={whFilter} onChange={e => applyFilter('', e.target.value ? Number(e.target.value) : '', '')}>
          <option value="">All Warehouses</option>
          {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
        </select>
        <select value={itemFilter} onChange={e => applyFilter('', '', e.target.value ? Number(e.target.value) : '')}>
          <option value="">All Items</option>
          {items.map(i => <option key={i.itemId} value={i.itemId}>{i.itemName} ({i.sku})</option>)}
        </select>
        {(typeFilter || whFilter || itemFilter) && (
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => applyFilter('', '', '')}>Clear</button>
        )}
      </div>

      {error && <div className="inv-error">{error}</div>}

      <div className="inv-table-wrap">
        {loading ? (
          <div className="inv-empty">Loading transactions…</div>
        ) : rows.length === 0 ? (
          <div className="inv-empty">No transactions found.</div>
        ) : (
          <>
            <div style={{ overflowX: 'auto' }}>
              <table className="inv-table">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Item</th>
                    <th>SKU</th>
                    <th>Warehouse</th>
                    <th>Type</th>
                    <th>Qty</th>
                    <th>Before</th>
                    <th>After</th>
                    <th>Performed By</th>
                    <th>Notes</th>
                    <th>Date</th>
                  </tr>
                </thead>
                <tbody>
                  {pageRows.map(t => (
                    <tr key={t.transactionId}>
                      <td className="muted" style={{ fontSize: 12 }}>#{t.transactionId}</td>
                      <td style={{ fontWeight: 600 }}>{t.itemName}</td>
                      <td style={{ color: 'var(--cyan)', fontWeight: 600 }}>{t.sku}</td>
                      <td className="muted">{t.warehouseName}</td>
                      <td><span className={`badge ${txTypeClass(t.transactionType)}`}>{t.transactionType}</span></td>
                      <td style={{ fontWeight: 700 }}>{fmtNum(t.quantity)}</td>
                      <td className="muted">{fmtNum(t.quantityBefore)}</td>
                      <td className="muted">{fmtNum(t.quantityAfter)}</td>
                      <td className="muted">{t.performedBy || '—'}</td>
                      <td className="muted" style={{ maxWidth: 180, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                        {t.notes || '—'}
                      </td>
                      <td className="muted">{formatDateTime(t.createdAt)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="inv-pagination">
              <span>{rows.length} total — Page {page + 1} of {totalPages}</span>
              <button onClick={() => setPage(p => Math.max(0, p - 1))} disabled={page === 0}>← Prev</button>
              <button onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))} disabled={page >= totalPages - 1}>Next →</button>
            </div>
          </>
        )}
      </div>
    </div>
  )
}
