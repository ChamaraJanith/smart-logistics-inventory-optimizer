import { useEffect, useRef, useState, useCallback } from 'react'
import { Link } from 'react-router-dom'
import '../styles/landing.css'

/* ══════════════════════════════════════════════
   HOOKS
══════════════════════════════════════════════ */
function useScrollY() {
  const [y, setY] = useState(0)
  useEffect(() => {
    const h = () => setY(window.scrollY)
    window.addEventListener('scroll', h, { passive: true })
    return () => window.removeEventListener('scroll', h)
  }, [])
  return y
}

function useInView(threshold = 0.12) {
  const ref = useRef<HTMLDivElement>(null)
  const [inView, setInView] = useState(false)
  useEffect(() => {
    const el = ref.current
    if (!el) return
    const obs = new IntersectionObserver(
      ([e]) => { if (e.isIntersecting) setInView(true) },
      { threshold }
    )
    obs.observe(el)
    return () => obs.disconnect()
  }, [threshold])
  return { ref, inView }
}
function useCounter(target: number, duration = 2200, active = false) {
  const [val, setVal] = useState(0)
  useEffect(() => {
    if (!active) return
    let start: number | null = null
    const tick = (ts: number) => {
      if (!start) start = ts
      const p = Math.min((ts - start) / duration, 1)
      setVal(Math.floor(p * target))
      if (p < 1) requestAnimationFrame(tick)
    }
    requestAnimationFrame(tick)
  }, [active, target, duration])
  return val
}

/* ══════════════════════════════════════════════
   SMALL COMPONENTS
══════════════════════════════════════════════ */

interface StatProps { value: number; suffix: string; label: string; icon: string }
function StatItem({ value, suffix, label, icon }: Readonly<StatProps>) {
  const { ref, inView } = useInView()
  const count = useCounter(value, 2200, inView)
  return (
    <div ref={ref} className={`stat-item ${inView ? 'visible' : ''}`}>
      <span className="stat-icon">{icon}</span>
      <strong>{count}{suffix}</strong>
      <span>{label}</span>
    </div>
  )
}

