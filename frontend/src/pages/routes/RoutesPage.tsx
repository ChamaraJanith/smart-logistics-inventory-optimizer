import { useEffect, useState } from 'react'
import '../../styles/routes.css'
import RouteMap from '../../components/map/RouteMap'

interface RouteItem {
  routeId: number;
  vehicleId: number;
  vehicleNumber: string;
  driverId: number;
  driverName: string;
  routeDate: string;
  startWarehouseId: number;
  startLocation: string;
  endLocation: string;
  totalDistanceKm: number;
  estimatedDurationMin: number;
  predictedCost: number;
  predictedDelayRisk: number;
  optimizationScore: number;
  routeStatus: string;
  createdAt: string;
}

export default function RoutesPage() {
  const [routes, setRoutes] = useState<RouteItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const [selectedRoute, setSelectedRoute] = useState<RouteItem | null>(null)

  // Modal states
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [editingRoute, setEditingRoute] = useState<RouteItem | null>(null)
  
  // Notice we must provide valid numbers for warehouse/vehicle since they are @NotNull
  const [formData, setFormData] = useState<Partial<RouteItem>>({
    vehicleId: 1,
    driverId: undefined,
    routeDate: new Date().toISOString().split('T')[0],
    startWarehouseId: 1,
    startLocation: '',
    endLocation: '',
    totalDistanceKm: 0,
    estimatedDurationMin: 0,
    predictedCost: 0,
    predictedDelayRisk: 0,
    optimizationScore: 0,
    routeStatus: 'PLANNED'
  })

  useEffect(() => {
    loadData()
  }, [])

  async function loadData() {
    try {
      setLoading(true)
      setError(null)
      const res = await fetch('/api/v1/routes')
      if (!res.ok) throw new Error(`${res.status} ${res.statusText}`)
      const data = await res.json()
      setRoutes(data)
    } catch (err: any) {
      setError(err.message || 'Failed to load routes')
    } finally {
      setLoading(false)
    }
  }

  const openNewModal = () => {
    setEditingRoute(null)
    setFormData({
      vehicleId: 1,
      driverId: undefined,
      routeDate: new Date().toISOString().split('T')[0],
      startWarehouseId: 1,
      startLocation: '',
      endLocation: '',
      totalDistanceKm: 0,
      estimatedDurationMin: 0,
      predictedCost: 0,
      predictedDelayRisk: 0,
      optimizationScore: 0,
      routeStatus: 'PLANNED'
    })
    setIsModalOpen(true)
  }

  const openEditModal = (r: RouteItem) => {
    setEditingRoute(r)
    setFormData({ ...r })
    setIsModalOpen(true)
  }

  const closeModal = () => {
    setIsModalOpen(false)
  }

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const isEdit = !!editingRoute
      const url = isEdit ? `/api/v1/routes/${editingRoute.routeId}` : '/api/v1/routes'
      
      const payload = {
        ...formData,
        vehicleId: Number(formData.vehicleId),
        driverId: formData.driverId ? Number(formData.driverId) : null,
        startWarehouseId: Number(formData.startWarehouseId),
        totalDistanceKm: Number(formData.totalDistanceKm) || 0,
        estimatedDurationMin: Number(formData.estimatedDurationMin) || 0,
        predictedCost: Number(formData.predictedCost) || 0,
        predictedDelayRisk: Number(formData.predictedDelayRisk) || 0,
        optimizationScore: Number(formData.optimizationScore) || 0
      }

      const res = await fetch(url, {
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
      alert(err.message || 'Failed to save route')
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this route?')) return
    
    try {
      const res = await fetch(`/api/v1/routes/${id}`, {
        method: 'DELETE'
      })
      if (!res.ok) throw new Error(`Failed to delete: ${res.statusText}`)
      await loadData()
    } catch (err: any) {
      alert(err.message || 'Failed to delete route')
    }
  }

  const getStatusClass = (status: string) => {
    if (!status) return 'pending'
    const s = status.toLowerCase()
    if (s === 'completed') return 'delivered'
    if (s === 'in_progress' || s === 'active') return 'in_transit'
    if (s === 'failed' || s === 'cancelled') return 'failed'
    return 'pending'
  }

  return (
    <div className="routes-page">
      <div className="routes-header">
        <h2>Routes</h2>
        <div className="routes-actions">
          <button className="lp-btn lp-btn--ghost" onClick={() => loadData()}>Refresh</button>
          <button className="lp-btn lp-btn--primary" onClick={openNewModal}>+ New Route</button>
        </div>
      </div>

      <RouteMap 
        routeId={selectedRoute?.routeId}
        status={selectedRoute?.routeStatus}
      />

      <div className="table-container" style={{ marginTop: '24px' }}>
        {loading && <div className="empty-state">Loading routes...</div>}
        {error && <div className="empty-state" style={{ color: 'var(--app-error)' }}>{error}</div>}
        
        {!loading && !error && (
          <table className="app-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Vehicle / Driver</th>
                <th>Locations</th>
                <th>Distance / Time</th>
                <th>Status</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {routes.length === 0 ? (
                <tr>
                  <td colSpan={7}>
                    <div className="empty-state">No routes found.</div>
                  </td>
                </tr>
              ) : (
                routes.map(r => (
                  <tr 
                    key={r.routeId} 
                    onClick={() => setSelectedRoute(r)}
                    style={{ 
                      cursor: 'pointer',
                      background: selectedRoute?.routeId === r.routeId ? 'rgba(255, 255, 255, 0.05)' : undefined,
                      borderLeft: selectedRoute?.routeId === r.routeId ? '3px solid var(--app-blue)' : '3px solid transparent'
                    }}
                  >
                    <td style={{ fontWeight: 600, color: 'var(--app-blue)' }}>#{r.routeId}</td>
                    <td>
                      <div>Vehicle ID: {r.vehicleId} {r.vehicleNumber && `(${r.vehicleNumber})`}</div>
                      <div style={{ fontSize: 12, color: 'var(--app-text-3)' }}>
                        Driver ID: {r.driverId} {r.driverName && `(${r.driverName})`}
                      </div>
                    </td>
                    <td>
                      <div>Start: {r.startLocation || `WH-${r.startWarehouseId}`}</div>
                      <div style={{ fontSize: 12, color: 'var(--app-text-3)' }}>End: {r.endLocation || '—'}</div>
                    </td>
                    <td>
                      <div>{r.totalDistanceKm} km</div>
                      <div style={{ fontSize: 12, color: 'var(--app-text-3)' }}>{r.estimatedDurationMin} mins</div>
                    </td>
                    <td>
                      <span className={`status-badge ${getStatusClass(r.routeStatus)}`}>
                        {r.routeStatus || 'PLANNED'}
                      </span>
                    </td>
                    <td>{r.routeDate}</td>
                    <td>
                      <button className="action-btn" title="Edit route" onClick={(e) => { e.stopPropagation(); openEditModal(r); }}>✏️</button>
                      <button className="action-btn" title="Delete route" onClick={(e) => { e.stopPropagation(); handleDelete(r.routeId); }}>🗑️</button>
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
              <h3>{editingRoute ? 'Edit Route' : 'New Route'}</h3>
              <button className="close-btn" onClick={closeModal}>&times;</button>
            </div>
            
            <form onSubmit={handleSave}>
              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Vehicle ID *</label>
                  <input type="number" required value={formData.vehicleId} onChange={e => setFormData({...formData, vehicleId: Number(e.target.value)})} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Driver ID</label>
                  <input type="number" value={formData.driverId || ''} onChange={e => setFormData({...formData, driverId: e.target.value ? Number(e.target.value) : undefined})} />
                </div>
              </div>
              
              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Route Date *</label>
                  <input type="date" required value={formData.routeDate?.split('T')[0]} onChange={e => setFormData({...formData, routeDate: e.target.value})} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Start Warehouse ID *</label>
                  <input type="number" required value={formData.startWarehouseId} onChange={e => setFormData({...formData, startWarehouseId: Number(e.target.value)})} />
                </div>
              </div>

              <div className="form-group">
                <label>Start Location</label>
                <input value={formData.startLocation || ''} onChange={e => setFormData({...formData, startLocation: e.target.value})} />
              </div>
              <div className="form-group">
                <label>End Location</label>
                <input value={formData.endLocation || ''} onChange={e => setFormData({...formData, endLocation: e.target.value})} />
              </div>

              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Distance (km)</label>
                  <input type="number" step="0.1" value={formData.totalDistanceKm} onChange={e => setFormData({...formData, totalDistanceKm: Number(e.target.value)})} />
                </div>
                <div className="form-group" style={{flex: 1}}>
                  <label>Duration (mins)</label>
                  <input type="number" step="0.1" value={formData.estimatedDurationMin} onChange={e => setFormData({...formData, estimatedDurationMin: Number(e.target.value)})} />
                </div>
              </div>
              
              <div style={{display: 'flex', gap: 16}}>
                <div className="form-group" style={{flex: 1}}>
                  <label>Status</label>
                  <select value={formData.routeStatus || 'PLANNED'} onChange={e => setFormData({...formData, routeStatus: e.target.value})}>
                    <option value="PLANNED">Planned</option>
                    <option value="ACTIVE">Active</option>
                    <option value="COMPLETED">Completed</option>
                    <option value="CANCELLED">Cancelled</option>
                  </select>
                </div>
              </div>

              <div className="modal-actions">
                <button type="button" className="lp-btn lp-btn--ghost" onClick={closeModal}>Cancel</button>
                <button type="submit" className="lp-btn lp-btn--primary">Save Route</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}