import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import SmartCityDashboard from './SmartCityDashboard.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <SmartCityDashboard />
  </StrictMode>,
)
