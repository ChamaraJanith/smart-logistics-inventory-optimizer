import { NavLink } from 'react-router-dom'

const items = [
  { to: '/app/dashboard', label: 'Dashboard' },
  { to: '/app/deliveries', label: 'Deliveries' },
  { to: '/app/routes', label: 'Routes' },
  { to: '/app/drivers', label: 'Drivers' },
  { to: '/app/vehicles', label: 'Vehicles' },
  { to: '/app/account', label: 'Account' },
  { to: '/app/warehouses', label: 'Warehouses' },
]

export default function Sidebar({ open }: { open?: boolean }) {
  return (
    <aside className={`app-sidebar ${open ? 'open' : ''}`}>
      <div className="app-sidebar__brand">
        <div className="app-brand-mark" />
        <strong>LogiOptima</strong>
      </div>

      <nav aria-label="Main app navigation" className="app-sidebar__nav">
        {items.map((it) => (
          <div key={it.to} className="app-sidebar__item">
            <NavLink
              to={it.to}
              className={({ isActive }) => `app-link ${isActive ? 'active' : ''}`}
            >
              {it.label}
            </NavLink>
          </div>
        ))}
      </nav>
    </aside>
  )
}
