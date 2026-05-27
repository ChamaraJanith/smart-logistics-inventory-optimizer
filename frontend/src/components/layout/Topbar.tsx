export default function Topbar({ onMenuClick }: { onMenuClick: () => void }) {
  return (
    <header className="app-topbar">
      <div className="app-topbar__left">
        <button onClick={onMenuClick} aria-label="Toggle navigation menu" className="app-topbar__menu">☰</button>
        <h3 className="app-topbar__title">Dashboard</h3>
      </div>

      <div className="app-topbar__right">
        <div className="app-topbar__greet">Hello, Admin</div>
        <div className="app-topbar__avatar" />
      </div>
    </header>
  )
}
