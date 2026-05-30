import { MapContainer, TileLayer, Marker, Popup, Polyline, useMap, CircleMarker } from 'react-leaflet'
import { useEffect, useState } from 'react'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'

// Fix for default Leaflet icon issues in React
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

// Component to dynamically update map center without remounting
function MapUpdater({ center, zoom }: { center: [number, number], zoom: number }) {
  const map = useMap()
  useEffect(() => {
    map.flyTo(center, zoom, { animate: true, duration: 1 })
  }, [center, zoom, map])
  return null
}

interface RouteMapProps {
  startLat?: number
  startLng?: number
  endLat?: number
  endLng?: number
  routeId?: number
  status?: string
}

export default function RouteMap({ startLat, startLng, endLat, endLng, routeId, status }: RouteMapProps) {
  // Use Colombo as default center if no coordinates
  
  // Add some pseudo-randomness based on routeId to make static coords look different
  const offset = routeId ? (routeId * 0.01) : 0;
  
  const startPos: [number, number] = [startLat || 6.9271, startLng || 79.8612]
  const endPos: [number, number] = [endLat || (6.8649 + offset), endLng || (79.8997 - offset)] // e.g. Nugegoda
  
  const polyline: [number, number][] = [startPos, endPos]
  const zoomLevel = routeId ? 12 : 11
  
  // Status-based color mapping
  let statusColor = '#10b981' // Green (default)
  if (status === 'PLANNED') statusColor = '#f59e0b' // Yellow
  else if (status === 'IN_PROGRESS') statusColor = '#10b981' // Green
  else if (status === 'ACTIVE') statusColor = '#10b981' // Green
  else if (status === 'COMPLETED') statusColor = '#6b7280' // Gray
  
  const [mounted, setMounted] = useState(false)
  useEffect(() => {
    setMounted(true)
  }, [])

  if (!mounted) return null

  return (
    <div style={{ height: '350px', width: '100%', borderRadius: '16px', overflow: 'hidden', marginTop: '24px', border: '1px solid var(--border)', boxShadow: 'var(--shadow-md)' }}>
      <MapContainer 
        center={startPos} 
        zoom={zoomLevel} 
        scrollWheelZoom={false} 
        style={{ height: '100%', width: '100%', background: '#1a1d24' }}
      >
        <MapUpdater center={startPos} zoom={zoomLevel} />
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OSM</a>'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          className="map-tiles"
        />
        
        {/* Start Location - Warehouse (Blue) */}
        <CircleMarker 
          center={startPos} 
          radius={10}
          color="#3b82f6"
          fillColor="#3b82f6"
          fillOpacity={0.8}
          weight={2}
        >
          <Popup>
            <div style={{ color: '#000', fontSize: '13px' }}>
              <strong>📍 Start Location</strong><br/>
              Warehouse
            </div>
          </Popup>
        </CircleMarker>
        
        {/* End Location - Destination (Status-based color) */}
        <CircleMarker 
          center={endPos}
          radius={10}
          color={statusColor}
          fillColor={statusColor}
          fillOpacity={0.8}
          weight={2}
        >
          <Popup>
            <div style={{ color: '#000', fontSize: '13px' }}>
              <strong style={{ color: statusColor }}>📦 End Location</strong><br/>
              Destination<br/>
              <span style={{ fontSize: '12px', color: '#666' }}>Status: {status || 'N/A'}</span>
            </div>
          </Popup>
        </CircleMarker>

        <Polyline positions={polyline} color={statusColor} weight={3} opacity={0.7} dashArray="5,5" />
      </MapContainer>
    </div>
  )
}