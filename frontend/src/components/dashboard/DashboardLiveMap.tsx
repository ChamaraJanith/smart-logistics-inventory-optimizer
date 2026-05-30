import { MapContainer, TileLayer, Marker, Popup, CircleMarker } from 'react-leaflet'
import { useEffect, useState } from 'react'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'
import { authFetch } from '../../auth/AuthContext'

import iconUrl from 'leaflet/dist/images/marker-icon.png'
import iconShadow from 'leaflet/dist/images/marker-shadow.png'

const DefaultIcon = L.icon({
  iconUrl,
  shadowUrl: iconShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
})

L.Marker.prototype.options.icon = DefaultIcon

const statusColors = {
  'In Transit': '#10b981', // Green
  'Planned': '#f59e0b',     // Amber/Yellow
  'Delayed': '#ef4444',     // Red
  'Arriving': '#06b6d4',    // Cyan
}

const colomboCenter: [number, number] = [6.9271, 79.8612]

// Map location names to approximate Colombo area coordinates
const locationCoordinates: Record<string, [number, number]> = {
  'Main Warehouse': [6.9271, 79.8612],
  'Colombo': [6.9271, 79.8612],
  'Nugegoda': [6.8649, 79.8997],
  'Dehiwala': [6.8360, 79.8597],
  'Mount Lavinia': [6.7788, 79.8640],
  'Ratmalana': [6.8285, 79.8852],
  'Malabe': [6.9397, 80.0035],
  'Anuradhapura': [8.3352, 80.4167],
  'Kandy': [7.2906, 80.6337],
  'Galle': [6.0535, 80.2193],
}

interface DeliveryMarker {
  id: number
  lat: number
  lng: number
  vehicle: string
  status: string
  location: string
}

export default function DashboardLiveMap() {
  const [mounted, setMounted] = useState(false)
  const [deliveries, setDeliveries] = useState<DeliveryMarker[]>([])
  const [loading, setLoading] = useState(true)
  
  useEffect(() => {
    setMounted(true)
  }, [])

  useEffect(() => {
    if (!mounted) return

    async function fetchDeliveries() {
      try {
        const res = await authFetch('/api/v1/routes')
        if (!res.ok) throw new Error('Failed to fetch routes')
        const routes = await res.json()

        // Transform routes into delivery markers
        const markers: DeliveryMarker[] = routes
          .filter((r: any) => r.routeStatus !== 'COMPLETED') // Show all non-completed routes
          .map((r: any, idx: number) => {
            const endCoords = locationCoordinates[r.endLocation] || [
              6.9271 + (idx * 0.05),
              79.8612 + (idx * 0.05),
            ]
            
            // Status mapping
            let status = 'Planned'
            if (r.routeStatus === 'IN_PROGRESS') status = 'In Transit'
            else if (r.routeStatus === 'ACTIVE') status = 'In Transit'
            else if (r.routeStatus === 'PLANNED') status = 'Planned'
            
            return {
              id: r.routeId,
              lat: endCoords[0],
              lng: endCoords[1],
              vehicle: r.vehicleNumber,
              status,
              location: r.endLocation,
            }
          })
        
        setDeliveries(markers)
      } catch (err) {
        console.error('Error fetching deliveries:', err)
        setDeliveries([])
      } finally {
        setLoading(false)
      }
    }

    fetchDeliveries()
    
    // Refresh every 30 seconds for live updates
    const interval = setInterval(fetchDeliveries, 30000)
    return () => clearInterval(interval)
  }, [mounted])

  if (!mounted) return null

  return (
    <div style={{ height: '320px', width: '100%', borderRadius: '12px', overflow: 'hidden', border: '1px solid var(--border)', position: 'relative' }}>
        <div style={{
            position: 'absolute', top: 10, left: 10, zIndex: 1000, 
            background: 'rgba(26, 29, 36, 0.85)', padding: '8px 14px', 
            borderRadius: '6px', fontSize: '13px', color: '#e5e7eb',
            backdropFilter: 'blur(4px)', border: '1px solid var(--border)',
            display: 'flex', alignItems: 'center', gap: '12px'
        }}>
            <span style={{ width: '8px', height: '8px', background: '#3b82f6', borderRadius: '50%', display: 'inline-block', boxShadow: '0 0 8px #3b82f6' }}></span>
            Live Locations
            <div style={{ display: 'flex', gap: '10px', fontSize: '11px', color: '#d1d5db' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <span style={{ width: '6px', height: '6px', background: '#10b981', borderRadius: '50%' }}></span> In Transit
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <span style={{ width: '6px', height: '6px', background: '#f59e0b', borderRadius: '50%' }}></span> Planned
              </div>
            </div>
        </div>
      <MapContainer 
        center={colomboCenter} 
        zoom={12} 
        scrollWheelZoom={false} 
        zoomControl={false}
        style={{ height: '100%', width: '100%', background: '#1a1d24' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          className="map-tiles"
        />
        
        {/* Main Warehouse Circle */}
        <CircleMarker center={colomboCenter} radius={14} color="#3b82f6" fillColor="#3b82f6" fillOpacity={0.2} stroke={true} weight={2}>
            <Popup>
              <div style={{ color: '#000', fontSize: '13px' }}>
                <strong>Main Warehouse</strong><br/>
                Colombo HQ
              </div>
            </Popup>
        </CircleMarker>

        {/* Active Deliveries with colored circle markers */}
        {deliveries.map(d => {
          const statusColor = statusColors[d.status as keyof typeof statusColors] || '#6b7280'
          
          return (
            <CircleMarker 
              key={d.id}
              center={[d.lat, d.lng]} 
              radius={12}
              color={statusColor}
              fillColor={statusColor}
              fillOpacity={0.8}
              weight={2}
            >
                <Popup>
                    <div style={{ color: '#000', fontSize: '13px', minWidth: '160px' }}>
                        <strong style={{ display: 'block', marginBottom: '4px', color: statusColor }}>
                          🚚 {d.vehicle}
                        </strong>
                        <div style={{ marginBottom: '3px' }}>
                          <span style={{ color: '#666', fontSize: '12px' }}>Destination:</span> {d.location}
                        </div>
                        <div>
                          <span style={{ color: '#666', fontSize: '12px' }}>Status:</span> 
                          <span style={{ color: statusColor, marginLeft: '4px', fontWeight: '600' }}>{d.status}</span>
                        </div>
                    </div>
                </Popup>
            </CircleMarker>
          )
        })}
      </MapContainer>
    </div>
  )
}