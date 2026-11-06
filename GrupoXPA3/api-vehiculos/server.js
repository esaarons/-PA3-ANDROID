// API REST de vehículos (PA3 - punto 2.7)
// Login con token JWT y CRUD completo. Los datos viven en memoria:
// al reiniciar el servidor vuelven a los datos de ejemplo.

const express = require('express');
const cors = require('cors');
const jwt = require('jsonwebtoken');
const os = require('os');

const app = express();
const PORT = process.env.PORT || 3000;
const SECRET = 'pa3-vehiculos-clave-secreta';
const USUARIO = { username: 'admin', password: 'admin123' };

app.use(cors());
app.use(express.json());

// Registro simple de cada petición (útil para ver la actividad durante el video)
app.use((req, _res, next) => {
  console.log(`${new Date().toLocaleTimeString()}  ${req.method} ${req.originalUrl}`);
  next();
});

let vehiculos = [
  { id: 1, placa: 'ABC-123', marca: 'Toyota', modelo: 'Corolla', anio: 2020 },
  { id: 2, placa: 'XYZ-789', marca: 'Hyundai', modelo: 'Tucson', anio: 2022 },
  { id: 3, placa: 'DEF-456', marca: 'Kia', modelo: 'Rio', anio: 2019 }
];
let siguienteId = 4;

// ---------- Autenticación ----------
function autenticar(req, res, next) {
  const cabecera = req.headers.authorization || '';
  const [tipo, token] = cabecera.split(' ');
  if (tipo !== 'Bearer' || !token) {
    return res.status(401).json({ message: 'Token requerido' });
  }
  try {
    jwt.verify(token, SECRET);
    next();
  } catch (e) {
    return res.status(401).json({ message: 'Token inválido o vencido' });
  }
}

app.post('/api/auth/login', (req, res) => {
  const { username, password } = req.body || {};
  if (username === USUARIO.username && password === USUARIO.password) {
    const accessToken = jwt.sign({ username }, SECRET, { expiresIn: '12h' });
    return res.json({ accessToken });
  }
  return res.status(401).json({ message: 'Usuario o contraseña incorrectos' });
});

// ---------- Validación ----------
function validar(body) {
  const { placa, marca, modelo, anio } = body || {};
  if (!placa || !marca || !modelo) {
    return 'placa, marca y modelo son obligatorios';
  }
  if (!Number.isInteger(anio) || anio < 1950 || anio > 2100) {
    return 'El año debe ser un número entero entre 1950 y 2100';
  }
  return null;
}

// ---------- CRUD de vehículos ----------
app.get('/api/vehiculos', autenticar, (_req, res) => {
  res.json(vehiculos);
});

app.get('/api/vehiculos/:id', autenticar, (req, res) => {
  const v = vehiculos.find((x) => x.id === Number(req.params.id));
  if (!v) return res.status(404).json({ message: 'Vehículo no encontrado' });
  res.json(v);
});

app.post('/api/vehiculos', autenticar, (req, res) => {
  const error = validar(req.body);
  if (error) return res.status(400).json({ message: error });
  const { placa, marca, modelo, anio } = req.body;
  const nuevo = { id: siguienteId++, placa, marca, modelo, anio };
  vehiculos.push(nuevo);
  res.status(201).json(nuevo);
});

app.put('/api/vehiculos/:id', autenticar, (req, res) => {
  const indice = vehiculos.findIndex((x) => x.id === Number(req.params.id));
  if (indice === -1) return res.status(404).json({ message: 'Vehículo no encontrado' });
  const error = validar(req.body);
  if (error) return res.status(400).json({ message: error });
  const { placa, marca, modelo, anio } = req.body;
  vehiculos[indice] = { id: vehiculos[indice].id, placa, marca, modelo, anio };
  res.json(vehiculos[indice]);
});

app.delete('/api/vehiculos/:id', autenticar, (req, res) => {
  const indice = vehiculos.findIndex((x) => x.id === Number(req.params.id));
  if (indice === -1) return res.status(404).json({ message: 'Vehículo no encontrado' });
  vehiculos.splice(indice, 1);
  res.status(204).end();
});

// ---------- Arranque ----------
app.listen(PORT, '0.0.0.0', () => {
  console.log('API de vehículos lista. Usuario: admin / Clave: admin123');
  console.log('Usa una de estas URLs como BASE_URL en la app (celular en la misma Wi-Fi):');
  Object.values(os.networkInterfaces())
    .flat()
    .filter((i) => i.family === 'IPv4' && !i.internal)
    .forEach((i) => console.log(`   http://${i.address}:${PORT}/`));
});
