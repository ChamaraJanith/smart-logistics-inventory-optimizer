import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import '../styles/landing.css'
import { useAuth } from '../auth/AuthContext'

export default function LoginPage() {
  const auth = useAuth()
  const navigate = useNavigate()
  const [username, setUsername] = useState('admin')
  const [password, setPassword] = useState('admin123')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    setError(null)
    setLoading(true)

    try {
      await auth.login(username.trim(), password)
      navigate('/app/dashboard', { replace: true })
    } catch (err: any) {
      setError(err.message || 'Login failed')
    } finally {
      setLoading(false)
    }
  }

  const fillDemoCredentials = () => {
    setUsername('admin')
    setPassword('admin123')
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1>Sign In</h1>
        <p>Enter your username and password to access the dashboard.</p>

        <div className="auth-demo-box">
          <div className="auth-demo-title">📝 Demo Credentials</div>
          <div className="auth-demo-content">
            <div className="auth-demo-item">
              <span className="auth-demo-label">Username:</span>
              <span className="auth-demo-value">admin</span>
            </div>
            <div className="auth-demo-item">
              <span className="auth-demo-label">Password:</span>
              <span className="auth-demo-value">admin123</span>
            </div>
            <button 
              type="button" 
              onClick={fillDemoCredentials}
              className="auth-demo-button"
            >
              Auto-fill Demo Credentials
            </button>
          </div>
        </div>

        {error && <div className="auth-error">{error}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <label>
            Username
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
              required
            />
          </label>

          <label>
            Password
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
              required
            />
          </label>

          <button type="submit" className="lp-btn lp-btn--primary lp-btn--lg" disabled={loading}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>

        <Link to="/" className="lp-btn lp-btn--outline lp-btn--white lp-btn--sm">
          Back to landing
        </Link>
      </div>
    </div>
  )
}