interface FeatureProps { img: string; tag: string; title: string; desc: string; bullets: string[] }
function FeatureBlock({ img, tag, title, desc, bullets }: Readonly<FeatureProps>) {
  const { ref, inView } = useInView()
  return (
    <div ref={ref} className={`feature-block ${inView ? 'visible' : ''}`}>
      <div className="fb-image">
        <img src={img} alt={title} loading="lazy" />
        <div className="fb-image-overlay" />
      </div>
      <div className="fb-body">
        <span className="fb-tag">{tag}</span>
        <h3>{title}</h3>
        <p>{desc}</p>
        <ul>
          {bullets.map((b) => (
            <li key={b}>
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden="true">
                <circle cx="8" cy="8" r="7" stroke="currentColor" strokeWidth="1.2" opacity=".4"/>
                <path d="M5 8l2 2 4-4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
              {b}
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}

interface TestimonialProps { quote: string; name: string; role: string; avatar: string }
function TestimonialCard({ quote, name, role, avatar }: Readonly<TestimonialProps>) {
  const { ref, inView } = useInView()
  return (
    <div ref={ref} className={`testimonial-card ${inView ? 'visible' : ''}`}>
      <div className="tc-stars">{'★'.repeat(5)}</div>
      <p>"{quote}"</p>
      <div className="tc-footer">
        <img src={avatar} alt={name} loading="lazy" />
        <div>
          <strong>{name}</strong>
          <span>{role}</span>
        </div>
      </div>
    </div>
  )
}

/* ══════════════════════════════════════════════
   MAIN PAGE
══════════════════════════════════════════════ */
export default function LandingPage() {
  const scrollY = useScrollY()
  const [menuOpen, setMenuOpen] = useState(false)
  const closeMenu = useCallback(() => setMenuOpen(false), [])
  const [newsletterEmail, setNewsletterEmail] = useState('')
  const [newsletterStatus, setNewsletterStatus] = useState<'idle'|'error'|'sent'>('idle')
  const emailRef = useRef<HTMLInputElement | null>(null)

  /* ── Data ── */
  const features: FeatureProps[] = [
    {
      img: 'https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?w=800&q=80',
      tag: 'Route Intelligence',
      title: 'Geospatial Route Optimization',
      desc: 'Cut delivery costs and transit times with algorithms that factor in vehicle capacity, traffic windows, and multi-stop sequencing.',
      bullets: ['Dynamic multi-stop sequencing', 'Vehicle capacity & weight constraints', 'Predicted delay risk scoring', 'Cost & distance optimization'],
    },
    {
      img: 'https://images.unsplash.com/photo-1553413077-190dd305871c?w=800&q=80',
      tag: 'Inventory Control',
      title: 'Real-Time Stock Management',
      desc: 'Full warehouse visibility with live stock levels, reserved quantities, and automated reorder alerts before you hit critical thresholds.',
      bullets: ['Live available vs reserved stock', 'Automated reorder alerts', 'Multi-warehouse tracking', 'Stock transaction history'],
    },
    {
      img: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800&q=80',
      tag: 'AI Forecasting',
      title: 'Demand Forecasting & Analytics',
      desc: 'Predictive models analyze seasonal patterns and historical demand to generate confidence-scored forecasts and anomaly detection.',
      bullets: ['7-day demand predictions', 'Seasonal pattern recognition', 'Anomaly detection & logging', 'Driver & vehicle performance KPIs'],
    },
    {
      img: 'https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=800&q=80',
      tag: 'Fleet Management',
      title: 'Vehicles, Drivers & Deliveries',
      desc: 'Assign drivers to vehicles, manage delivery priorities, and track every shipment from warehouse to doorstep in one place.',
      bullets: ['Driver license & status tracking', 'Priority-based delivery queuing', 'Time-window delivery scheduling', 'Real-time delivery status updates'],
    },
  ]

  const testimonials: TestimonialProps[] = [
    {
      quote: 'We reduced fuel costs by 38% in the first quarter after switching. The route optimizer is genuinely impressive.',
      name: 'Sarah Mitchell',
      role: 'Head of Logistics, FreightCo',
      avatar: 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=80&q=80',
    },
    {
      quote: 'Stock visibility across 12 warehouses used to take hours. Now it\'s instant. The reorder alerts alone saved us thousands.',
      name: 'James Okafor',
      role: 'Supply Chain Director, NovaDist',
      avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=80&q=80',
    },
    {
      quote: 'The demand forecasting is surprisingly accurate. We\'ve cut overstock by 45% and stockouts are almost zero now.',
      name: 'Priya Nair',
      role: 'Operations Manager, SwiftWare',
      avatar: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=80&q=80',
    },
  ]

  const steps = [
    { n: '01', title: 'Connect Your Network', desc: 'Register warehouses, vehicles, and drivers. The system maps your entire logistics infrastructure instantly.', img: 'https://images.unsplash.com/photo-1578575437130-527eed3abbec?w=600&q=80' },
    { n: '02', title: 'Plan Optimized Routes', desc: 'Input deliveries and let the engine generate cost-aware, capacity-respecting routes with ETA predictions.', img: 'https://images.unsplash.com/photo-1524661135-423995f22d0b?w=600&q=80' },
    { n: '03', title: 'Monitor Everything Live', desc: 'Track deliveries, stock levels, and vehicles on a unified dashboard with real-time alerts and anomaly flags.', img: 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&q=80' },
    { n: '04', title: 'Forecast & Reorder', desc: 'AI-driven demand forecasts trigger smart reorder suggestions before stock reaches critical levels.', img: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600&q=80' },
  ]

  const partners = [
    { name: 'FreightCo', initials: 'F', color: '#203040' },
    { name: 'NovaDist', initials: 'N', color: '#2a3340' },
    { name: 'SwiftWare', initials: 'S', color: '#16202b' },
    { name: 'CargoLink', initials: 'C', color: '#1b2430' },
    { name: 'PrimeLogi', initials: 'P', color: '#17202a' },
    { name: 'SupplyEdge', initials: 'SE', color: '#24303a' },
  ]

  return (
    <div className="lp">
      <a className="skip-link" href="#top">Skip to content</a>

      {/* ══ NAV ══ */}
      <header className={`lp-nav ${scrollY > 50 ? 'lp-nav--solid' : ''}`}>
        <div className="lp-nav__inner">
          <a href="#top" className="lp-nav__logo" onClick={closeMenu}>
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none" aria-hidden="true">
              <rect width="32" height="32" rx="8" fill="url(#lg1)"/>
              <path d="M8 20l8-12 8 12" stroke="#fff" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round"/>
              <path d="M12 20h8" stroke="#fff" strokeWidth="2.2" strokeLinecap="round"/>
              <defs>
                <linearGradient id="lg1" x1="0" y1="0" x2="32" y2="32" gradientUnits="userSpaceOnUse">
                  <stop stopColor="#3b82f6"/>
                  <stop offset="1" stopColor="#8b5cf6"/>
                </linearGradient>
              </defs>
            </svg>
            <span>LogiOptima</span>
          </a>

          <nav className={`lp-nav__links ${menuOpen ? 'open' : ''}`} aria-label="Main navigation">
            <a href="#features" onClick={closeMenu}>Features</a>
            <a href="#how-it-works" onClick={closeMenu}>How It Works</a>
            <a href="#testimonials" onClick={closeMenu}>Testimonials</a>
            <a href="#tech" onClick={closeMenu}>Tech Stack</a>
          </nav>

          <div className="lp-nav__actions">
            <Link to="/app/dashboard" className="lp-btn lp-btn--ghost lp-btn--sm">Sign In</Link>
            <Link to="/app/dashboard" className="lp-btn lp-btn--primary lp-btn--sm">Get Started</Link>
          </div>

          <button
            className={`lp-hamburger ${menuOpen ? 'open' : ''}`}
            onClick={() => setMenuOpen(v => !v)}
            aria-label="Toggle navigation"
            aria-expanded={menuOpen}
          >
            <span /><span /><span />
          </button>
        </div>
      </header>

      {/* ══ HERO ══ */}
      <section className="lp-hero" id="top">
        {/* Full-bleed background image */}
        <div className="lp-hero__bg">
          <img
            src="https://images.unsplash.com/photo-1494412574643-ff11b0a5c1c3?w=1800&q=85"
            alt=""
            aria-hidden="true"
          />
          <div className="lp-hero__bg-overlay" />
        </div>

        {/* Floating accent shapes */}
        <div className="lp-hero__shape lp-hero__shape--1" />
        <div className="lp-hero__shape lp-hero__shape--2" />

        {/* ── Inner grid: content + visual ── */}
        <div className="lp-hero__inner">

        <div className="lp-hero__content">
          <div className="lp-hero__badge">
            <span className="lp-badge-dot" />{' '}
            Smart Logistics &amp; Inventory Intelligence
          </div>

          <h1>
            Optimize Every Route.<br />
            <span className="lp-gradient-text">Control Every Stock.</span>
          </h1>

          <p className="lp-hero__sub">
            A unified platform combining geospatial route optimization, real-time inventory
            control, and AI-powered demand forecasting — purpose-built for modern logistics.
          </p>

          <div className="lp-hero__cta">
            <Link to="/app/dashboard" className="lp-btn lp-btn--primary lp-btn--lg">
              Explore the Platform
              <svg width="18" height="18" viewBox="0 0 18 18" fill="none" aria-hidden="true">
                <path d="M4 9h10M10 5l4 4-4 4" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </Link>
            <a href="#how-it-works" className="lp-btn lp-btn--outline lp-btn--lg">
              See How It Works
            </a>
          </div>

          {/* Floating KPI cards */}
          <div className="lp-hero__kpis">
            <div className="lp-kpi lp-kpi--1">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
                <path d="M3 10l4 4 10-8" stroke="#10b981" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
              <div>
                <strong>94.3%</strong>
                <span>On-Time Delivery</span>
              </div>
            </div>
            <div className="lp-kpi lp-kpi--2">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
                <path d="M10 3v14M3 10h14" stroke="#3b82f6" strokeWidth="2" strokeLinecap="round"/>
              </svg>
              <div>
                <strong>2,847</strong>
                <span>Deliveries Today</span>
              </div>
            </div>
            <div className="lp-kpi lp-kpi--3">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
                <circle cx="10" cy="10" r="7" stroke="#f59e0b" strokeWidth="2"/>
                <path d="M10 7v3l2 2" stroke="#f59e0b" strokeWidth="1.8" strokeLinecap="round"/>
              </svg>
              <div>
                <strong>18 Routes</strong>
                <span>Active Now</span>
              </div>
            </div>
          </div>
        </div>

        {/* Hero image panel */}
        <div className="lp-hero__visual">
          <div className="lp-hero__visual-frame">
            <img
              src="https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?w=900&q=85"
              alt="Logistics warehouse operations"
            />
            <div className="lp-hero__visual-badge">
              <span className="lp-live-dot" />{' '}
              Live Operations
            </div>
          </div>
        </div>

        </div>{/* end lp-hero__inner */}

        <a href="#stats" className="lp-scroll-cue" aria-label="Scroll down">
          <span>Scroll</span>
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
            <path d="M10 4v12M5 11l5 5 5-5" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </a>
      </section>

      {/* ══ PARTNERS — redesigned as cards to avoid footer-like appearance ══ */}
      <section className="lp-trust" aria-label="Trusted by">
        <div className="lp-container">
          <div className="lp-trust__header">
            <p className="lp-trust__label">Trusted by logistics teams worldwide</p>
            <p className="lp-trust__sub">Operational teams rely on LogiOptima for day-to-day route and stock decisions.</p>
          </div>

          <div className="lp-trust__cards" role="list">
            {partners.map((p) => (
              <div key={p.name} role="listitem" className="lp-trust__card" title={p.name}>
                <div className="lp-trust__mark" aria-hidden style={{ background: p.color }}>{p.initials}</div>
                <div className="lp-trust__name">{p.name}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ══ STATS ══ */}
      <section className="lp-stats" id="stats">
        <div className="lp-container">
          <div className="lp-stats__grid">
            <StatItem value={98}  suffix="%" label="Delivery Accuracy"   icon="🎯" />
            <StatItem value={40}  suffix="%" label="Fuel Cost Reduction"  icon="⛽" />
            <StatItem value={13}  suffix="+"  label="Integrated Modules"  icon="🔧" />
            <StatItem value={99}  suffix="%" label="Stock Visibility"     icon="📦" />
          </div>
        </div>
      </section>

      {/* ══ FEATURES ══ */}
      <section className="lp-features" id="features">
        <div className="lp-container">
          <div className="lp-section-header">
            <span className="lp-tag">Core Capabilities</span>
            <h2>Everything your operation needs,<br />in one platform</h2>
            <p>From warehouse to last-mile delivery — route intelligence, inventory control, and predictive analytics unified.</p>
          </div>
          <div className="lp-features__list">
            {features.map((f) => (
              <FeatureBlock key={f.title} img={f.img} tag={f.tag} title={f.title} desc={f.desc} bullets={f.bullets} />
            ))}
          </div>
        </div>
      </section>

      {/* ══ HOW IT WORKS ══ */}
      <section className="lp-how" id="how-it-works">
        <div className="lp-container">
          <div className="lp-section-header">
            <span className="lp-tag">Process</span>
            <h2>Up and running in four steps</h2>
            <p>A streamlined setup that gets your entire logistics network optimized fast.</p>
          </div>
          <div className="lp-steps">
            {steps.map((s) => {
              return (
                <StepCard key={s.n} n={s.n} title={s.title} desc={s.desc} img={s.img} />
              )
            })}
          </div>
        </div>
      </section>

      {/* ══ SHOWCASE IMAGE STRIP ══ */}
      <section className="lp-showcase">
        <div className="lp-showcase__track">
          {[
            'https://images.unsplash.com/photo-1553413077-190dd305871c?w=600&q=80',
            'https://images.unsplash.com/photo-1566576912321-d58ddd7a6088?w=600&q=80',
            'https://images.unsplash.com/photo-1494412574643-ff11b0a5c1c3?w=600&q=80',
            'https://images.unsplash.com/photo-1578575437130-527eed3abbec?w=600&q=80',
            'https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=600&q=80',
            'https://images.unsplash.com/photo-1524661135-423995f22d0b?w=600&q=80',
            'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&q=80',
            'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=600&q=80',
          ].map((src, i) => (
            <div key={src} className="lp-showcase__item" style={{ animationDelay: `${i * -3}s` }}>
              <img src={src} alt="" loading="lazy" aria-hidden="true" />
            </div>
          ))}
        </div>
      </section>

      {/* ══ TESTIMONIALS ══ */}
      <section className="lp-testimonials" id="testimonials">
        <div className="lp-container">
          <div className="lp-section-header">
            <span className="lp-tag">Testimonials</span>
            <h2>Trusted by logistics professionals</h2>
            <p>Real results from teams that transformed their operations with LogiOptima.</p>
          </div>
          <div className="lp-testimonials__grid">
            {testimonials.map((t) => (
              <TestimonialCard key={t.name} quote={t.quote} name={t.name} role={t.role} avatar={t.avatar} />
            ))}
          </div>
        </div>
      </section>

      {/* ══ TECH STACK ══ */}
      <section className="lp-tech" id="tech">
        <div className="lp-container">
          <div className="lp-section-header">
            <span className="lp-tag">Technology</span>
            <h2>Built on enterprise-grade foundations</h2>
            <p>Every layer of the stack chosen for reliability, performance, and scalability.</p>
          </div>
          <div className="lp-tech__grid">
            {[
              { name: 'Spring Boot 3', role: 'Backend REST API', color: '#6db33f', icon: '🍃' },
              { name: 'PostgreSQL 16', role: 'Primary Database', color: '#336791', icon: '🐘' },
              { name: 'PostGIS',       role: 'Geospatial Engine', color: '#4169e1', icon: '🗺️' },
              { name: 'React 19',      role: 'Frontend UI',       color: '#61dafb', icon: '⚛️' },
              { name: 'TypeScript',    role: 'Type Safety',        color: '#3178c6', icon: '🔷' },
              { name: 'Vite 8',        role: 'Build Tooling',      color: '#646cff', icon: '⚡' },
              { name: 'Java 21',       role: 'JVM Runtime',        color: '#f89820', icon: '☕' },
              { name: 'Docker',        role: 'Containerization',   color: '#2496ed', icon: '🐳' },
            ].map((t) => (
              <TechCard key={t.name} name={t.name} role={t.role} color={t.color} icon={t.icon} />
            ))}
          </div>
        </div>
      </section>

      {/* ══ CTA BANNER ══ */}
      <section className="lp-cta">
        <div className="lp-cta__bg">
          <img src="https://images.unsplash.com/photo-1566576912321-d58ddd7a6088?w=1600&q=80" alt="" aria-hidden="true" />
          <div className="lp-cta__overlay" />
        </div>
        <div className="lp-container lp-cta__inner">
          <h2>Ready to transform your logistics?</h2>
          <p>Join the platform that turns complex supply chains into competitive advantages.</p>

          <form
            className="lp-newsletter"
            onSubmit={(e) => {
              e.preventDefault()
              const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
              if (!re.test(newsletterEmail)) {
                setNewsletterStatus('error')
                emailRef.current?.focus()
                return
              }
              // Frontend-only: mock a successful subscribe (no backend)
              setNewsletterStatus('sent')
              setTimeout(() => setNewsletterEmail(''), 1200)
            }}
          >
            <label htmlFor="lp-email" className="sr-only">Email address</label>
            <input
              id="lp-email"
              ref={emailRef}
              type="email"
              placeholder="Enter your work email"
              value={newsletterEmail}
              onChange={(e) => { setNewsletterEmail(e.target.value); setNewsletterStatus('idle') }}
              aria-invalid={newsletterStatus === 'error'}
            />
            <button type="submit" className="lp-btn lp-btn--primary lp-btn--lg">Get Early Access</button>
            <div role="status" aria-live="polite" className={`lp-newsletter__status ${newsletterStatus}`}> 
              {newsletterStatus === 'error' && 'Please enter a valid email.'}
              {newsletterStatus === 'sent' && 'Thanks — we\'ll be in touch!'}
            </div>
          </form>

          <div className="lp-cta__actions">
            <Link to="/app/dashboard" className="lp-btn lp-btn--white lp-btn--lg">
              Start Optimizing
              <svg width="18" height="18" viewBox="0 0 18 18" fill="none" aria-hidden="true">
                <path d="M4 9h10M10 5l4 4-4 4" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </Link>
            <a href="#how-it-works" className="lp-btn lp-btn--outline-white lp-btn--lg">Learn More</a>
          </div>
        </div>
      </section>

      {/* ══ FOOTER ══ */}
      <footer className="lp-footer">
        <div className="lp-container lp-footer__inner">
          <div className="lp-footer__brand">
            <svg width="28" height="28" viewBox="0 0 32 32" fill="none" aria-hidden="true">
              <rect width="32" height="32" rx="8" fill="url(#lg2)"/>
              <path d="M8 20l8-12 8 12" stroke="#fff" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round"/>
              <path d="M12 20h8" stroke="#fff" strokeWidth="2.2" strokeLinecap="round"/>
              <defs>
                <linearGradient id="lg2" x1="0" y1="0" x2="32" y2="32" gradientUnits="userSpaceOnUse">
                  <stop stopColor="#3b82f6"/>
                  <stop offset="1" stopColor="#8b5cf6"/>
                </linearGradient>
              </defs>
            </svg>
            <span>LogiOptima</span>
          </div>
          <p className="lp-footer__copy">
            Smart Logistics &amp; Inventory Optimizer — React 19 + Spring Boot 3 + PostgreSQL
          </p>
          <nav className="lp-footer__links" aria-label="Footer navigation">
            <a href="#features">Features</a>
            <a href="#how-it-works">Process</a>
            <a href="#testimonials">Testimonials</a>
            <a href="#tech">Tech Stack</a>
          </nav>
        </div>
      </footer>

    </div>
  )
}

/* ══════════════════════════════════════════════
   STEP CARD — separate component to allow hooks
══════════════════════════════════════════════ */
interface StepProps { n: string; title: string; desc: string; img: string }
function StepCard({ n, title, desc, img }: Readonly<StepProps>) {
  const { ref, inView } = useInView()
  return (
    <div ref={ref} className={`lp-step ${inView ? 'visible' : ''}`}>
      <div className="lp-step__img">
        <img src={img} alt={title} loading="lazy" />
        <div className="lp-step__num">{n}</div>
      </div>
      <div className="lp-step__body">
        <h3>{title}</h3>
        <p>{desc}</p>
      </div>
    </div>
  )
}

/* ══════════════════════════════════════════════
   TECH CARD — separate component to allow hooks
══════════════════════════════════════════════ */
interface TechCardProps { name: string; role: string; color: string; icon: string }
function TechCard({ name, role, color, icon }: Readonly<TechCardProps>) {
  const { ref, inView } = useInView()
  return (
    <div ref={ref} className={`lp-tech__card ${inView ? 'visible' : ''}`}>
      <span className="lp-tech__icon">{icon}</span>
      <div className="lp-tech__dot" style={{ background: color }} />
      <strong>{name}</strong>
      <span>{role}</span>
    </div>
  )
}
