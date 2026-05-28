import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import LandingPage from '../pages/LandingPage'
import AppLayout from '../components/layout/AppLayout'
import DashboardPage from '../pages/dashboard/DashboardPage'
import DeliveriesPage from '../pages/deliveries/DeliveriesPage'
import RoutesPage from '../pages/routes/RoutesPage'
import DriversPage from '../pages/drivers/DriversPage'
import VehiclesPage from '../pages/vehicles/VehiclesPage'

export default function Router() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />

        <Route path="/app" element={<AppLayout />}>
          <Route index element={<Navigate to="dashboard" replace />} />
          <Route path="dashboard" element={<DashboardPage />} />
          <Route path="deliveries" element={<DeliveriesPage />} />
          <Route path="routes" element={<RoutesPage />} />
          <Route path="drivers" element={<DriversPage />} />
          <Route path="vehicles" element={<VehiclesPage />} />
          <Route path="warehouses" element={<div style={{padding:20}}>Warehouses page (todo)</div>} />
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
