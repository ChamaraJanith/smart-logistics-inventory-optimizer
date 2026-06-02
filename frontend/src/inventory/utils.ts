/** Shared utilities for the inventory module */

/** Format ISO datetime string to human-readable */
export function formatDateTime(iso: string | null | undefined): string {
  if (!iso) return '—'
  try {
    return new Date(iso).toLocaleString('en-US', {
      month: 'short', day: 'numeric', year: 'numeric',
      hour: '2-digit', minute: '2-digit',
    })
  } catch {
    return iso
  }
}

/** Format ISO date (no time) */
export function formatDate(iso: string | null | undefined): string {
  if (!iso) return '—'
  try {
    return new Date(iso).toLocaleDateString('en-US', {
      month: 'short', day: 'numeric', year: 'numeric',
    })
  } catch {
    return iso
  }
}

/** Format number to max 2 decimal places, strip trailing zeros */
export function fmtNum(value: number | null | undefined): string {
  if (value === null || value === undefined) return '—'
  return Number(value.toFixed(2)).toString()
}

/** Map stock status → badge CSS class */
export function stockStatusClass(status: string): string {
  const map: Record<string, string> = {
    NORMAL: 'badge--normal',
    LOW: 'badge--low',
    CRITICAL: 'badge--critical',
    OUT_OF_STOCK: 'badge--out_of_stock',
  }
  return map[status?.toUpperCase()] ?? 'badge--normal'
}

/** Map severity → badge CSS class */
export function severityClass(sev: string): string {
  const map: Record<string, string> = {
    LOW: 'badge--sev-low',
    MEDIUM: 'badge--sev-medium',
    HIGH: 'badge--sev-high',
    CRITICAL: 'badge--sev-critical',
  }
  return map[sev?.toUpperCase()] ?? 'badge--sev-medium'
}

/** Map transaction type → badge CSS class */
export function txTypeClass(type: string): string {
  const map: Record<string, string> = {
    RESTOCK: 'badge--restock',
    DISPATCH: 'badge--dispatch',
    ADJUSTMENT: 'badge--adjustment',
    RESERVE: 'badge--reserve',
    RELEASE_RESERVE: 'badge--release_reserve',
  }
  return map[type?.toUpperCase()] ?? 'badge--adjustment'
}

/** Map warehouse/item status → badge CSS class */
export function statusClass(status: string): string {
  const s = status?.toUpperCase()
  if (s === 'ACTIVE') return 'badge--active'
  if (s === 'INACTIVE') return 'badge--inactive'
  if (s === 'OPEN') return 'badge--open'
  if (s === 'RESOLVED') return 'badge--resolved'
  return 'badge--active'
}
