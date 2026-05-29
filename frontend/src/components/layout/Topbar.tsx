import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../auth/AuthContext'

export default function Topbar({ onMenuClick }: { onMenuClick: () => void }) {
  const auth = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    auth.logout()
    navigate('/login', { replace: true })
  }

  return (
    <header className="app-topbar">
      <div className="app-topbar__left">
        <button onClick={onMenuClick} aria-label="Toggle navigation menu" className="app-topbar__menu">☰</button>
        <h3 className="app-topbar__title">Dashboard</h3>
      </div>

      <div className="app-topbar__right">
        <div className="app-topbar__greet">Hello, {auth.username ?? 'Admin'}</div>
        <button className="app-topbar__logout" onClick={handleLogout}>Logout</button>
        <div className="app-topbar__avatar" />
      </div>
    </header>
  )
}
