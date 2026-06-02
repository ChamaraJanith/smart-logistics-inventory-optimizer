import { useState, useEffect, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import '../styles/landing.css'
import '../styles/login.css'
import { useAuth } from '../auth/AuthContext'

// ── All real system features ──────────────────────────────────────────
const MODULES = [
  {
    category: 'Logistics',
    color: '#3b82f6',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <rect x="1" y="3" width="15" height="13"/><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/>
      </svg>
    ),
    features: [
      { icon: '📦', title: 'Delivery Management', desc: 'Full CRUD — create, track & manage all customer deliveries with priority & status' },
      { icon: '📍', title: 'Route Optimization', desc: 'Plan & manage delivery routes by date, status, and route summaries' },
      { icon: '🚚', title: 'Route Deliveries', desc: 'Assign deliveries to routes, update stop statuses in real-time' },
      { icon: '🧑‍✈️', title: 'Driver Management', desc: 'Manage your entire driver roster with performance tracking' },
      { icon: '🚛', title: 'Fleet & Vehicles', desc: 'Track vehicle inventory, utilization rates and availability' },
      { icon: '📊', title: 'Logistics Analytics', desc: 'Delivery trends, route trends, driver performance & heatmaps' },
    ],
  },
  {
    category: 'Inventory',
    color: '#8b5cf6',
    icon: (
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/>
      </svg>
    ),
    features: [
      { icon: '🏭', title: 'Warehouse Management', desc: 'Full CRUD for all storage locations with capacity, manager & geo-coordinates' },
      { icon: '�️', title: 'Inventory Items', desc: 'Product catalog per warehouse with SKU, category, pricing & unit tracking' },
      { icon: '📦', title: 'Stock Management', desc: 'Live stock levels with RESTOCK · DISPATCH · ADJUSTMENT · RESERVE operations' },
      { icon: '🔔', title: 'Reorder Alerts', desc: 'Automated alerts with severity levels and 7-day predicted demand context' },
      { icon: '📈', title: 'AI Demand Forecasting', desc: 'Machine-learning 7-day demand predictions with confidence scoring per item' },
      { icon: '⚠️', title: 'Anomaly Detection', desc: 'AI-powered detection of LARGE_DISPATCH, RAPID_DEPLETION & critical stock events' },
    ],
  },
]

const ALL_FEATURES = [
  ...MODULES[0].features.map(f => ({ ...f, mod: 'Logistics', color: MODULES[0].color })),
  ...MODULES[1].features.map(f => ({ ...f, mod: 'Inventory', color: MODULES[1].color })),
]

const STATS = [
  { value: '12', label: 'Modules', icon: '⚡' },
  { value: 'AI', label: 'Forecasting', icon: '🧠' },
  { value: 'JWT', label: 'Auth', icon: '🔐' },
  { value: 'Live', label: 'Analytics', icon: '📡' },
]

