import { useEffect, useState } from 'react'
import { stockApi, itemApi, warehouseApi, type InventoryStockResponse, type InventoryItemResponse, type WarehouseResponse } from '../../inventory/api'
import { formatDateTime, stockStatusClass, fmtNum } from '../../inventory/utils'
import '../../styles/inventory.css'

const TX_TYPES = ['RESTOCK', 'DISPATCH', 'ADJUSTMENT', 'RESERVE', 'RELEASE_RESERVE']

export default function InventoryStockPage() {
  const [rows, setRows] = useState<InventoryStockResponse[]>([])
  const [items, setItems] = useState<InventoryItemResponse[]>([])
  const [warehouses, setWarehouses] = useState<WarehouseResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [lowOnly, setLowOnly] = useState(false)

  // Add stock modal
  const [addModal, setAddModal] = useState(false)
  const [addForm, setAddForm] = useState({ itemId: '', warehouseId: '', quantityOnHand: '', reorderLevel: '', reorderQuantity: '', maxStockLevel: '' })
  const [addErr, setAddErr] = useState<string | null>(null)
  const [addSaving, setAddSaving] = useState(false)

  // Update stock modal
  const [updateStock, setUpdateStock] = useState<InventoryStockResponse | null>(null)
  const [updateForm, setUpdateForm] = useState({ transactionType: 'RESTOCK', quantity: '', notes: '', performedBy: '' })
  const [updateErr, setUpdateErr] = useState<string | null>(null)
  const [updateSaving, setUpdateSaving] = useState(false)

  async function load(low = false) {
    try {
      setLoading(true); setError(null)
      const data = low ? await stockApi.getLowStock() : await stockApi.getAll()
      setRows(data)
    } catch (e: any) {
      setError(e.message || 'Failed to load stock')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
    itemApi.getAll().then(setItems).catch(() => {})
    warehouseApi.getAll().then(setWarehouses).catch(() => {})
  }, [])

  function toggleLow(val: boolean) { setLowOnly(val); load(val) }

  async function handleAddSave(e: React.FormEvent) {
    e.preventDefault()
    if (!addForm.itemId) { setAddErr('Item is required'); return }
    if (!addForm.warehouseId) { setAddErr('Warehouse is required'); return }
    if (!addForm.quantityOnHand) { setAddErr('Quantity on hand is required'); return }
    if (!addForm.reorderLevel) { setAddErr('Reorder level is required'); return }
    if (!addForm.reorderQuantity) { setAddErr('Reorder quantity is required'); return }
    try {
      setAddSaving(true); setAddErr(null)
      await stockApi.create({
        itemId: Number(addForm.itemId),
        warehouseId: Number(addForm.warehouseId),
        quantityOnHand: Number(addForm.quantityOnHand),
        reorderLevel: Number(addForm.reorderLevel),
        reorderQuantity: Number(addForm.reorderQuantity),
        maxStockLevel: addForm.maxStockLevel ? Number(addForm.maxStockLevel) : undefined,
      })
      setAddModal(false)
      load(lowOnly)
    } catch (ex: any) {
      setAddErr(ex.message || 'Save failed')
    } finally {
      setAddSaving(false)
    }
  }

  async function handleUpdate(e: React.FormEvent) {
    e.preventDefault()
    if (!updateStock) return
    if (!updateForm.quantity) { setUpdateErr('Quantity is required'); return }
    try {
      setUpdateSaving(true); setUpdateErr(null)
      await stockApi.update(updateStock.stockId, {
        transactionType: updateForm.transactionType,
        quantity: Number(updateForm.quantity),
        notes: updateForm.notes || undefined,
        performedBy: updateForm.performedBy || undefined,
      })
      setUpdateStock(null)
      load(lowOnly)
    } catch (ex: any) {
      setUpdateErr(ex.message || 'Update failed')
    } finally {
      setUpdateSaving(false)
    }
  }

  return (
    <div className="inv-page">
      <div className="inv-header">
        <h2>Inventory Stock</h2>
        <div className="inv-header-actions">
          <div className="inv-toggle">
            <button className={!lowOnly ? 'active' : ''} onClick={() => toggleLow(false)}>All Stock</button>
            <button className={lowOnly ? 'active' : ''} onClick={() => toggleLow(true)}>Low Stock</button>
          </div>
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => load(lowOnly)}>Refresh</button>
          <button className="lp-btn lp-btn--primary lp-btn--sm" onClick={() => { setAddModal(true); setAddForm({ itemId: '', warehouseId: '', quantityOnHand: '', reorderLevel: '', reorderQuantity: '', maxStockLevel: '' }); setAddErr(null) }}>+ Add Stock Entry</button>
        </div>
      </div>

      {error && <div className="inv-error">{error}</div>}

      <div className="inv-table-wrap">
        {loading ? (
          <div className="inv-empty">Loading stock…</div>
        ) : rows.length === 0 ? (
          <div className="inv-empty">No stock records found.</div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table className="inv-table">
              <thead>
                <tr>
                  <th>Item</th>
                  <th>SKU</th>
                  <th>Warehouse</th>
                  <th>On Hand</th>
                  <th>Reserved</th>
                  <th>Available</th>
                  <th>Reorder Lvl</th>
                  <th>Max Lvl</th>
                  <th>Status</th>
                  <th>Last Updated</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {rows.map(s => (
                  <tr key={s.stockId}>
                    <td style={{ fontWeight: 600 }}>{s.itemName}</td>
                    <td style={{ color: 'var(--cyan)', fontWeight: 600 }}>{s.sku}</td>
                    <td className="muted">{s.warehouseName}</td>
                    <td>{fmtNum(s.quantityOnHand)}</td>
                    <td className="muted">{fmtNum(s.reservedQuantity)}</td>
                    <td style={{ fontWeight: 700 }}>{fmtNum(s.availableQuantity)}</td>
                    <td className="muted">{fmtNum(s.reorderLevel)}</td>
                    <td className="muted">{s.maxStockLevel != null ? fmtNum(s.maxStockLevel) : '—'}</td>
                    <td><span className={`badge ${stockStatusClass(s.stockStatus)}`}>{s.stockStatus}</span></td>
                    <td className="muted">{formatDateTime(s.lastUpdated)}</td>
                    <td>
                      <button className="inv-action-btn" onClick={() => {
                        setUpdateStock(s)
                        setUpdateForm({ transactionType: 'RESTOCK', quantity: '', notes: '', performedBy: '' })
                        setUpdateErr(null)
                      }}>Update</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Add stock modal */}
      {addModal && (
        <div className="inv-modal-overlay" onClick={() => setAddModal(false)}>
          <div className="inv-modal" onClick={e => e.stopPropagation()}>
            <div className="inv-modal-header">
              <h3>Add Stock Entry</h3>
              <button className="inv-modal-close" onClick={() => setAddModal(false)}>×</button>
            </div>
            <form onSubmit={handleAddSave}>
              <div className="inv-form-row">
                <div className="inv-field">
                  <label>Item *</label>
                  <select value={addForm.itemId} onChange={e => setAddForm(f => ({ ...f, itemId: e.target.value }))}>
                    <option value="">Select item…</option>
                    {items.map(i => <option key={i.itemId} value={i.itemId}>{i.itemName} ({i.sku})</option>)}
                  </select>
                </div>
                <div className="inv-field">
                  <label>Warehouse *</label>
                  <select value={addForm.warehouseId} onChange={e => setAddForm(f => ({ ...f, warehouseId: e.target.value }))}>
                    <option value="">Select warehouse…</option>
                    {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
                  </select>
                </div>
              </div>
              <div className="inv-form-row">
                <div className="inv-field">
                  <label>Qty on Hand *</label>
                  <input type="number" min="0" step="0.001" value={addForm.quantityOnHand} onChange={e => setAddForm(f => ({ ...f, quantityOnHand: e.target.value }))} />
                </div>
                <div className="inv-field">
                  <label>Reorder Level *</label>
                  <input type="number" min="0" step="0.001" value={addForm.reorderLevel} onChange={e => setAddForm(f => ({ ...f, reorderLevel: e.target.value }))} />
                </div>
              </div>
              <div className="inv-form-row">
                <div className="inv-field">
                  <label>Reorder Qty *</label>
                  <input type="number" min="0.001" step="0.001" value={addForm.reorderQuantity} onChange={e => setAddForm(f => ({ ...f, reorderQuantity: e.target.value }))} />
                </div>
                <div className="inv-field">
                  <label>Max Stock Level</label>
                  <input type="number" min="0" step="0.001" value={addForm.maxStockLevel} onChange={e => setAddForm(f => ({ ...f, maxStockLevel: e.target.value }))} />
                </div>
              </div>
              {addErr && <div className="inv-field-error" style={{ marginBottom: 12 }}>{addErr}</div>}
              <div className="inv-modal-actions">
                <button type="button" className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => setAddModal(false)}>Cancel</button>
                <button type="submit" className="lp-btn lp-btn--primary lp-btn--sm" disabled={addSaving}>
                  {addSaving ? 'Saving…' : 'Save'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Update stock modal */}
      {updateStock && (
        <div className="inv-modal-overlay" onClick={() => setUpdateStock(null)}>
          <div className="inv-modal" onClick={e => e.stopPropagation()} style={{ maxWidth: 440 }}>
            <div className="inv-modal-header">
              <h3>Update Stock</h3>
              <button className="inv-modal-close" onClick={() => setUpdateStock(null)}>×</button>
            </div>
            <p style={{ color: 'var(--text-2)', fontSize: 14, marginBottom: 20 }}>
              <strong style={{ color: 'var(--text)' }}>{updateStock.itemName}</strong> — {updateStock.warehouseName}
              <span style={{ marginLeft: 12, color: 'var(--text-3)' }}>Available: {fmtNum(updateStock.availableQuantity)}</span>
            </p>
            <form onSubmit={handleUpdate}>
              <div className="inv-field">
                <label>Transaction Type *</label>
                <select value={updateForm.transactionType} onChange={e => setUpdateForm(f => ({ ...f, transactionType: e.target.value }))}>
                  {TX_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                </select>
              </div>
              <div className="inv-field">
                <label>Quantity *</label>
                <input type="number" min="0.001" step="0.001" value={updateForm.quantity} onChange={e => setUpdateForm(f => ({ ...f, quantity: e.target.value }))} />
              </div>
              <div className="inv-field">
                <label>Performed By</label>
                <input value={updateForm.performedBy} onChange={e => setUpdateForm(f => ({ ...f, performedBy: e.target.value }))} placeholder="User or system" />
              </div>
              <div className="inv-field">
                <label>Notes</label>
                <input value={updateForm.notes} onChange={e => setUpdateForm(f => ({ ...f, notes: e.target.value }))} placeholder="Optional notes" />
              </div>
              {updateErr && <div className="inv-field-error" style={{ marginBottom: 12 }}>{updateErr}</div>}
              <div className="inv-modal-actions">
                <button type="button" className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => setUpdateStock(null)}>Cancel</button>
                <button type="submit" className="lp-btn lp-btn--primary lp-btn--sm" disabled={updateSaving}>
                  {updateSaving ? 'Updating…' : 'Update'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
