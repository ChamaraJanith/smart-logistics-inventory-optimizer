import React from 'react'
import { AreaChart, Area, XAxis, YAxis, Tooltip, ResponsiveContainer, BarChart, Bar, CartesianGrid } from 'recharts'

export function DeliveryTrendsChart({ data }: { data: number[] }) {
  // convert numeric array to objects with index label
  const chartData = (data || []).map((v, i) => ({ name: `W${i+1}`, value: v }))
  return (
    <div style={{ width: '100%', height: 160 }}>
      <ResponsiveContainer>
        <AreaChart data={chartData} margin={{ top: 12, right: 12, left: 0, bottom: 6 }}>
          <defs>
            <linearGradient id="colorUv" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor="#8b5cf6" stopOpacity={0.5}/>
              <stop offset="95%" stopColor="#3b82f6" stopOpacity={0}/>
            </linearGradient>
          </defs>
          <XAxis dataKey="name" tick={{ fill: '#89a0b8', fontSize: 12 }} axisLine={false} tickLine={false} dy={8} />
          <YAxis hide />
          <Tooltip 
            wrapperStyle={{ outline: 'none' }}
            contentStyle={{ background: 'rgba(11,27,43,0.9)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '12px', color: '#e6f0fb', backdropFilter: 'blur(10px)', boxShadow: '0 8px 32px rgba(0,0,0,0.4)', padding: '12px 16px' }} 
            itemStyle={{ color: '#60a5fa', fontWeight: 600 }}
          />
          <Area type="monotone" dataKey="value" stroke="url(#colorUv)" strokeWidth={3} fillOpacity={1} fill="url(#colorUv)" />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  )
}

export function DriverPerformanceChart({ data }: { data: { name: string; value: number }[] }) {
  const chartData = (data || []).map((d) => ({ name: d.name, value: d.value }))
  return (
    <div style={{ width: '100%', height: 200 }}>
      <ResponsiveContainer>
        <BarChart data={chartData} margin={{ top: 10, right: 6, left: 0, bottom: 6 }}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="rgba(255,255,255,0.06)" />
          <XAxis dataKey="name" tick={{ fill: '#89a0b8', fontSize: 12 }} axisLine={false} tickLine={false} dy={8} />
          <YAxis hide />
          <Tooltip 
            cursor={{ fill: 'rgba(255,255,255,0.02)' }}
            wrapperStyle={{ outline: 'none' }}
            contentStyle={{ background: 'rgba(11,27,43,0.9)', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '12px', color: '#e6f0fb', backdropFilter: 'blur(10px)', boxShadow: '0 8px 32px rgba(0,0,0,0.4)', padding: '12px 16px' }} 
            itemStyle={{ color: '#a78bfa', fontWeight: 600 }}
          />
          <Bar dataKey="value" fill="url(#colorUv)" radius={[6,6,6,6]} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}

export default DeliveryTrendsChart
