import React, { useState, useEffect } from 'react';
import './styles.css';

const API_BASE = 'http://localhost:8080/api';

// Icons as simple SVG components
const Icon = ({ name, size = 24, color = "currentColor" }) => {
  const icons = {
    activity: <path d="M22 12h-4l-3 9L9 3l-3 9H2" />,
    users: <><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" /><circle cx="9" cy="7" r="4" /><path d="M23 21v-2a4 4 0 0 0-3-3.87" /><path d="M16 3.13a4 4 0 0 1 0 7.75" /></>,
    bus: <><rect width="16" height="20" x="7" y="2" rx="2" /><path d="M7 10h10M7 14h10M7 18h4" /></>,
    wind: <><path d="M9.59 4.59A2 2 0 1 1 11 8H2m10.59 11.41A2 2 0 1 0 14 16H2m15.73-8.27A2.5 2.5 0 1 1 19.5 12H2" /></>,
    alert: <><path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z" /><path d="M12 9v4M12 17h.01" /></>,
    mapPin: <><path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z" /><circle cx="12" cy="10" r="3" /></>,
    trending: <><polyline points="23 6 13.5 15.5 8.5 10.5 1 18" /><polyline points="17 6 23 6 23 12" /></>,
    clock: <><circle cx="12" cy="12" r="10" /><polyline points="12 6 12 12 16 14" /></>,
    navigation: <><polygon points="3 11 22 2 13 21 11 13 3 11" /></>,
    x: <><path d="M18 6 6 18M6 6l12 12" /></>,
    plus: <><path d="M5 12h14M12 5v14" /></>,
    refresh: <><path d="M21 2v6h-6M3 12a9 9 0 0 1 15-6.7L21 8M3 22v-6h6M21 12a9 9 0 0 1-15 6.7L3 16" /></>,
    messageCircle: <><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" /></>,
    send: <><line x1="22" x2="11" y1="2" y2="13" /><polygon points="22 2 15 22 11 13 2 9 22 2" /></>,
  };

  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke={color}
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      {icons[name]}
    </svg>
  );
};

// Main App Component
const SmartCityDashboard = () => {
  const [activeTab, setActiveTab] = useState('overview');
  const [transportLines, setTransportLines] = useState([]);
  const [airQuality, setAirQuality] = useState([]);
  const [incidents, setIncidents] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(null);
  const [showChat, setShowChat] = useState(false);
  const [stats, setStats] = useState({
    totalUsers: 0,
    activeIncidents: 0,
    transportLines: 0,
    avgAqi: 0
  });

  useEffect(() => {
    fetchData();
  }, [activeTab]);

  const fetchData = async () => {
    setLoading(true);
    try {
      if (activeTab === 'overview' || activeTab === 'users') {
        await fetchUsers();
      }
      if (activeTab === 'overview' || activeTab === 'transport') {
        await fetchTransportLines();
      }
      if (activeTab === 'overview' || activeTab === 'air') {
        await fetchAirQuality();
      }
      if (activeTab === 'overview' || activeTab === 'incidents') {
        await fetchIncidents();
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchUsers = async () => {
    const query = `{ allUsers { id username email createdAt } }`;
    const response = await fetch(`${API_BASE}/graphql`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ query })
    });
    const data = await response.json();
    setUsers(data.data?.allUsers || []);
    setStats(prev => ({ ...prev, totalUsers: data.data?.allUsers?.length || 0 }));
  };

  const fetchTransportLines = async () => {
    const query = `{ 
      allTransportLines { 
        id name mode route 
        schedules { id stop departureTime arrivalTime dayOfWeek } 
      } 
    }`;
    const response = await fetch(`${API_BASE}/graphql`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ query })
    });
    const data = await response.json();
    setTransportLines(data.data?.allTransportLines || []);
    setStats(prev => ({ ...prev, transportLines: data.data?.allTransportLines?.length || 0 }));
  };

  const fetchAirQuality = async () => {
    const query = `{ latestAirQuality(limit: 10) { id zone aqi pm25 no2 co2 measuredAt } }`;
    const response = await fetch(`${API_BASE}/graphql`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ query })
    });
    const data = await response.json();
    const aqData = data.data?.latestAirQuality || [];
    setAirQuality(aqData);
    const avgAqi = aqData.length > 0 
      ? Math.round(aqData.reduce((sum, aq) => sum + (aq.aqi || 0), 0) / aqData.length)
      : 0;
    setStats(prev => ({ ...prev, avgAqi }));
  };

