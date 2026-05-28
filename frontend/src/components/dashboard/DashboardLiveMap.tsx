import { MapContainer, TileLayer, Marker, Popup, CircleMarker } from 'react-leaflet'
import { useEffect, useState } from 'react'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'

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

const colomboCenter: [number, number] = [6.9271, 79.8612]

const activeDeliveries = [
  { id: 1, lat: 6.9310, lng: 79.8510, status: 'In Transit', vehicle: 'ABC-1234' },
  { id: 2, lat: 6.9040, lng: 79.8600, status: 'Arriving', vehicle: 'XYZ-9876' },
  { id: 3, lat: 6.8900, lng: 79.8750, status: 'Delayed', vehicle: 'LMN-4567' },
  { id: 4, lat: 6.9150, lng: 79.8800, status: 'In Transit', vehicle: 'DEF-3456' },
]

export default function DashboardLiveMap() {
  const [mounted, setMounted] = useState(false)
  
  useEffect(() => {
    setMounted(true)
  }, [])

  if (!mounted) return null

  return (
    <div style={{ height: '320px', width: '100%', borderRadius: '12px', overflow: 'hidden', border: '1px solid var(--border)', position: 'relative' }}>
        <div style={{
            position: 'absolute', top: 10, left: 10, zIndex: 1000, 
            background: 'rgba(26, 29, 36, 0.85)', padding: '6px 12px', 
            borderRadius: '6px', fontSize: '13px', color: '#e5e7eb',
            backdropFilter: 'blur(4px)', border: '1px solid var(--border)',
            display: 'flex', alignItems: 'center', gap: '8px'
        }}>
            <span style={{ width: '8px', height: '8px', background: '#3b82f6', borderRadius: '50%', display: 'inline-block', boxShadow: '0 0 8px #3b82f6' }}></span>
            Live Locations
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
            <Popup>Main Warehouse (Colombo HQ)</Popup>
        </CircleMarker>

        {activeDeliveries.map(d => (
            <Marker key={d.id} position={[d.lat, d.lng]}>
                <Popup>
                    <div style={{ color: '#000', fontSize: '13px' }}>
                        <strong style={{ display: 'block', marginBottom: '2px' }}>{d.vehicle}</strong>
                        Status: <span style={{ color: d.status === 'Delayed' ? '#ef4444' : '#10b981' }}>{d.status}</span>
                    </div>
                </Popup>
            </Marker>
        ))}
      </MapContainer>
    </div>
  )
}