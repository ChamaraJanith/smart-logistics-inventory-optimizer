import { useEffect, useState } from 'react'
import { itemApi, warehouseApi, type InventoryItemResponse, type WarehouseResponse } from '../../inventory/api'
import { formatDate, statusClass } from '../../inventory/utils'
import '../../styles/inventory.css'

type FormData = {
  warehouseId: number | ''
  sku: string
  itemName: string
  category: string
  unit: string
  unitWeightKg: number | ''
  unitVolume: number | ''
  unitPrice: number | ''
  status: string
}

const EMPTY_FORM: FormData = { warehouseId: '', sku: '', itemName: '', category: '', unit: '', unitWeightKg: '', unitVolume: '', unitPrice: '', status: 'ACTIVE' }

export default function InventoryItemsPage() {
  const [rows, setRows] = useState<InventoryItemResponse[]>([])
  const [warehouses, setWarehouses] = useState<WarehouseResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const [whFilter, setWhFilter] = useState<number | ''>('')
  const [catFilter, setCatFilter] = useState('')
  const [catInput, setCatInput] = useState('')

  const [modal, setModal] = useState(false)
  const [editing, setEditing] = useState<InventoryItemResponse | null>(null)
  const [form, setForm] = useState<FormData>({ ...EMPTY_FORM })
  const [saving, setSaving] = useState(false)
  const [formErr, setFormErr] = useState<string | null>(null)

  const [confirmId, setConfirmId] = useState<number | null>(null)
  const [deleting, setDeleting] = useState(false)

  async function load(wid?: number, cat?: string) {
    try {
      setLoading(true); setError(null)
      let data: InventoryItemResponse[]
      if (wid) data = await itemApi.getByWarehouse(wid)
      else if (cat) data = await itemApi.getByCategory(cat)
      else data = await itemApi.getAll()
      setRows(data)
    } catch (e: any) {
      setError(e.message || 'Failed to load items')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
    warehouseApi.getAll().then(setWarehouses).catch(() => {})
  }, [])

  function applyWhFilter(wid: number | '') {
    setWhFilter(wid); setCatFilter(''); setCatInput('')
    load(wid || undefined)
  }

  function applyCatFilter() {
    const cat = catInput.trim()
    setCatFilter(cat); setWhFilter('')
    if (cat) load(undefined, cat)
    else load()
  }

  function openAdd() { setEditing(null); setForm({ ...EMPTY_FORM }); setFormErr(null); setModal(true) }
  function openEdit(it: InventoryItemResponse) {
    setEditing(it)
    setForm({
      warehouseId: it.warehouseId,
      sku: it.sku,
      itemName: it.itemName,
      category: it.category ?? '',
      unit: it.unit,
      unitWeightKg: it.unitWeightKg ?? '',
      unitVolume: it.unitVolume ?? '',
      unitPrice: it.unitPrice ?? '',
      status: it.status,
    })
    setFormErr(null); setModal(true)
  }
  function closeModal() { setModal(false) }

  async function handleSave(e: React.FormEvent) {
    e.preventDefault()
    if (!form.warehouseId) { setFormErr('Warehouse is required'); return }
    if (!form.sku.trim()) { setFormErr('SKU is required'); return }
    if (!form.itemName.trim()) { setFormErr('Item name is required'); return }
    if (!form.unit.trim()) { setFormErr('Unit is required'); return }
    try {
      setSaving(true); setFormErr(null)
      const payload = { ...form, warehouseId: Number(form.warehouseId) }
      if (editing) await itemApi.update(editing.itemId, payload)
      else await itemApi.create(payload)
      closeModal()
      load(whFilter || undefined, catFilter || undefined)
    } catch (ex: any) {
      setFormErr(ex.message || 'Save failed')
    } finally {
      setSaving(false)
    }
  }

  async function handleDelete() {
    if (confirmId === null) return
    try {
      setDeleting(true)
      await itemApi.delete(confirmId)
      setConfirmId(null)
      load(whFilter || undefined, catFilter || undefined)
    } catch (ex: any) {
      alert(ex.message || 'Delete failed')
    } finally {
      setDeleting(false)
    }
  }

  return (
    <div className="inv-page">
      <div className="inv-header">
        <h2>Inventory Items</h2>
        <div className="inv-header-actions">
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => load(whFilter || undefined, catFilter || undefined)}>Refresh</button>
          <button className="lp-btn lp-btn--primary lp-btn--sm" onClick={openAdd}>+ Add Item</button>
        </div>
      </div>

      <div className="inv-filters">
        <select value={whFilter} onChange={e => applyWhFilter(e.target.value ? Number(e.target.value) : '')}>
          <option value="">All Warehouses</option>
          {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
        </select>
        <input
          type="text"
          placeholder="Filter by category…"
          value={catInput}
          onChange={e => setCatInput(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && applyCatFilter()}
          style={{ minWidth: 200 }}
        />
        <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={applyCatFilter}>Apply</button>
        {(whFilter || catFilter) && (
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => { setWhFilter(''); setCatFilter(''); setCatInput(''); load() }}>Clear</button>
        )}
      </div>

      {error && <div className="inv-error">{error}</div>}

      <div className="inv-table-wrap">
        {loading ? (
          <div className="inv-empty">Loading items…</div>
        ) : rows.length === 0 ? (
          <div className="inv-empty">No inventory items found.</div>
        ) : (
          <table className="inv-table">
            <thead>
              <tr>
                <th>SKU</th>
                <th>Item Name</th>
                <th>Category</th>
                <th>Warehouse</th>
                <th>Unit</th>
                <th>Unit Price</th>
                <th>Status</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {rows.map(it => (
                <tr key={it.itemId}>
                  <td style={{ fontWeight: 600, color: 'var(--cyan)' }}>{it.sku}</td>
                  <td style={{ fontWeight: 600 }}>{it.itemName}</td>
                  <td className="muted">{it.category || '—'}</td>
                  <td className="muted">{it.warehouseName}</td>
                  <td className="muted">{it.unit}</td>
                  <td className="muted">{it.unitPrice != null ? `$${Number(it.unitPrice).toFixed(2)}` : '—'}</td>
                  <td><span className={`badge ${statusClass(it.status)}`}>{it.status}</span></td>
                  <td className="muted">{formatDate(it.createdAt)}</td>
                  <td>
                    <button className="inv-action-btn" onClick={() => openEdit(it)}>✏️</button>
                    <button className="inv-action-btn danger" onClick={() => setConfirmId(it.itemId)}>🗑️</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {modal && (
        <div className="inv-modal-overlay" onClick={closeModal}>
          <div className="inv-modal" onClick={e => e.stopPropagation()}>
            <div className="inv-modal-header">
              <h3>{editing ? 'Edit Item' : 'Add Inventory Item'}</h3>
              <button className="inv-modal-close" onClick={closeModal}>×</button>
            </div>
            <form onSubmit={handleSave}>
              <div className="inv-field">
                <label>Warehouse *</label>
                <select value={form.warehouseId} onChange={e => setForm(f => ({ ...f, warehouseId: Number(e.target.value) }))}>
                  <option value="">Select warehouse…</option>
                  {warehouses.map(w => <option key={w.warehouseId} value={w.warehouseId}>{w.name}</option>)}
                </select>
              </div>
              <div className="inv-form-row">
                <div className="inv-field">
                  <label>SKU *</label>
                  <input value={form.sku} onChange={e => setForm(f => ({ ...f, sku: e.target.value }))} placeholder="e.g. SKU-001" />
                </div>
                <div className="inv-field">
                  <label>Unit *</label>
                  <input value={form.unit} onChange={e => setForm(f => ({ ...f, unit: e.target.value }))} placeholder="e.g. kg, pcs" />
                </div>
              </div>
              <div className="inv-field">
                <label>Item Name *</label>
                <input value={form.itemName} onChange={e => setForm(f => ({ ...f, itemName: e.target.value }))} />
              </div>
              <div className="inv-field">
                <label>Category</label>
                <input value={form.category} onChange={e => setForm(f => ({ ...f, category: e.target.value }))} placeholder="e.g. Electronics" />
              </div>
              <div className="inv-form-row">
                <div className="inv-field">
                  <label>Weight (kg)</label>
                  <input type="number" min="0" step="0.001" value={form.unitWeightKg} onChange={e => setForm(f => ({ ...f, unitWeightKg: e.target.value ? Number(e.target.value) : '' }))} />
                </div>
                <div className="inv-field">
                  <label>Volume</label>
                  <input type="number" min="0" step="0.001" value={form.unitVolume} onChange={e => setForm(f => ({ ...f, unitVolume: e.target.value ? Number(e.target.value) : '' }))} />
                </div>
                <div className="inv-field">
                  <label>Unit Price ($)</label>
                  <input type="number" min="0" step="0.01" value={form.unitPrice} onChange={e => setForm(f => ({ ...f, unitPrice: e.target.value ? Number(e.target.value) : '' }))} />
                </div>
              </div>
              <div className="inv-field">
                <label>Status</label>
                <select value={form.status} onChange={e => setForm(f => ({ ...f, status: e.target.value }))}>
                  <option value="ACTIVE">Active</option>
                  <option value="INACTIVE">Inactive</option>
                </select>
              </div>
              {formErr && <div className="inv-field-error" style={{ marginBottom: 12 }}>{formErr}</div>}
              <div className="inv-modal-actions">
                <button type="button" className="lp-btn lp-btn--ghost lp-btn--sm" onClick={closeModal}>Cancel</button>
                <button type="submit" className="lp-btn lp-btn--primary lp-btn--sm" disabled={saving}>
                  {saving ? 'Saving…' : 'Save'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {confirmId !== null && (
        <div className="inv-modal-overlay" onClick={() => setConfirmId(null)}>
          <div className="inv-confirm" onClick={e => e.stopPropagation()}>
            <h4>Delete Item?</h4>
            <p>This action cannot be undone.</p>
            <div className="inv-confirm-actions">
              <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => setConfirmId(null)}>Cancel</button>
              <button className="lp-btn lp-btn--primary lp-btn--sm" style={{ background: 'var(--rose)' }} onClick={handleDelete} disabled={deleting}>
                {deleting ? 'Deleting…' : 'Delete'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
