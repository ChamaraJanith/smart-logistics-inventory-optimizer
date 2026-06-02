import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import LandingPage from '../pages/LandingPage'
import LoginPage from '../pages/LoginPage'
import AppLayout from '../components/layout/AppLayout'
import DashboardPage from '../pages/dashboard/DashboardPage'
import DeliveriesPage from '../pages/deliveries/DeliveriesPage'
import RoutesPage from '../pages/routes/RoutesPage'
import DriversPage from '../pages/drivers/DriversPage'
import VehiclesPage from '../pages/vehicles/VehiclesPage'
import AccountPage from '../pages/AccountPage'
import PrivateRoute from '../auth/PrivateRoute'
// Inventory pages
import InventoryDashboard from '../pages/inventory/InventoryDashboard'
import WarehousesPage from '../pages/inventory/WarehousesPage'
import InventoryItemsPage from '../pages/inventory/InventoryItemsPage'
import InventoryStockPage from '../pages/inventory/InventoryStockPage'
import ReorderAlertsPage from '../pages/inventory/ReorderAlertsPage'
import DemandForecastPage from '../pages/inventory/DemandForecastPage'
import AnomalyDetectionPage from '../pages/inventory/AnomalyDetectionPage'
import StockTransactionsPage from '../pages/inventory/StockTransactionsPage'

export default function Router() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />

        <Route path="/app" element={<PrivateRoute />}>
          <Route element={<AppLayout />}>
            <Route index element={<Navigate to="dashboard" replace />} />
            {/* Logistics */}
            <Route path="dashboard" element={<DashboardPage />} />
            <Route path="deliveries" element={<DeliveriesPage />} />
            <Route path="routes" element={<RoutesPage />} />
            <Route path="drivers" element={<DriversPage />} />
            <Route path="vehicles" element={<VehiclesPage />} />
            <Route path="account" element={<AccountPage />} />
            {/* Inventory */}
            <Route path="inv-dashboard" element={<InventoryDashboard />} />
            <Route path="warehouses" element={<WarehousesPage />} />
            <Route path="inventory-items" element={<InventoryItemsPage />} />
            <Route path="inventory-stock" element={<InventoryStockPage />} />
            <Route path="reorder-alerts" element={<ReorderAlertsPage />} />
            <Route path="demand-forecast" element={<DemandForecastPage />} />
            <Route path="anomalies" element={<AnomalyDetectionPage />} />
            <Route path="stock-transactions" element={<StockTransactionsPage />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
