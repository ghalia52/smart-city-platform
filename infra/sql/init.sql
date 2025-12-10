-- Database: smartcity

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  email VARCHAR(150),
  created_at TIMESTAMP DEFAULT now()
);

-- Mobility: transport lines & schedules
CREATE TABLE transport_line (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  mode VARCHAR(50) NOT NULL, -- bus/train/metro
  route TEXT
);

CREATE TABLE schedule (
  id SERIAL PRIMARY KEY,
  line_id INTEGER REFERENCES transport_line(id) ON DELETE CASCADE,
  stop VARCHAR(150),
  departure_time TIME,
  arrival_time TIME,
  day_of_week VARCHAR(20) -- e.g., "Mon-Fri"
);

-- Air quality
CREATE TABLE air_quality (
  id SERIAL PRIMARY KEY,
  zone VARCHAR(100) NOT NULL,
  aqi INTEGER,
  pm25 FLOAT,
  no2 FLOAT,
  co2 FLOAT,
  o3 FLOAT,
  measured_at TIMESTAMP DEFAULT now()
);

-- Emergencies (gRPC) - simple event log
CREATE TABLE incidents (
  id SERIAL PRIMARY KEY,
  type VARCHAR(80), -- accident, fire, ambulance
  description TEXT,
  lat DOUBLE PRECISION,
  lon DOUBLE PRECISION,
  status VARCHAR(30) DEFAULT 'new',
  created_at TIMESTAMP DEFAULT now()
);
