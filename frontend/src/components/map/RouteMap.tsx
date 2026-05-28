import { MapContainer, TileLayer, Marker, Popup, Polyline, useMap } from 'react-leaflet'
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
}

export default function RouteMap({ startLat, startLng, endLat, endLng, routeId }: RouteMapProps) {
  // Use Colombo as default center if no coordinates
  
  // Add some pseudo-randomness based on routeId to make static coords look different
  const offset = routeId ? (routeId * 0.01) : 0;
  
  const startPos: [number, number] = [startLat || 6.9271, startLng || 79.8612]
  const endPos: [number, number] = [endLat || (6.8649 + offset), endLng || (79.8997 - offset)] // e.g. Nugegoda
  
  const polyline: [number, number][] = [startPos, endPos]
  const zoomLevel = routeId ? 12 : 11
  
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
        
        <Marker position={startPos}>
          <Popup>
            Start Location (Warehouse)
          </Popup>
        </Marker>
        
        <Marker position={endPos}>
          <Popup>
            End Location (Destination)
          </Popup>
        </Marker>

        <Polyline positions={polyline} color="#4A90E2" weight={4} opacity={0.7} />
      </MapContainer>
    </div>
  )
}