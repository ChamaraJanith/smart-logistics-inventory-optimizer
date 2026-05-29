import { useEffect, useState } from 'react'
import { authFetch } from '../../auth/AuthContext'
import '../../styles/deliveries.css'

interface Delivery {
  deliveryId: number;
  warehouseId: number;
  customerName: string;
  contactNumber: string;
  deliveryAddress: string;
  priority: string;
  status: string;
  requestedDate: string;
  latitude: number;
  longitude: number;
}

export default function DeliveriesPage() {
  const [deliveries, setDeliveries] = useState<Delivery[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  // Modal states
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [editingDelivery, setEditingDelivery] = useState<Delivery | null>(null)
  const [formData, setFormData] = useState<Partial<Delivery>>({
    warehouseId: 1,
    customerName: '',
    contactNumber: '',
    deliveryAddress: '',
    priority: 'NORMAL',
    status: 'PENDING',
    latitude: 0,
    longitude: 0
  })

  useEffect(() => {
    loadData()
  }, [])

  async function loadData() {
    try {
      setLoading(true)
      setError(null)
      const res = await authFetch('/api/v1/deliveries')
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`)
      const data = await res.json()
      setDeliveries(data)
    } catch (err: any) {
      setError(err.message || 'Failed to load deliveries')
    } finally {
      setLoading(false)
    }
  }

  const openNewModal = () => {
    setEditingDelivery(null)
    setFormData({
      warehouseId: 1,
      customerName: '',
      contactNumber: '',
      deliveryAddress: '',
      priority: 'NORMAL',
      status: 'PENDING',
      latitude: 0,
      longitude: 0
    })
    setIsModalOpen(true)
  }

  const openEditModal = (d: Delivery) => {
    setEditingDelivery(d)
    setFormData({ ...d })
    setIsModalOpen(true)
  }

  const closeModal = () => {
    setIsModalOpen(false)
  }

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const isEdit = !!editingDelivery
      const url = isEdit ? `/api/v1/deliveries/${editingDelivery.deliveryId}` : '/api/v1/deliveries'
      
      const payload = {
        ...formData,
        warehouseId: Number(formData.warehouseId),
        latitude: Number(formData.latitude) || 0,
        longitude: Number(formData.longitude) || 0
      }

      const res = await authFetch(url, {
        method: isEdit ? 'PUT' : 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      })

      if (!res.ok) throw new Error(`Failed to save: ${res.statusText}`)
      
      await loadData()
      closeModal()
    } catch (err: any) {
      alert(err.message || 'Failed to save delivery')
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this delivery?')) return
    
    try {
      const res = await authFetch(`/api/v1/deliveries/${id}`, {
        method: 'DELETE'
      })
      if (!res.ok) throw new Error(`Failed to delete: ${res.statusText}`)
      await loadData()
    } catch (err: any) {
      alert(err.message || 'Failed to delete delivery')
    }
  }

  const getStatusClass = (status: string) => {
    if (!status) return 'pending'
    const s = status.toLowerCase()
    if (s === 'delivered') return 'delivered'
    if (s === 'in_transit' || s === 'in-transit' || s === 'in transit') return 'in_transit'
    if (s === 'failed' || s === 'cancelled') return 'failed'
    return 'pending'
  }

  const formatDate = (dateStr: string) => {
    if (!dateStr) return '—'
    const d = new Date(dateStr)
    return d.toLocaleDateString()
  }

  return (
    <div className="deliveries-page">
      <div className="deliveries-header">
        <h2>Deliveries</h2>
        <div className="deliveries-actions">
          <button className="lp-btn lp-btn--ghost" onClick={() => loadData()}>Refresh</button>
          <button className="lp-btn lp-btn--primary" onClick={openNewModal}>+ New Delivery</button>
        </div>
      </div>

      <div className="table-container">
        {loading && <div className="empty-state">Loading deliveries...</div>}
        {error && <div className="empty-state" style={{ color: 'var(--app-error)' }}>{error}</div>}
        
        {!loading && !error && (
          <table className="app-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Customer</th>
                <th>Address</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {deliveries.length === 0 ? (
                <tr>
                  <td colSpan={7}>
                    <div className="empty-state">No deliveries found.</div>
                  </td>
                </tr>
              ) : (
                deliveries.map(d => (
                  <tr key={d.deliveryId}>
                    <td style={{ fontWeight: 600, color: 'var(--app-blue)' }}>#{d.deliveryId}</td>
                    <td>
                      <div>{d.customerName}</div>
                      <div style={{ fontSize: 12, color: 'var(--app-text-3)' }}>{d.contactNumber}</div>
                    </td>
                    <td>
                      <div style={{ maxWidth: 200, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                        {d.deliveryAddress}
                      </div>
                    </td>
                    <td>{d.priority || 'NORMAL'}</td>
                    <td>
                      <span className={`status-badge ${getStatusClass(d.status)}`}>
                        {d.status || 'PENDING'}
                      </span>
                    </td>
                    <td>{formatDate(d.requestedDate)}</td>
                    <td>
                      <button className="action-btn" title="Edit delivery" onClick={() => openEditModal(d)}>✏️</button>
                      <button className="action-btn" title="Delete delivery" onClick={() => handleDelete(d.deliveryId)}>🗑️</button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        )}
      </div>

      {isModalOpen && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>{editingDelivery ? 'Edit Delivery' : 'New Delivery'}</h3>
              <button className="close-btn" onClick={closeModal}>&times;</button>
            </div>
            
            <form onSubmit={handleSave}>
              <div className="form-group">
                <label>Customer Name *</label>
                <input required value={formData.customerName} onChange={e => setFormData({...formData, customerName: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Contact Number</label>
                <input value={formData.contactNumber || ''} onChange={e => setFormData({...formData, contactNumber: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Delivery Address *</label>
                <input required value={formData.deliveryAddress} onChange={e => setFormData({...formData, deliveryAddress: e.target.value})} />
              </div>
              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Latitude *</label>
                  <input type="number" step="0.000001" required value={formData.latitude} onChange={e => setFormData({...formData, latitude: Number(e.target.value)})} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Longitude *</label>
                  <input type="number" step="0.000001" required value={formData.longitude} onChange={e => setFormData({...formData, longitude: Number(e.target.value)})} />
                </div>
              </div>
              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Priority</label>
                  <select value={formData.priority || 'NORMAL'} onChange={e => setFormData({...formData, priority: e.target.value})}>
                    <option value="LOW">Low</option>
                    <option value="NORMAL">Normal</option>
                    <option value="HIGH">High</option>
                    <option value="URGENT">Urgent</option>
                  </select>
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Status</label>
                  <select value={formData.status || 'PENDING'} onChange={e => setFormData({...formData, status: e.target.value})}>
                    <option value="PENDING">Pending</option>
                    <option value="IN_TRANSIT">In Transit</option>
                    <option value="DELIVERED">Delivered</option>
                    <option value="FAILED">Failed</option>
                    <option value="CANCELLED">Cancelled</option>
                  </select>
                </div>
              </div>

              <div className="modal-actions">
                <button type="button" className="lp-btn lp-btn--ghost" onClick={closeModal}>Cancel</button>
                <button type="submit" className="lp-btn lp-btn--primary">Save Delivery</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}