const fetchIncidents = async () => {
  try {
    const response = await fetch(`${API_BASE}/integrator/api/incidents`, {
      method: 'GET',
      credentials: 'include', // if you use cookies/auth, remove if not needed
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const data = await response.json();
    setIncidents(data || []);

    const active = data?.filter(i => i.status === 'new' || i.status === 'in-progress').length || 0;
    setStats(prev => ({ ...prev, activeIncidents: active }));

  } catch (err) {
    console.error('Error fetching incidents:', err);
  }
};


  const getAqiLevel = (aqi) => {
    if (aqi <= 50) return { label: 'Good', class: 'aqi-good' };
    if (aqi <= 100) return { label: 'Moderate', class: 'aqi-moderate' };
    return { label: 'Unhealthy', class: 'aqi-unhealthy' };
  };

  const getStatusBadge = (status) => {
    const badges = {
      new: 'danger',
      'in-progress': 'warning',
      resolved: 'success'
    };
    return badges[status] || 'gray';
  };

  return (
    <div className="app">
      {/* Header */}
      <header className="header">
        <div className="header-content">
          <div>
            <div className="header-title">
              <Icon name="activity" size={32} />
              Smart City Platform
            </div>
            <div className="header-subtitle">
              Real-time monitoring and management system
            </div>
          </div>
          <button className="btn btn-secondary" onClick={fetchData}>
            <Icon name="refresh" size={16} />
            Refresh Data
          </button>
        </div>
      </header>

      {/* Navigation */}
      <nav className="nav">
        <div className="nav-content">
          <button
            className={`nav-button ${activeTab === 'overview' ? 'active' : ''}`}
            onClick={() => setActiveTab('overview')}
          >
            <Icon name="activity" size={18} />
            Overview
          </button>
          <button
            className={`nav-button ${activeTab === 'transport' ? 'active' : ''}`}
            onClick={() => setActiveTab('transport')}
          >
            <Icon name="bus" size={18} />
            Transport
          </button>
          <button
            className={`nav-button ${activeTab === 'air' ? 'active' : ''}`}
            onClick={() => setActiveTab('air')}
          >
            <Icon name="wind" size={18} />
            Air Quality
          </button>
          <button
            className={`nav-button ${activeTab === 'incidents' ? 'active' : ''}`}
            onClick={() => setActiveTab('incidents')}
          >
            <Icon name="alert" size={18} />
            Incidents
          </button>
          <button
            className={`nav-button ${activeTab === 'users' ? 'active' : ''}`}
            onClick={() => setActiveTab('users')}
          >
            <Icon name="users" size={18} />
            Users
          </button>
        </div>
      </nav>

      {/* Main Content */}
      <main className="main">
        {loading ? (
          <div className="loading">
            <div className="spinner"></div>
            <p>Loading data...</p>
          </div>
        ) : (
          <>
            {activeTab === 'overview' && <OverviewTab stats={stats} incidents={incidents} airQuality={airQuality} />}
            {activeTab === 'transport' && <TransportTab lines={transportLines} onRefresh={fetchTransportLines} />}
            {activeTab === 'air' && <AirQualityTab data={airQuality} getAqiLevel={getAqiLevel} />}
            {activeTab === 'incidents' && <IncidentsTab incidents={incidents} onRefresh={fetchIncidents} getStatusBadge={getStatusBadge} setShowModal={setShowModal} />}
            {activeTab === 'users' && <UsersTab users={users} onRefresh={fetchUsers} setShowModal={setShowModal} />}
          </>
        )}
      </main>

      {/* Modals */}
      {showModal === 'createIncident' && (
        <CreateIncidentModal 
          onClose={() => setShowModal(null)} 
          onSuccess={() => {
            setShowModal(null);
            fetchIncidents();
          }} 
        />
      )}
      {showModal === 'createUser' && (
        <CreateUserModal 
          onClose={() => setShowModal(null)} 
          onSuccess={() => {
            setShowModal(null);
            fetchUsers();
          }} 
        />
      )}

      {/* Chat Button */}
      <button 
        className="chat-fab"
        onClick={() => setShowChat(!showChat)}
        title="Chat with AI Assistant"
      >
        <Icon name="messageCircle" size={24} />
      </button>

      {/* Chat Widget */}
      {showChat && <GeminiChatWidget onClose={() => setShowChat(false)} />}
    </div>
  );
};

// Overview Tab Component
const OverviewTab = ({ stats, incidents, airQuality }) => (
  <>
    <div className="stats-grid">
      <div className="stat-card">
        <div className="stat-icon primary">
          <Icon name="users" size={28} />
        </div>
        <div className="stat-content">
          <h3>{stats.totalUsers}</h3>
          <p>Total Users</p>
        </div>
      </div>
      <div className="stat-card">
        <div className="stat-icon success">
          <Icon name="bus" size={28} />
        </div>
        <div className="stat-content">
          <h3>{stats.transportLines}</h3>
          <p>Transport Lines</p>
        </div>
      </div>
      <div className="stat-card">
        <div className="stat-icon warning">
          <Icon name="wind" size={28} />
        </div>
        <div className="stat-content">
          <h3>{stats.avgAqi}</h3>
          <p>Average AQI</p>
        </div>
      </div>
      <div className="stat-card">
        <div className="stat-icon danger">
          <Icon name="alert" size={28} />
        </div>
        <div className="stat-content">
          <h3>{stats.activeIncidents}</h3>
          <p>Active Incidents</p>
        </div>
      </div>
    </div>

    <div className="grid-2">
      <div className="card">
        <div className="card-header">
          <h2 className="card-title">
            <Icon name="alert" size={20} />
            Recent Incidents
          </h2>
        </div>
        <ul className="list">
          {incidents.slice(0, 5).map(incident => (
            <li key={incident.id} className="list-item">
              <div className="list-item-header">
                <span className="list-item-title">{incident.type}</span>
                <span className={`badge ${incident.status === 'new' ? 'danger' : incident.status === 'in-progress' ? 'warning' : 'success'}`}>
                  {incident.status}
                </span>
              </div>
              <p className="list-item-meta">{incident.description || 'No description'}</p>
            </li>
          ))}
        </ul>
        {incidents.length === 0 && (
          <div className="empty-state">
            <Icon name="alert" size={48} />
            <h3>No Incidents</h3>
            <p>All systems operating normally</p>
          </div>
        )}
      </div>

      <div className="card">
        <div className="card-header">
          <h2 className="card-title">
            <Icon name="wind" size={20} />
            Air Quality Status
          </h2>
        </div>
        <ul className="list">
          {airQuality.slice(0, 5).map(aq => {
            const level = aq.aqi <= 50 ? { label: 'Good', class: 'aqi-good' } : 
                         aq.aqi <= 100 ? { label: 'Moderate', class: 'aqi-moderate' } : 
                         { label: 'Unhealthy', class: 'aqi-unhealthy' };
            return (
              <li key={aq.id} className="list-item">
                <div className="list-item-header">
                  <span className="list-item-title">{aq.zone}</span>
                  <span className={`aqi-badge ${level.class}`}>
                    AQI: {aq.aqi} - {level.label}
                  </span>
                </div>
              </li>
            );
          })}
        </ul>
        {airQuality.length === 0 && (
          <div className="empty-state">
            <Icon name="wind" size={48} />
            <h3>No Air Quality Data</h3>
            <p>Waiting for sensor readings</p>
          </div>
        )}
      </div>
    </div>
  </>
);

// Transport Tab Component
const TransportTab = ({ lines, onRefresh }) => (
  <>
    <div className="card-header">
      <h2 className="card-title">
        <Icon name="bus" size={24} />
        Transport Lines
      </h2>
      <button className="btn btn-primary" onClick={onRefresh}>
        <Icon name="refresh" size={16} />
        Refresh
      </button>
    </div>

    {lines.length === 0 ? (
      <div className="card">
        <div className="empty-state">
          <Icon name="bus" size={48} />
          <h3>No Transport Lines</h3>
          <p>No transport lines available</p>
        </div>
      </div>
    ) : (
      <div className="grid-2">
        {lines.map(line => (
          <div key={line.id} className="card">
            <div className="card-header">
              <div>
                <h3 className="card-title">{line.name}</h3>
                <span className={`badge ${line.mode === 'bus' ? 'info' : line.mode === 'train' ? 'success' : 'warning'}`}>
                  {line.mode}
                </span>
              </div>
            </div>
            <p className="mb-2"><strong>Route:</strong> {line.route || 'Not specified'}</p>
            
            {line.schedules && line.schedules.length > 0 && (
              <>
                <h4 className="mb-1">Schedules</h4>
                <table className="schedule-table">
                  <thead>
                    <tr>
                      <th>Stop</th>
                      <th>Departure</th>
                      <th>Day</th>
                    </tr>
                  </thead>
                  <tbody>
                    {line.schedules.slice(0, 3).map(schedule => (
                      <tr key={schedule.id}>
                        <td>{schedule.stop}</td>
                        <td>{schedule.departureTime}</td>
                        <td>{schedule.dayOfWeek}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </>
            )}
          </div>
        ))}
      </div>
    )}
  </>
);

// Air Quality Tab Component
const AirQualityTab = ({ data, getAqiLevel }) => (
  <>
    <div className="card-header">
      <h2 className="card-title">
        <Icon name="wind" size={24} />
        Air Quality Monitoring
      </h2>
    </div>

    {data.length === 0 ? (
      <div className="card">
        <div className="empty-state">
          <Icon name="wind" size={48} />
          <h3>No Air Quality Data</h3>
          <p>No sensor readings available</p>
        </div>
      </div>
    ) : (
      <div className="grid-3">
        {data.map(aq => {
          const level = getAqiLevel(aq.aqi);
          return (
            <div key={aq.id} className="card">
              <h3 className="card-title">{aq.zone}</h3>
              <div className={`aqi-badge ${level.class} mb-2`}>
                AQI: {aq.aqi} - {level.label}
              </div>
              <div className="list-item-meta">
                <div>PM2.5: {aq.pm25?.toFixed(2) || 'N/A'}</div>
                <div>NO2: {aq.no2?.toFixed(2) || 'N/A'}</div>
                <div>CO2: {aq.co2?.toFixed(2) || 'N/A'}</div>
              </div>
              <p className="mt-1" style={{fontSize: '0.75rem', color: '#718096'}}>
                {new Date(aq.measuredAt).toLocaleString()}
              </p>
            </div>
          );
        })}
      </div>
    )}
  </>
);

// Incidents Tab Component
const IncidentsTab = ({ incidents, onRefresh, getStatusBadge, setShowModal }) => (
  <>
    <div className="card-header">
      <h2 className="card-title">
        <Icon name="alert" size={24} />
        Emergency Incidents
      </h2>
      <div className="flex gap-1">
        <button className="btn btn-primary" onClick={() => setShowModal('createIncident')}>
          <Icon name="plus" size={16} />
          Report Incident
        </button>
        <button className="btn btn-secondary" onClick={onRefresh}>
          <Icon name="refresh" size={16} />
        </button>
      </div>
    </div>

    {incidents.length === 0 ? (
      <div className="card">
        <div className="empty-state">
          <Icon name="alert" size={48} />
          <h3>No Incidents</h3>
          <p>All systems operating normally</p>
        </div>
      </div>
    ) : (
      <div className="card">
        <ul className="list">
          {incidents.map(incident => (
            <li key={incident.id} className="list-item">
              <div className="list-item-header">
                <div>
                  <span className="list-item-title">#{incident.id} - {incident.type}</span>
                  <span className={`badge ${getStatusBadge(incident.status)} ml-1`}>
                    {incident.status}
                  </span>
                </div>
              </div>
              <p className="list-item-meta">
                <span>{incident.description || 'No description'}</span>
              </p>
              <div className="list-item-meta mt-1">
                <span><Icon name="mapPin" size={14} /> {incident.lat?.toFixed(4)}, {incident.lon?.toFixed(4)}</span>
                <span>{new Date(incident.createdAt).toLocaleString()}</span>
              </div>
            </li>
          ))}
        </ul>
      </div>
    )}
  </>
);

// Users Tab Component
const UsersTab = ({ users, onRefresh, setShowModal }) => (
  <>
    <div className="card-header">
      <h2 className="card-title">
        <Icon name="users" size={24} />
        System Users
      </h2>
      <div className="flex gap-1">
        <button className="btn btn-primary" onClick={() => setShowModal('createUser')}>
          <Icon name="plus" size={16} />
          Add User
        </button>
        <button className="btn btn-secondary" onClick={onRefresh}>
          <Icon name="refresh" size={16} />
        </button>
      </div>
    </div>

    {users.length === 0 ? (
      <div className="card">
        <div className="empty-state">
          <Icon name="users" size={48} />
          <h3>No Users</h3>
          <p>No users registered in the system</p>
        </div>
      </div>
    ) : (
      <div className="card">
        <ul className="list">
          {users.map(user => (
            <li key={user.id} className="list-item">
              <div className="list-item-header">
                <span className="list-item-title">{user.username}</span>
              </div>
              <div className="list-item-meta">
                <span>{user.email || 'No email'}</span>
                <span>Joined: {new Date(user.createdAt).toLocaleDateString()}</span>
              </div>
            </li>
          ))}
        </ul>
      </div>
    )}
  </>
);

// Create Incident Modal
const CreateIncidentModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    type: 'accident',
    description: '',
    lat: '',
    lon: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await fetch(`${API_BASE}/integrator/api/incidents`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...formData,
          lat: parseFloat(formData.lat),
          lon: parseFloat(formData.lon)
        })
      });
      onSuccess();
    } catch (error) {
      console.error('Error creating incident:', error);
      alert('Failed to create incident');
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2 className="modal-title">Report New Incident</h2>
          <button className="close-button" onClick={onClose}>
            <Icon name="x" size={20} />
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="modal-body">
            <div className="form-group">
              <label className="form-label">Type</label>
              <select 
                className="form-select" 
                value={formData.type}
                onChange={e => setFormData({...formData, type: e.target.value})}
                required
              >
                <option value="accident">Accident</option>
                <option value="fire">Fire</option>
                <option value="ambulance">Ambulance</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Description</label>
              <textarea 
                className="form-textarea"
                value={formData.description}
                onChange={e => setFormData({...formData, description: e.target.value})}
                placeholder="Describe the incident..."
                required
              />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Latitude</label>
                <input 
                  type="number" 
                  step="0.000001"
                  className="form-input"
                  value={formData.lat}
                  onChange={e => setFormData({...formData, lat: e.target.value})}
                  placeholder="36.8065"
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Longitude</label>
                <input 
                  type="number" 
                  step="0.000001"
                  className="form-input"
                  value={formData.lon}
                  onChange={e => setFormData({...formData, lon: e.target.value})}
                  placeholder="10.1815"
                  required
                />
              </div>
            </div>
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="btn btn-danger">
              <Icon name="alert" size={16} />
              Report Incident
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

// Create User Modal
const CreateUserModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    username: '',
    email: ''
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const mutation = `
        mutation CreateUser($input: UserInput!) {
          createUser(input: $input) {
            id username email createdAt
          }
        }
      `;
      await fetch(`${API_BASE}/graphql`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
          query: mutation,
          variables: { input: formData }
        })
      });
      onSuccess();
    } catch (error) {
      console.error('Error creating user:', error);
      alert('Failed to create user');
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2 className="modal-title">Add New User</h2>
          <button className="close-button" onClick={onClose}>
            <Icon name="x" size={20} />
          </button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="modal-body">
            <div className="form-group">
              <label className="form-label">Username</label>
              <input 
                type="text" 
                className="form-input"
                value={formData.username}
                onChange={e => setFormData({...formData, username: e.target.value})}
                placeholder="Enter username"
                required
              />
            </div>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input 
                type="email" 
                className="form-input"
                value={formData.email}
                onChange={e => setFormData({...formData, email: e.target.value})}
                placeholder="user@example.com"
                required
              />
            </div>
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="btn btn-primary">
              <Icon name="plus" size={16} />
              Create User
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

// Gemini Chat Widget Component
const GeminiChatWidget = ({ onClose }) => {
  const [messages, setMessages] = useState([
    { role: 'assistant', content: 'Hello! I\'m your Smart City AI Assistant. How can I help you today?' }
  ]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const messagesEndRef = React.useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!input.trim() || loading) return;

    const userMessage = input.trim();
    setInput('');
    setMessages(prev => [...prev, { role: 'user', content: userMessage }]);
    setLoading(true);

    try {
      const response = await fetch('https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=YOUR_API_KEY', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          contents: [{
            parts: [{
              text: `You are a helpful Smart City assistant. Answer questions about the smart city platform, incidents, transport, air quality, and general city services. User question: ${userMessage}`
            }]
          }]
        })
      });

      const data = await response.json();
      const aiResponse = data.candidates?.[0]?.content?.parts?.[0]?.text || 'Sorry, I could not generate a response.';
      
      setMessages(prev => [...prev, { role: 'assistant', content: aiResponse }]);
    } catch (error) {
      console.error('Error calling Gemini API:', error);
      setMessages(prev => [...prev, { 
        role: 'assistant', 
        content: 'Sorry, I encountered an error. Please try again later.' 
      }]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="chat-widget">
      <div className="chat-header">
        <div className="chat-header-title">
          <Icon name="messageCircle" size={20} />
          <span>AI Assistant</span>
        </div>
        <button className="chat-close" onClick={onClose}>
          <Icon name="x" size={20} />
        </button>
      </div>

      <div className="chat-messages">
        {messages.map((msg, idx) => (
          <div key={idx} className={`chat-message ${msg.role}`}>
            <div className="chat-message-content">
              {msg.content}
            </div>
          </div>
        ))}
        {loading && (
          <div className="chat-message assistant">
            <div className="chat-message-content">
              <div className="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        )}
        <div ref={messagesEndRef} />
      </div>

      <form className="chat-input-form" onSubmit={sendMessage}>
        <input
          type="text"
          className="chat-input"
          placeholder="Ask me anything about the smart city..."
          value={input}
          onChange={(e) => setInput(e.target.value)}
          disabled={loading}
        />
        <button 
          type="submit" 
          className="chat-send-btn"
          disabled={loading || !input.trim()}
        >
          <Icon name="send" size={20} />
        </button>
      </form>
    </div>
  );
};

export default SmartCityDashboard;