import { NavLink } from 'react-router-dom'

const SECTIONS = [
  {
    label: 'Logistics',
    items: [
      { to: '/app/dashboard', label: '📊 Dashboard' },
      { to: '/app/deliveries', label: '📦 Deliveries' },
      { to: '/app/routes', label: '📍 Routes' },
      { to: '/app/drivers', label: '🧑‍✈️ Drivers' },
      { to: '/app/vehicles', label: '🚛 Vehicles' },
    ],
  },
  {
    label: 'Inventory',
    items: [
      { to: '/app/inv-dashboard', label: '🗂️ Inv. Dashboard' },
      { to: '/app/warehouses', label: '🏭 Warehouses' },
      { to: '/app/inventory-items', label: '📋 Items' },
      { to: '/app/inventory-stock', label: '📦 Stock' },
      { to: '/app/reorder-alerts', label: '🔔 Reorder Alerts' },
      { to: '/app/demand-forecast', label: '📈 Demand Forecast' },
      { to: '/app/anomalies', label: '⚠️ Anomalies' },
      { to: '/app/stock-transactions', label: '📜 Transactions' },
    ],
  },
  {
    label: 'Account',
    items: [
      { to: '/app/account', label: '⚙️ Account' },
    ],
  },
]

export default function Sidebar({ open }: { open?: boolean }) {
  return (
    <aside className={`app-sidebar ${open ? 'open' : ''}`} style={{ overflowY: 'auto' }}>
      <div className="app-sidebar__brand">
        <div className="app-brand-mark" />
        <strong>LogiOptima</strong>
      </div>

      <nav aria-label="Main app navigation" className="app-sidebar__nav">
        {SECTIONS.map(section => (
          <div key={section.label} style={{ marginBottom: 8 }}>
            <div style={{ fontSize: 10, fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.8px', color: 'var(--text-3)', padding: '8px 16px 4px', marginTop: 8 }}>
              {section.label}
            </div>
            {section.items.map(it => (
              <div key={it.to} className="app-sidebar__item">
                <NavLink
                  to={it.to}
                  className={({ isActive }) => `app-link ${isActive ? 'active' : ''}`}
                >
                  {it.label}
                </NavLink>
              </div>
            ))}
          </div>
        ))}
      </nav>
    </aside>
  )
}
