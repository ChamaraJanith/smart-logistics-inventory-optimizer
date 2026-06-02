import { useEffect, useState } from 'react'
import { warehouseApi, type WarehouseResponse } from '../../inventory/api'
import { formatDate, statusClass } from '../../inventory/utils'
import '../../styles/inventory.css'

const EMPTY: Partial<WarehouseResponse> = { name: '', address: '', managerName: '', contactNumber: '', status: 'ACTIVE' }

export default function WarehousesPage() {
  const [rows, setRows] = useState<WarehouseResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [statusFilter, setStatusFilter] = useState('')

  const [modal, setModal] = useState(false)
  const [editing, setEditing] = useState<WarehouseResponse | null>(null)
  const [form, setForm] = useState<Partial<WarehouseResponse>>({ ...EMPTY })
  const [saving, setSaving] = useState(false)
  const [formErr, setFormErr] = useState<string | null>(null)

  const [confirmId, setConfirmId] = useState<number | null>(null)
  const [deleting, setDeleting] = useState(false)

  async function load(status?: string) {
    try {
      setLoading(true); setError(null)
      const data = status ? await warehouseApi.getByStatus(status) : await warehouseApi.getAll()
      setRows(data)
    } catch (e: any) {
      setError(e.message || 'Failed to load warehouses')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  function openAdd() { setEditing(null); setForm({ ...EMPTY }); setFormErr(null); setModal(true) }
  function openEdit(w: WarehouseResponse) { setEditing(w); setForm({ ...w }); setFormErr(null); setModal(true) }
  function closeModal() { setModal(false) }

  async function handleSave(e: React.FormEvent) {
    e.preventDefault()
    if (!form.name?.trim()) { setFormErr('Warehouse name is required'); return }
    try {
      setSaving(true); setFormErr(null)
      if (editing) {
        await warehouseApi.update(editing.warehouseId, form)
      } else {
        await warehouseApi.create(form)
      }
      closeModal()
      load(statusFilter || undefined)
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
      await warehouseApi.delete(confirmId)
      setConfirmId(null)
      load(statusFilter || undefined)
    } catch (ex: any) {
      alert(ex.message || 'Delete failed')
    } finally {
      setDeleting(false)
    }
  }

  return (
    <div className="inv-page">
      <div className="inv-header">
        <h2>Warehouses</h2>
        <div className="inv-header-actions">
          <div className="inv-filters" style={{ margin: 0 }}>
            <select value={statusFilter} onChange={e => { setStatusFilter(e.target.value); load(e.target.value || undefined) }}>
              <option value="">All Statuses</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
            </select>
          </div>
          <button className="lp-btn lp-btn--ghost lp-btn--sm" onClick={() => load(statusFilter || undefined)}>Refresh</button>
          <button className="lp-btn lp-btn--primary lp-btn--sm" onClick={openAdd}>+ Add Warehouse</button>
        </div>
      </div>

      {error && <div className="inv-error">{error}</div>}

      <div className="inv-table-wrap">
        {loading ? (
          <div className="inv-empty">Loading warehouses…</div>
        ) : rows.length === 0 ? (
          <div className="inv-empty">No warehouses found.</div>
        ) : (
          <table className="inv-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Address</th>
                <th>Manager</th>
                <th>Contact</th>
                <th>Capacity (sqm)</th>
                <th>Status</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {rows.map(w => (
                <tr key={w.warehouseId}>
                  <td style={{ fontWeight: 600 }}>{w.name}</td>
                  <td className="muted">{w.address || '—'}</td>
                  <td>{w.managerName || '—'}</td>
                  <td className="muted">{w.contactNumber || '—'}</td>
                  <td className="muted">{w.totalCapacitySqm ?? '—'}</td>
                  <td><span className={`badge ${statusClass(w.status)}`}>{w.status}</span></td>
                  <td className="muted">{formatDate(w.createdAt)}</td>
                  <td>
                    <button className="inv-action-btn" title="Edit" onClick={() => openEdit(w)}>✏️</button>
                    <button className="inv-action-btn danger" title="Delete" onClick={() => setConfirmId(w.warehouseId)}>🗑️</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Add / Edit modal */}
      {modal && (
        <div className="inv-modal-overlay" onClick={closeModal}>
          <div className="inv-modal" onClick={e => e.stopPropagation()}>
            <div className="inv-modal-header">
              <h3>{editing ? 'Edit Warehouse' : 'Add Warehouse'}</h3>
              <button className="inv-modal-close" onClick={closeModal}>×</button>
            </div>
            <form onSubmit={handleSave}>
              <div className="inv-field">
                <label>Name *</label>
                <input value={form.name ?? ''} onChange={e => setForm(f => ({ ...f, name: e.target.value }))} placeholder="Warehouse name" />
              </div>
              <div className="inv-field">
                <label>Address</label>
                <input value={form.address ?? ''} onChange={e => setForm(f => ({ ...f, address: e.target.value }))} placeholder="Street address" />
              </div>
              <div className="inv-form-row">
                <div className="inv-field">
                  <label>Latitude</label>
                  <input type="number" step="0.000001" value={form.latitude ?? ''} onChange={e => setForm(f => ({ ...f, latitude: e.target.value ? Number(e.target.value) : null }))} placeholder="e.g. 6.9271" />
                </div>
                <div className="inv-field">
                  <label>Longitude</label>
                  <input type="number" step="0.000001" value={form.longitude ?? ''} onChange={e => setForm(f => ({ ...f, longitude: e.target.value ? Number(e.target.value) : null }))} placeholder="e.g. 79.8612" />
                </div>
              </div>
              <div className="inv-form-row">
                <div className="inv-field">
                  <label>Manager Name</label>
                  <input value={form.managerName ?? ''} onChange={e => setForm(f => ({ ...f, managerName: e.target.value }))} />
                </div>
                <div className="inv-field">
                  <label>Contact Number</label>
                  <input value={form.contactNumber ?? ''} onChange={e => setForm(f => ({ ...f, contactNumber: e.target.value }))} />
                </div>
              </div>
              <div className="inv-form-row">
                <div className="inv-field">
                  <label>Total Capacity (sqm)</label>
                  <input type="number" min="0" step="0.01" value={form.totalCapacitySqm ?? ''} onChange={e => setForm(f => ({ ...f, totalCapacitySqm: e.target.value ? Number(e.target.value) : null }))} />
                </div>
                <div className="inv-field">
                  <label>Status</label>
                  <select value={form.status ?? 'ACTIVE'} onChange={e => setForm(f => ({ ...f, status: e.target.value }))}>
                    <option value="ACTIVE">Active</option>
                    <option value="INACTIVE">Inactive</option>
                  </select>
                </div>
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

      {/* Confirm delete */}
      {confirmId !== null && (
        <div className="inv-modal-overlay" onClick={() => setConfirmId(null)}>
          <div className="inv-confirm" onClick={e => e.stopPropagation()}>
            <h4>Delete Warehouse?</h4>
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
