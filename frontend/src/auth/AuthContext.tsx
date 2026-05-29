import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'

interface AuthState {
  token: string | null
  username: string | null
}

interface AuthContextValue extends AuthState {
  login: (username: string, password: string) => Promise<{ token: string; username: string }>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

const STORAGE_KEY = 'logioptima-auth'

function loadStoredAuth(): AuthState {
  try {
    const raw = window.localStorage.getItem(STORAGE_KEY)
    if (!raw) return { token: null, username: null }
    return JSON.parse(raw) as AuthState
  } catch {
    return { token: null, username: null }
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState<AuthState>(() => loadStoredAuth())

  useEffect(() => {
    if (state.token && state.username) {
      window.localStorage.setItem(STORAGE_KEY, JSON.stringify(state))
    } else {
      window.localStorage.removeItem(STORAGE_KEY)
    }
  }, [state])

  const login = async (username: string, password: string) => {
    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    })

    if (!response.ok) {
      const body = await response.text().catch(() => '')
      throw new Error(body || 'Unable to sign in')
    }

    const data = await response.json()
    const newState = { token: data.token, username: data.username }
    
    // Write to localStorage immediately so it's available synchronously
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(newState))
    
    // Then update state
    setState(newState)
    return newState
  }

  const logout = () => {
    setState({ token: null, username: null })
  }

  const value = useMemo(() => ({
    token: state.token,
    username: state.username,
    login,
    logout,
  }), [state])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}

export async function authFetch(input: RequestInfo, init?: RequestInit) {
  const raw = window.localStorage.getItem(STORAGE_KEY)
  const stored = raw ? (JSON.parse(raw) as AuthState) : null
  const headers = new Headers(init?.headers || {})
  if (stored?.token) {
    headers.set('Authorization', `Bearer ${stored.token}`)
  }

  const response = await fetch(input, { ...init, headers })
  return response
}
