import { useEffect, useState } from 'react'
import '../../styles/drivers.css'

interface DriverItem {
  driverId: number;
  driverName: string;
  phone: string;
  licenseNo: string;
  status: string;
  vehicleId?: number;
  vehicleNumber?: string;
  createdAt: string;
}

export default function DriversPage() {
  const [drivers, setDrivers] = useState<DriverItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const [isModalOpen, setIsModalOpen] = useState(false)
  const [editingDriver, setEditingDriver] = useState<DriverItem | null>(null)
  
  const [formData, setFormData] = useState<Partial<DriverItem>>({
    driverName: '',
    phone: '',
    licenseNo: '',
    status: 'ACTIVE',
    vehicleId: undefined
  })

  useEffect(() => {
    loadData()
  }, [])

  async function loadData() {
    try {
      setLoading(true)
      setError(null)
      const res = await fetch('/api/v1/drivers')
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`)
      const data = await res.json()
      setDrivers(data)
    } catch (err: any) {
      setError(err.message || 'Failed to load drivers')
    } finally {
      setLoading(false)
    }
  }

  const openNewModal = () => {
    setEditingDriver(null)
    setFormData({
      driverName: '',
      phone: '',
      licenseNo: '',
      status: 'ACTIVE',
      vehicleId: undefined
    })
    setIsModalOpen(true)
  }

  const openEditModal = (d: DriverItem) => {
    setEditingDriver(d)
    setFormData({ ...d })
    setIsModalOpen(true)
  }

  const closeModal = () => setIsModalOpen(false)

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const isEdit = !!editingDriver
      const url = isEdit ? `/api/v1/drivers/${editingDriver.driverId}` : '/api/v1/drivers'
      
      const payload = {
        ...formData,
        vehicleId: formData.vehicleId ? Number(formData.vehicleId) : null
      }

      const res = await fetch(url, {
        method: isEdit ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      })

      if (!res.ok) throw new Error(`Failed to save: ${res.statusText}`)
      
      await loadData()
      closeModal()
    } catch (err: any) {
      alert(err.message || 'Failed to save driver')
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this driver?')) return
    
    try {
      const res = await fetch(`/api/v1/drivers/${id}`, { method: 'DELETE' })
      if (!res.ok) throw new Error(`Failed to delete: ${res.statusText}`)
      await loadData()
    } catch (err: any) {
      alert(err.message || 'Failed to delete driver')
    }
  }

  const getStatusClass = (status: string) => {
    if (!status) return 'pending'
    const s = status.toLowerCase()
    if (s === 'active' || s === 'available') return 'delivered'
    if (s === 'on_leave' || s === 'inactive') return 'failed'
    if (s === 'on_route' || s === 'busy') return 'in_transit'
    return 'pending'
  }

  return (
    <div className="drivers-page">
      <div className="drivers-header">
        <h2>Drivers</h2>
        <div className="drivers-actions">
          <button className="lp-btn lp-btn--ghost" onClick={() => loadData()}>Refresh</button>
          <button className="lp-btn lp-btn--primary" onClick={openNewModal}>+ New Driver</button>
        </div>
      </div>

      <div className="table-container">
        {loading && <div className="empty-state">Loading drivers...</div>}
        {error && <div className="empty-state" style={{ color: 'var(--app-error)' }}>{error}</div>}
        
        {!loading && !error && (
          <table className="app-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Driver Name</th>
                <th>Contact</th>
                <th>License No</th>
                <th>Assigned Vehicle</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {drivers.length === 0 ? (
                <tr>
                  <td colSpan={7}>
                    <div className="empty-state">No drivers found.</div>
                  </td>
                </tr>
              ) : (
                drivers.map(d => (
                  <tr key={d.driverId}>
                    <td style={{ fontWeight: 600, color: 'var(--app-blue)' }}>#{d.driverId}</td>
                    <td style={{ fontWeight: 500 }}>{d.driverName}</td>
                    <td>{d.phone || '—'}</td>
                    <td><div style={{ fontFamily: 'monospace', letterSpacing: 1 }}>{d.licenseNo}</div></td>
                    <td>
                      {d.vehicleId ? (
                        <>
                          <div>ID: {d.vehicleId}</div>
                          <div style={{ fontSize: 12, color: 'var(--app-text-3)' }}>{d.vehicleNumber}</div>
                        </>
                      ) : (
                        <span style={{ color: 'var(--app-text-3)' }}>Unassigned</span>
                      )}
                    </td>
                    <td>
                      <span className={`status-badge ${getStatusClass(d.status)}`}>
                        {d.status || 'UNKNOWN'}
                      </span>
                    </td>
                    <td>
                      <button className="action-btn" title="Edit driver" onClick={() => openEditModal(d)}>✏️</button>
                      <button className="action-btn" title="Delete driver" onClick={() => handleDelete(d.driverId)}>🗑️</button>
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
              <h3>{editingDriver ? 'Edit Driver' : 'New Driver'}</h3>
              <button className="close-btn" onClick={closeModal}>&times;</button>
            </div>
            
            <form onSubmit={handleSave}>
              <div className="form-group">
                <label>Driver Name *</label>
                <input required value={formData.driverName} onChange={e => setFormData({...formData, driverName: e.target.value})} />
              </div>
              
              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Phone Number</label>
                  <input type="tel" value={formData.phone || ''} onChange={e => setFormData({...formData, phone: e.target.value})} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>License Number *</label>
                  <input required value={formData.licenseNo || ''} onChange={e => setFormData({...formData, licenseNo: e.target.value})} style={{ textTransform: 'uppercase' }} />
                </div>
              </div>

              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Assigned Vehicle ID</label>
                  <input type="number" placeholder="Optional" value={formData.vehicleId || ''} onChange={e => setFormData({...formData, vehicleId: e.target.value ? Number(e.target.value) : undefined})} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Status</label>
                  <select value={formData.status || 'ACTIVE'} onChange={e => setFormData({...formData, status: e.target.value})}>
                    <option value="ACTIVE">Active / Available</option>
                    <option value="ON_ROUTE">On Route</option>
                    <option value="ON_LEAVE">On Leave</option>
                    <option value="INACTIVE">Inactive</option>
                  </select>
                </div>
              </div>

              <div className="modal-actions">
                <button type="button" className="lp-btn lp-btn--ghost" onClick={closeModal}>Cancel</button>
                <button type="submit" className="lp-btn lp-btn--primary">Save Driver</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}