import { useEffect, useState } from 'react'
import { authFetch } from '../../auth/AuthContext'
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
  const [validationReport, setValidationReport] = useState<any | null>(null)
  const [validating, setValidating] = useState(false)
  const [allocating, setAllocating] = useState(false)

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

  useEffect(() => {
    if (selectedRoute) {
      loadValidationReport(selectedRoute.routeId)
    } else {
      setValidationReport(null)
    }
  }, [selectedRoute])

  async function loadValidationReport(routeId: number) {
    try {
      setValidating(true)
      const res = await authFetch(`/api/v1/routes/${routeId}/stock-validation`)
      if (!res.ok) throw new Error()
      const data = await res.json()
      setValidationReport(data)
    } catch (e) {
      setValidationReport(null)
    } finally {
      setValidating(false)
    }
  }

  async function handleAllocateStock() {
    if (!selectedRoute) return
    try {
      setAllocating(true)
      const res = await authFetch(`/api/v1/routes/${selectedRoute.routeId}/allocate-stock`, {
        method: 'POST'
      })
      if (!res.ok) {
        const text = await res.text()
        throw new Error(text || 'Allocation failed')
      }
      alert('Stock allocated and reserved successfully!')
      loadValidationReport(selectedRoute.routeId)
      loadData()
    } catch (err: any) {
      alert(err.message || 'Allocation failed')
    } finally {
      setAllocating(false)
    }
  }

  async function handleDispatchRoute() {
    if (!selectedRoute) return
    try {
      const res = await authFetch(`/api/v1/routes/${selectedRoute.routeId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...selectedRoute,
          routeStatus: 'ACTIVE'
        })
      })
      if (!res.ok) throw new Error('Dispatch failed')
      alert('Route dispatched successfully! Stock has been deducted from the warehouse.')
      loadData()
      const updated = await res.json()
      setSelectedRoute(updated)
    } catch (err: any) {
      alert(err.message || 'Failed to dispatch route')
    }
  }

  async function loadData() {
    try {
      setLoading(true)
      setError(null)
      const res = await authFetch('/api/v1/routes')
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
      alert(err.message || 'Failed to save route')
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Are you sure you want to delete this route?')) return
    
    try {
      const res = await authFetch(`/api/v1/routes/${id}`, {
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

      {selectedRoute && (
        <div className="manifest-section" style={{
          background: 'rgba(30, 41, 59, 0.45)',
          backdropFilter: 'blur(12px)',
          border: '1px solid rgba(255, 255, 255, 0.08)',
          borderRadius: '16px',
          padding: '24px',
          marginTop: '24px',
          boxShadow: '0 12px 40px 0 rgba(0, 0, 0, 0.25)'
        }}>
          <h3 style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', margin: 0, paddingBottom: '16px', borderBottom: '1px solid rgba(255, 255, 255, 0.1)', color: '#fff' }}>
            <span style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>📦 Route #{selectedRoute.routeId} Dispatch Manifest</span>
            <span style={{ fontSize: '13px', background: 'rgba(59, 130, 246, 0.15)', color: '#60a5fa', padding: '4px 12px', borderRadius: '20px', fontWeight: 600 }}>
              Start Warehouse: #{selectedRoute.startWarehouseId}
            </span>
          </h3>

          {validating ? (
            <div style={{ padding: '32px 0', textAlign: 'center', color: 'var(--app-text-3)', fontSize: '15px' }}>
              🔄 Analyzing warehouse stock levels & vehicle capacity...
            </div>
          ) : validationReport ? (
            <div>
              {/* Payload Progress Bars */}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '24px', margin: '20px 0' }}>
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px', fontSize: '13px', color: 'var(--app-text-2)' }}>
                    <span>Weight Payload ({validationReport.totalWeightKg.toFixed(1)} / {validationReport.vehicleWeightCapacity} kg)</span>
                    <span style={{ fontWeight: 600, color: validationReport.totalWeightKg > validationReport.vehicleWeightCapacity ? 'var(--app-error)' : '#10b981' }}>
                      {((validationReport.totalWeightKg / validationReport.vehicleWeightCapacity) * 100).toFixed(0)}%
                    </span>
                  </div>
                  <div style={{ height: '8px', background: 'rgba(255, 255, 255, 0.1)', borderRadius: '4px', overflow: 'hidden' }}>
                    <div style={{
                      height: '100%',
                      width: `${Math.min((validationReport.totalWeightKg / validationReport.vehicleWeightCapacity) * 100, 100)}%`,
                      background: validationReport.totalWeightKg > validationReport.vehicleWeightCapacity ? 'var(--app-error)' : 'linear-gradient(90deg, #3b82f6, #10b981)',
                      transition: 'width 0.3s ease'
                    }} />
                  </div>
                </div>

                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px', fontSize: '13px', color: 'var(--app-text-2)' }}>
                    <span>Volume Payload ({validationReport.totalVolume.toFixed(1)} / {validationReport.vehicleVolumeCapacity} m³)</span>
                    <span style={{ fontWeight: 600, color: validationReport.totalVolume > validationReport.vehicleVolumeCapacity ? 'var(--app-error)' : '#10b981' }}>
                      {((validationReport.totalVolume / validationReport.vehicleVolumeCapacity) * 100).toFixed(0)}%
                    </span>
                  </div>
                  <div style={{ height: '8px', background: 'rgba(255, 255, 255, 0.1)', borderRadius: '4px', overflow: 'hidden' }}>
                    <div style={{
                      height: '100%',
                      width: `${Math.min((validationReport.totalVolume / validationReport.vehicleVolumeCapacity) * 100, 100)}%`,
                      background: validationReport.totalVolume > validationReport.vehicleVolumeCapacity ? 'var(--app-error)' : 'linear-gradient(90deg, #3b82f6, #10b981)',
                      transition: 'width 0.3s ease'
                    }} />
                  </div>
                </div>
              </div>

              {validationReport.totalWeightKg > validationReport.vehicleWeightCapacity && (
                <div style={{ background: 'rgba(239, 68, 68, 0.12)', border: '1px solid rgba(239, 68, 68, 0.3)', color: '#f87171', padding: '12px 16px', borderRadius: '8px', marginBottom: '20px', fontSize: '14px' }}>
                  ⚠️ <strong>Vehicle Overloaded!</strong> The total payload weight exceeds the vehicle's capacity.
                </div>
              )}

              {/* Items Checklist Table */}
              <h4 style={{ margin: '24px 0 12px 0', color: '#fff', fontSize: '15px' }}>Required Inventory Items</h4>
              <table className="app-table" style={{ background: 'transparent', margin: 0 }}>
                <thead>
                  <tr>
                    <th>Item Name</th>
                    <th>SKU</th>
                    <th>Required Qty</th>
                    <th>Available Qty (Start WH)</th>
                    <th>Stock Status</th>
                  </tr>
                </thead>
                <tbody>
                  {validationReport.items.length === 0 ? (
                    <tr>
                      <td colSpan={5} style={{ textAlign: 'center', padding: '16px', color: 'var(--app-text-3)' }}>
                        No items specified for this route's deliveries. Add items to deliveries first.
                      </td>
                    </tr>
                  ) : (
                    validationReport.items.map((item: any) => (
                      <tr key={item.itemId}>
                        <td style={{ fontWeight: 500, color: '#fff' }}>{item.itemName}</td>
                        <td style={{ color: 'var(--app-blue)', fontWeight: 600 }}>{item.sku}</td>
                        <td>{item.quantityRequired}</td>
                        <td>{item.availableQuantity}</td>
                        <td>
                          {item.isShort ? (
                            <span className="status-badge failed" style={{ background: 'rgba(239, 68, 68, 0.15)', color: '#f87171', padding: '4px 10px', borderRadius: '4px', fontSize: '12px', fontWeight: 600 }}>
                              Shortage (-{(item.quantityRequired - item.availableQuantity).toFixed(1)})
                            </span>
                          ) : (
                            <span className="status-badge delivered" style={{ background: 'rgba(16, 185, 129, 0.15)', color: '#34d399', padding: '4px 10px', borderRadius: '4px', fontSize: '12px', fontWeight: 600 }}>
                              In Stock
                            </span>
                          )}
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>

              {/* Action Buttons */}
              <div style={{ display: 'flex', gap: '16px', justifyContent: 'flex-end', marginTop: '24px' }}>
                {selectedRoute.routeStatus === 'PLANNED' && (
                  <button
                    className="lp-btn lp-btn--ghost"
                    disabled={allocating || validationReport.hasShortage || validationReport.items.length === 0}
                    onClick={handleAllocateStock}
                    style={{ display: 'flex', alignItems: 'center', gap: '8px', border: '1px solid rgba(255,255,255,0.15)' }}
                  >
                    📥 {allocating ? 'Allocating...' : 'Reserve Stock'}
                  </button>
                )}

                {selectedRoute.routeStatus === 'PLANNED' && (
                  <button
                    className="lp-btn lp-btn--primary"
                    onClick={handleDispatchRoute}
                    disabled={validationReport.hasShortage || validationReport.items.length === 0 || validationReport.totalWeightKg > validationReport.vehicleWeightCapacity}
                    style={{ display: 'flex', alignItems: 'center', gap: '8px' }}
                  >
                    🚀 Dispatch (Set Active)
                  </button>
                )}
              </div>
            </div>
          ) : (
            <div style={{ padding: '24px 0', textAlign: 'center', color: 'var(--app-text-3)' }}>
              Could not retrieve stock status. Please check if items are linked to the route's deliveries.
            </div>
          )}
        </div>
      )}

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