export default function LoginPage() {
  const auth = useAuth()
  const navigate = useNavigate()

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const [focusedField, setFocusedField] = useState<string | null>(null)

  // Carousel state
  const [activeIdx, setActiveIdx] = useState(0)
  const [animating, setAnimating] = useState(false)

  // Auto-cycle through features every 3s
  useEffect(() => {
    const id = setInterval(() => {
      setAnimating(true)
      setTimeout(() => {
        setActiveIdx(i => (i + 1) % ALL_FEATURES.length)
        setAnimating(false)
      }, 300)
    }, 3200)
    return () => clearInterval(id)
  }, [])

  const goTo = (idx: number) => {
    if (idx === activeIdx) return
    setAnimating(true)
    setTimeout(() => { setActiveIdx(idx); setAnimating(false) }, 250)
  }

  const activeFeature = ALL_FEATURES[activeIdx]

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!username.trim()) { setError('Username is required'); return }
    if (!password) { setError('Password is required'); return }
    setError(null)
    setLoading(true)
    try {
      await auth.login(username.trim(), password)
      navigate('/app/dashboard', { replace: true })
    } catch (err: any) {
      setError(err.message || 'Invalid credentials. Please try again.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-root">
      {/* Background orbs */}
      <div className="login-orb login-orb--1" />
      <div className="login-orb login-orb--2" />
      <div className="login-orb login-orb--3" />

      {/* ──────────────── LEFT PANEL ──────────────── */}
      <div className="login-left">

        {/* Brand */}
        <div className="login-brand">
          <div className="login-brand-mark">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
              <polyline points="9 22 9 12 15 12 15 22"/>
            </svg>
          </div>
          <span>LogiOptima</span>
          <div className="login-brand-badge">
            <span className="login-live-dot" />
            System Online
          </div>
        </div>

        {/* Headline */}
        <div className="login-headline">
          <div className="login-tagline">Smart Logistics &amp; Inventory Intelligence</div>
          <h1>
            One platform.<br />
            <span className="lp-gradient-text">All operations.</span>
          </h1>
          <p>End-to-end supply chain visibility — from warehouse shelf to last-mile delivery, powered by AI analytics.</p>
        </div>

        {/* Module tabs */}
        <div className="login-module-tabs">
          {MODULES.map((m) => (
            <button
              key={m.category}
              className={`login-module-tab ${ALL_FEATURES[activeIdx].mod === m.category ? 'active' : ''}`}
              style={{ '--tab-color': m.color } as React.CSSProperties}
              onClick={() => goTo(ALL_FEATURES.findIndex(f => f.mod === m.category))}
            >
              <span className="login-module-tab__icon">{m.icon}</span>
              {m.category}
              <span className="login-module-tab__count">{m.features.length}</span>
            </button>
          ))}
        </div>

        {/* Feature spotlight carousel */}
        <div className={`login-spotlight ${animating ? 'exit' : 'enter'}`}
          style={{ '--spot-color': activeFeature.color } as React.CSSProperties}>
          <div className="login-spotlight__icon">{activeFeature.icon}</div>
          <div className="login-spotlight__body">
            <div className="login-spotlight__module" style={{ color: activeFeature.color }}>
              {activeFeature.mod}
            </div>
            <div className="login-spotlight__title">{activeFeature.title}</div>
            <div className="login-spotlight__desc">{activeFeature.desc}</div>
          </div>
        </div>

        {/* Dot indicators */}
        <div className="login-dots">
          {ALL_FEATURES.map((f, i) => (
            <button
              key={i}
              className={`login-dot ${i === activeIdx ? 'active' : ''} ${f.mod === 'Inventory' ? 'purple' : ''}`}
              onClick={() => goTo(i)}
              aria-label={f.title}
            />
          ))}
        </div>

        {/* Feature grid — all 12 */}
        <div className="login-feature-grid">
          {ALL_FEATURES.map((f, i) => (
            <button
              key={i}
              className={`login-feat-chip ${i === activeIdx ? 'active' : ''}`}
              style={{ '--chip-color': f.color } as React.CSSProperties}
              onClick={() => goTo(i)}
            >
              <span>{f.icon}</span>
              <span>{f.title}</span>
            </button>
          ))}
        </div>

        {/* Stats row */}
        <div className="login-stats-row">
          {STATS.map((s, i) => (
            <div key={i} className="login-stat-item">
              <span className="login-stat-icon">{s.icon}</span>
              <span className="login-stat-val">{s.value}</span>
              <span className="login-stat-lbl">{s.label}</span>
            </div>
          ))}
        </div>
      </div>

      {/* ──────────────── RIGHT PANEL ──────────────── */}
      <div className="login-right">
        <div className="login-card">

          {/* Card header */}
          <div className="login-card__header">
            <div className="login-card__icon">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M15 3h4a2 2 0 012 2v14a2 2 0 01-2 2h-4"/>
                <polyline points="10 17 15 12 10 7"/>
                <line x1="15" y1="12" x2="3" y2="12"/>
              </svg>
            </div>
            <div>
              <h2 className="login-card__title">Welcome back</h2>
              <p className="login-card__sub">Sign in to your workspace</p>
            </div>
          </div>

          {/* Error */}
          {error && (
            <div className="login-error" role="alert">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" style={{ flexShrink: 0 }}>
                <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
              </svg>
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="login-form" noValidate>
            {/* Username */}
            <div className={`login-field ${focusedField === 'un' ? 'focused' : ''}`}>
              <label htmlFor="lg-un">Username</label>
              <div className="login-field__wrap">
                <svg className="login-field__icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/>
                </svg>
                <input
                  id="lg-un"
                  type="text"
                  className="login-field__input"
                  value={username}
                  onChange={e => { setUsername(e.target.value); setError(null) }}
                  onFocus={() => setFocusedField('un')}
                  onBlur={() => setFocusedField(null)}
                  autoComplete="username"
                  placeholder="Enter your username"
                  spellCheck={false}
                  autoFocus
                />
              </div>
            </div>

            {/* Password */}
            <div className={`login-field ${focusedField === 'pw' ? 'focused' : ''}`}>
              <label htmlFor="lg-pw">Password</label>
              <div className="login-field__wrap">
                <svg className="login-field__icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0110 0v4"/>
                </svg>
                <input
                  id="lg-pw"
                  type={showPassword ? 'text' : 'password'}
                  className="login-field__input"
                  value={password}
                  onChange={e => { setPassword(e.target.value); setError(null) }}
                  onFocus={() => setFocusedField('pw')}
                  onBlur={() => setFocusedField(null)}
                  autoComplete="current-password"
                  placeholder="Enter your password"
                />
                <button
                  type="button"
                  className="login-field__toggle"
                  onClick={() => setShowPassword(v => !v)}
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                  tabIndex={-1}
                >
                  {showPassword ? (
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                      <path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19"/><line x1="1" y1="1" x2="23" y2="23"/>
                    </svg>
                  ) : (
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>
                    </svg>
                  )}
                </button>
              </div>
            </div>

            {/* Submit */}
            <button type="submit" className={`login-submit ${loading ? 'loading' : ''}`} disabled={loading}>
              {loading ? (
                <><span className="login-spinner" />Signing in…</>
              ) : (
                <>
                  Sign In to Dashboard
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                    <line x1="5" y1="12" x2="19" y2="12"/><polyline points="12 5 19 12 12 19"/>
                  </svg>
                </>
              )}
            </button>
          </form>

          {/* Access modules hint */}
          <div className="login-access-hint">
            <div className="login-access-hint__label">After login you'll access</div>
            <div className="login-access-hint__chips">
              {MODULES.map(m => (
                <span key={m.category} className="login-access-chip" style={{ '--chip-c': m.color } as React.CSSProperties}>
                  {m.icon} {m.category}
                </span>
              ))}
            </div>
          </div>

          {/* Footer */}
          <div className="login-card__footer">
            <div className="login-secure-badge">
              <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
              </svg>
              JWT · AES-256 · Secure Session
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
