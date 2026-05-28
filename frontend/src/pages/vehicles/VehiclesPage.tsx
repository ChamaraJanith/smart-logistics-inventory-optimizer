import { useEffect, useState } from 'react'
import '../../styles/vehicles.css'

interface VehicleItem {
  vehicleId: number;
  vehicleNumber: string;
  vehicleType: string;
  capacityKg: number;
  maxVolume: number;
  fuelType: string;
  currentStatus: string;
  currentLatitude?: number;
  currentLongitude?: number;
  createdAt: string;
}

export default function VehiclesPage() {
  const [vehicles, setVehicles] = useState<VehicleItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const [isModalOpen, setIsModalOpen] = useState(false)
  const [editingVehicle, setEditingVehicle] = useState<VehicleItem | null>(null)
  
  const [formData, setFormData] = useState<Partial<VehicleItem>>({
    vehicleNumber: '',
    vehicleType: 'TRUCK',
    capacityKg: 0,
    maxVolume: 0,
    fuelType: 'DIESEL',
    currentStatus: 'AVAILABLE',
    currentLatitude: 0,
    currentLongitude: 0
  })

  useEffect(() => {
    loadData()
  }, [])

  async function loadData() {
    try {
      setLoading(true)
      setError(null)
      const res = await fetch('/api/v1/vehicles')
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`)
      const data = await res.json()
      setVehicles(data)
    } catch (err: any) {
      setError(err.message || 'Failed to load vehicles')
    } finally {
      setLoading(false)
    }
  }

  const openNewModal = () => {
    setEditingVehicle(null)
    setFormData({
      vehicleNumber: '',
      vehicleType: 'TRUCK',
      capacityKg: 0,
      maxVolume: 0,
      fuelType: 'DIESEL',
      currentStatus: 'AVAILABLE',
      currentLatitude: 0,
      currentLongitude: 0
    })
    setIsModalOpen(true)
  }

  const openEditModal = (v: VehicleItem) => {
    setEditingVehicle(v)
    setFormData({ ...v })
    setIsModalOpen(true)
  }

  const closeModal = () => setIsModalOpen(false)

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const isEdit = !!editingVehicle
      const url = isEdit ? `/api/v1/vehicles/${editingVehicle.vehicleId}` : '/api/v1/vehicles'
      
      const payload = {
        ...formData,
        capacityKg: Number(formData.capacityKg),
        maxVolume: Number(formData.maxVolume),
        currentLatitude: formData.currentLatitude ? Number(formData.currentLatitude) : null,
        currentLongitude: formData.currentLongitude ? Number(formData.currentLongitude) : null
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
      alert(err.message || 'Failed to save vehicle')
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this vehicle?')) return
    
    try {
      const res = await fetch(`/api/v1/vehicles/${id}`, { method: 'DELETE' })
      if (!res.ok) throw new Error(`Failed to delete: ${res.statusText}`)
      await loadData()
    } catch (err: any) {
      alert(err.message || 'Failed to delete vehicle')
    }
  }

  const getStatusClass = (status: string) => {
    if (!status) return 'pending'
    const s = status.toLowerCase()
    if (s === 'available' || s === 'active') return 'delivered'
    if (s === 'maintenance' || s === 'inactive') return 'failed'
    if (s === 'in_transit' || s === 'busy') return 'in_transit'
    return 'pending'
  }

  return (
    <div className="vehicles-page">
      <div className="vehicles-header">
        <h2>Vehicles</h2>
        <div className="vehicles-actions">
          <button className="lp-btn lp-btn--ghost" onClick={() => loadData()}>Refresh</button>
          <button className="lp-btn lp-btn--primary" onClick={openNewModal}>+ New Vehicle</button>
        </div>
      </div>

      <div className="table-container">
        {loading && <div className="empty-state">Loading vehicles...</div>}
        {error && <div className="empty-state" style={{ color: 'var(--app-error)' }}>{error}</div>}
        
        {!loading && !error && (
          <table className="app-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Reg No.</th>
                <th>Type / Fuel</th>
                <th>Capacity (Kg)</th>
                <th>Max Volume (m³)</th>
                <th>Location</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {vehicles.length === 0 ? (
                <tr>
                  <td colSpan={8}>
                    <div className="empty-state">No vehicles found.</div>
                  </td>
                </tr>
              ) : (
                vehicles.map(v => (
                  <tr key={v.vehicleId}>
                    <td style={{ fontWeight: 600, color: 'var(--app-blue)' }}>#{v.vehicleId}</td>
                    <td><div style={{ fontFamily: 'monospace', letterSpacing: 1, fontWeight: 'bold' }}>{v.vehicleNumber}</div></td>
                    <td>
                      <div>{v.vehicleType}</div>
                      <div style={{ fontSize: 12, color: 'var(--app-text-3)' }}>{v.fuelType}</div>
                    </td>
                    <td>{v.capacityKg}</td>
                    <td>{v.maxVolume}</td>
                    <td>
                      {v.currentLatitude && v.currentLongitude ? (
                        <div style={{ fontSize: 12, fontFamily: 'monospace' }}>
                          {Number(v.currentLatitude).toFixed(4)},<br/>
                          {Number(v.currentLongitude).toFixed(4)}
                        </div>
                      ) : (
                        <span style={{ color: 'var(--app-text-3)' }}>Unknown</span>
                      )}
                    </td>
                    <td>
                      <span className={`status-badge ${getStatusClass(v.currentStatus)}`}>
                        {v.currentStatus || 'UNKNOWN'}
                      </span>
                    </td>
                    <td>
                      <button className="action-btn" title="Edit vehicle" onClick={() => openEditModal(v)}>✏️</button>
                      <button className="action-btn" title="Delete vehicle" onClick={() => handleDelete(v.vehicleId)}>🗑️</button>
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
              <h3>{editingVehicle ? 'Edit Vehicle' : 'New Vehicle'}</h3>
              <button className="close-btn" onClick={closeModal}>&times;</button>
            </div>
            
            <form onSubmit={handleSave}>
              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Vehicle Number *</label>
                  <input required value={formData.vehicleNumber} onChange={e => setFormData({...formData, vehicleNumber: e.target.value})} style={{ textTransform: 'uppercase' }} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Status</label>
                  <select value={formData.currentStatus || 'AVAILABLE'} onChange={e => setFormData({...formData, currentStatus: e.target.value})}>
                    <option value="AVAILABLE">Available</option>
                    <option value="IN_TRANSIT">In Transit</option>
                    <option value="MAINTENANCE">Maintenance</option>
                    <option value="INACTIVE">Inactive</option>
                  </select>
                </div>
              </div>

              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Vehicle Type</label>
                  <select value={formData.vehicleType || 'TRUCK'} onChange={e => setFormData({...formData, vehicleType: e.target.value})}>
                    <option value="TRUCK">Truck</option>
                    <option value="VAN">Van</option>
                    <option value="MOTORCYCLE">Motorcycle</option>
                    <option value="LCT">Light Commercial Truck</option>
                  </select>
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Fuel Type</label>
                  <select value={formData.fuelType || 'DIESEL'} onChange={e => setFormData({...formData, fuelType: e.target.value})}>
                    <option value="DIESEL">Diesel</option>
                    <option value="PETROL">Petrol</option>
                    <option value="ELECTRIC">Electric</option>
                    <option value="HYBRID">Hybrid</option>
                  </select>
                </div>
              </div>
              
              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Capacity (Kg) *</label>
                  <input type="number" step="0.01" required value={formData.capacityKg || 0} onChange={e => setFormData({...formData, capacityKg: Number(e.target.value)})} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Max Volume (m³) *</label>
                  <input type="number" step="0.01" required value={formData.maxVolume || 0} onChange={e => setFormData({...formData, maxVolume: Number(e.target.value)})} />
                </div>
              </div>

              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Latitude</label>
                  <input type="number" step="0.000001" value={formData.currentLatitude || 0} onChange={e => setFormData({...formData, currentLatitude: Number(e.target.value)})} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Longitude</label>
                  <input type="number" step="0.000001" value={formData.currentLongitude || 0} onChange={e => setFormData({...formData, currentLongitude: Number(e.target.value)})} />
                </div>
              </div>

              <div className="modal-actions">
                <button type="button" className="lp-btn lp-btn--ghost" onClick={closeModal}>Cancel</button>
                <button type="submit" className="lp-btn lp-btn--primary">Save Vehicle</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}