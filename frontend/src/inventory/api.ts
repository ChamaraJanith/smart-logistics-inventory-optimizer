/**
 * Inventory API helpers — all calls go through authFetch from AuthContext
 * so the JWT token is automatically attached.
 */
import { authFetch } from '../auth/AuthContext'

const BASE = '/api/v1'

// ── Generic helpers ──────────────────────────────────────────────────

async function get<T>(path: string): Promise<T> {
  const res = await authFetch(`${BASE}${path}`)
  if (res.status === 401) { window.location.href = '/login'; throw new Error('Unauthorized') }
  if (!res.ok) {
    const body = await res.text().catch(() => '')
    throw new Error(body || `${res.status} ${res.statusText}`)
  }
  return res.json()
}

async function post<T>(path: string, body: unknown): Promise<T> {
  const res = await authFetch(`${BASE}${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
  if (res.status === 401) { window.location.href = '/login'; throw new Error('Unauthorized') }
  if (!res.ok) {
    const b = await res.text().catch(() => '')
    throw new Error(b || `${res.status} ${res.statusText}`)
  }
  if (res.status === 204) return undefined as T
  return res.json()
}

async function put<T>(path: string, body: unknown): Promise<T> {
  const res = await authFetch(`${BASE}${path}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
  if (res.status === 401) { window.location.href = '/login'; throw new Error('Unauthorized') }
  if (!res.ok) {
    const b = await res.text().catch(() => '')
    throw new Error(b || `${res.status} ${res.statusText}`)
  }
  return res.json()
}

async function patch<T>(path: string, body?: unknown): Promise<T> {
  const res = await authFetch(`${BASE}${path}`, {
    method: 'PATCH',
    headers: body ? { 'Content-Type': 'application/json' } : {},
    body: body ? JSON.stringify(body) : undefined,
  })
  if (res.status === 401) { window.location.href = '/login'; throw new Error('Unauthorized') }
  if (!res.ok) {
    const b = await res.text().catch(() => '')
    throw new Error(b || `${res.status} ${res.statusText}`)
  }
  return res.json()
}

async function del(path: string): Promise<void> {
  const res = await authFetch(`${BASE}${path}`, { method: 'DELETE' })
  if (res.status === 401) { window.location.href = '/login'; throw new Error('Unauthorized') }
  if (!res.ok) {
    const b = await res.text().catch(() => '')
    throw new Error(b || `${res.status} ${res.statusText}`)
  }
}

// ── Types ────────────────────────────────────────────────────────────

export interface WarehouseResponse {
  warehouseId: number
  name: string
  address: string | null
  latitude: number | null
  longitude: number | null
  managerName: string | null
  contactNumber: string | null
  totalCapacitySqm: number | null
  status: string
  createdAt: string
}

export interface InventoryItemResponse {
  itemId: number
  warehouseId: number
  warehouseName: string
  sku: string
  itemName: string
  category: string | null
  unit: string
  unitWeightKg: number | null
  unitVolume: number | null
  unitPrice: number | null
  status: string
  createdAt: string
}

export interface InventoryStockResponse {
  stockId: number
  itemId: number
  itemName: string
  sku: string
  warehouseId: number
  warehouseName: string
  quantityOnHand: number
  reservedQuantity: number
  availableQuantity: number
  reorderLevel: number
  reorderQuantity: number
  maxStockLevel: number | null
  stockStatus: 'NORMAL' | 'LOW' | 'CRITICAL' | 'OUT_OF_STOCK'
  lastUpdated: string
}

export interface ReorderAlertResponse {
  alertId: number
  stockId: number
  itemId: number
  itemName: string
  sku: string
  warehouseId: number
  warehouseName: string
  alertType: string
  currentStock: number
  reorderLevel: number
  suggestedReorderQty: number
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  status: string
  triggeredAt: string
  resolvedAt: string | null
  predictedDemand7d: number | null
}

export interface DemandForecastResponse {
  forecastId: number
  itemId: number
  itemName: string
  sku: string
  warehouseId: number
  warehouseName: string
  forecastDate: string
  predictedDemand: number
  confidenceScore: number
  seasonPattern: string | null
  modelVersion: string | null
  createdAt: string
}

export interface AnomalyLogResponse {
  anomalyId: number
  itemId: number
  itemName: string
  sku: string
  warehouseId: number
  warehouseName: string
  anomalyType: string
  description: string | null
  deviationScore: number
  status: string
  detectedAt: string
  resolvedAt: string | null
}

export interface StockTransactionResponse {
  transactionId: number
  stockId: number
  itemId: number
  itemName: string
  sku: string
  warehouseId: number
  warehouseName: string
  transactionType: 'RESTOCK' | 'DISPATCH' | 'ADJUSTMENT' | 'RESERVE' | 'RELEASE_RESERVE'
  quantity: number
  quantityBefore: number
  quantityAfter: number
  performedBy: string | null
  notes: string | null
  createdAt: string
}

export interface InventoryDashboardSummary {
  totalWarehouses: number
  totalItems: number
  lowStockCount: number
  outOfStockCount: number
  openAlertsCount: number
  criticalAlertsCount: number
  openAnomaliesCount: number
  todayTransactionsCount: number
}

export interface StatusCount {
  status: string
  count: number
}

export interface WarehouseStockSummary {
  warehouseId: number
  warehouseName: string
  warehouseStatus: string
  totalItems: number
  normalStockCount: number
  lowStockCount: number
  criticalStockCount: number
  outOfStockCount: number
  openAlertsCount: number
}

export interface RouteStockItemValidation {
  itemId: number
  itemName: string
  sku: string
  quantityRequired: number
  availableQuantity: number
  isShort: boolean
}

export interface RouteStockValidation {
  items: RouteStockItemValidation[]
  hasShortage: boolean
  totalWeightKg: number
  totalVolume: number
  vehicleWeightCapacity: number
  vehicleVolumeCapacity: number
}

// ── Inventory Dashboard ──────────────────────────────────────────────

export const invDashboard = {
  getSummary: () => get<InventoryDashboardSummary>('/inventory-dashboard/summary'),
  getStockStatusCounts: () => get<StatusCount[]>('/inventory-dashboard/stock-status-counts'),
  getWarehouseSummary: () => get<WarehouseStockSummary[]>('/inventory-dashboard/warehouse-summary'),
  getRecentAlerts: () => get<ReorderAlertResponse[]>('/inventory-dashboard/recent-alerts'),
  getRecentAnomalies: () => get<AnomalyLogResponse[]>('/inventory-dashboard/recent-anomalies'),
}

// ── Warehouses ────────────────────────────────────────────────────────

export const warehouseApi = {
  getAll: () => get<WarehouseResponse[]>('/warehouses'),
  getByStatus: (status: string) => get<WarehouseResponse[]>(`/warehouses/by-status?status=${encodeURIComponent(status)}`),
  create: (body: object) => post<WarehouseResponse>('/warehouses', body),
  update: (id: number, body: object) => put<WarehouseResponse>(`/warehouses/${id}`, body),
  delete: (id: number) => del(`/warehouses/${id}`),
}

// ── Inventory Items ────────────────────────────────────────────────────

export const itemApi = {
  getAll: () => get<InventoryItemResponse[]>('/inventory-items'),
  getByWarehouse: (wid: number) => get<InventoryItemResponse[]>(`/inventory-items/warehouse/${wid}`),
  getByCategory: (cat: string) => get<InventoryItemResponse[]>(`/inventory-items/category/${encodeURIComponent(cat)}`),
  create: (body: object) => post<InventoryItemResponse>('/inventory-items', body),
  update: (id: number, body: object) => put<InventoryItemResponse>(`/inventory-items/${id}`, body),
  delete: (id: number) => del(`/inventory-items/${id}`),
}

// ── Inventory Stock ────────────────────────────────────────────────────

export const stockApi = {
  getAll: () => get<InventoryStockResponse[]>('/inventory-stock'),
  getLowStock: () => get<InventoryStockResponse[]>('/inventory-stock/low-stock'),
  getByWarehouse: (wid: number) => get<InventoryStockResponse[]>(`/inventory-stock/warehouse/${wid}`),
  create: (body: object) => post<InventoryStockResponse>('/inventory-stock', body),
  update: (stockId: number, body: object) => patch<InventoryStockResponse>(`/inventory-stock/${stockId}/update`, body),
}

// ── Reorder Alerts ────────────────────────────────────────────────────

export const alertApi = {
  getAll: () => get<ReorderAlertResponse[]>('/reorder-alerts'),
  getBySeverity: (sev: string) => get<ReorderAlertResponse[]>(`/reorder-alerts/severity/${encodeURIComponent(sev)}`),
  getByWarehouse: (wid: number) => get<ReorderAlertResponse[]>(`/reorder-alerts/warehouse/${wid}`),
  resolve: (alertId: number) => patch<ReorderAlertResponse>(`/reorder-alerts/${alertId}/resolve`),
  planReplenishment: (alertId: number) => post<any>(`/reorder-alerts/${alertId}/plan-replenishment`, {}),
}

// ── Demand Forecast ────────────────────────────────────────────────────

export const forecastApi = {
  generate: (body: { itemId: number; warehouseId: number; forecastDays: number }) =>
    post<DemandForecastResponse[]>('/demand-forecast/generate', body),
  generateForWarehouse: (warehouseId: number, days = 7) =>
    post<DemandForecastResponse[]>(`/demand-forecast/generate/warehouse/${warehouseId}?days=${days}`, {}),
  getByItem: (itemId: number) => get<DemandForecastResponse[]>(`/demand-forecast/item/${itemId}`),
  getByWarehouse: (wid: number) => get<DemandForecastResponse[]>(`/demand-forecast/warehouse/${wid}`),
}

// ── Anomalies ────────────────────────────────────────────────────────

export const anomalyApi = {
  getAll: () => get<AnomalyLogResponse[]>('/anomalies'),
  getByWarehouse: (wid: number) => get<AnomalyLogResponse[]>(`/anomalies/warehouse/${wid}`),
  resolve: (anomalyId: number) => patch<AnomalyLogResponse>(`/anomalies/${anomalyId}/resolve`),
}

// ── Stock Transactions ─────────────────────────────────────────────────

export const txApi = {
  getAll: () => get<StockTransactionResponse[]>('/stock-transactions'),
  getByType: (type: string) => get<StockTransactionResponse[]>(`/stock-transactions/type/${encodeURIComponent(type)}`),
  getByWarehouse: (wid: number) => get<StockTransactionResponse[]>(`/stock-transactions/warehouse/${wid}`),
  getByItem: (iid: number) => get<StockTransactionResponse[]>(`/stock-transactions/item/${iid}`),
}

// ── Routes ────────────────────────────────────────────────────────────

export const routeApi = {
  validateStock: (routeId: number) => get<RouteStockValidation>(`/routes/${routeId}/stock-validation`),
  allocateStock: (routeId: number) => post<void>(`/routes/${routeId}/allocate-stock`, {}),
}

