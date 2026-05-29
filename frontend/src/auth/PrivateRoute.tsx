import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from './AuthContext'

const STORAGE_KEY = 'logioptima-auth'

export default function PrivateRoute() {
  const auth = useAuth()
  
  // Check both context and localStorage as fallback for freshly logged-in users
  // (context state may not have updated yet due to React async state batching)
  const hasToken = auth.token || (() => {
    try {
      const raw = window.localStorage.getItem(STORAGE_KEY)
      if (!raw) return false
      const stored = JSON.parse(raw)
      return !!stored.token
    } catch {
      return false
    }
  })()
  
  return hasToken ? <Outlet /> : <Navigate to="/login" replace />
}
