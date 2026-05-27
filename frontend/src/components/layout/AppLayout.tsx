import { useState, useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import Sidebar from './Sidebar'
import Topbar from './Topbar'
import '../../styles/landing.css'

export default function AppLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(true)
  const [isMobile, setIsMobile] = useState(false)

  useEffect(() => {
    const handleResize = () => {
      const mobile = window.innerWidth <= 768
      setIsMobile(mobile)
      if (!mobile) setSidebarOpen(true)
    }
    
    handleResize()
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [])

  return (
    <div className="app-layout" style={{ display: 'flex', minHeight: '100vh' }}>
      <Sidebar open={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      <div className="app-main" style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
        <Topbar onMenuClick={() => setSidebarOpen(!sidebarOpen)} />
        <main className="app-content" style={{ padding: 24 }}>
          <Outlet />
        </main>
      </div>
      {sidebarOpen && isMobile && <div className="app-sidebar-overlay" onClick={() => setSidebarOpen(false)} />}
    </div>
  )
}
