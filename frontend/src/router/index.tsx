import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import LandingPage from '../pages/LandingPage'
import AppLayout from '../components/layout/AppLayout'
import DashboardPage from '../pages/dashboard/DashboardPage'

export default function Router() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />

        <Route path="/app" element={<AppLayout />}>
          <Route index element={<Navigate to="dashboard" replace />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="deliveries" element={<div style={{padding:20}}>Deliveries page (todo)</div>} />
          <Route path="routes" element={<div style={{padding:20}}>Routes page (todo)</div>} />
          <Route path="drivers" element={<div style={{padding:20}}>Drivers page (todo)</div>} />
          <Route path="vehicles" element={<div style={{padding:20}}>Vehicles page (todo)</div>} />
          <Route path="warehouses" element={<div style={{padding:20}}>Warehouses page (todo)</div>} />
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
