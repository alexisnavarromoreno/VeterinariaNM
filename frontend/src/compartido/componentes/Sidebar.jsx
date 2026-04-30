import { NavLink } from 'react-router-dom'
import { RUTAS } from '../constantes/rutas.js'
import styles from './Sidebar.module.css'

const ENLACES = [
  { a: RUTAS.DASHBOARD,  etiqueta: 'Dashboard',  icono: '📊' },
  { a: RUTAS.CITAS,      etiqueta: 'Citas',       icono: '📅' },
  { a: RUTAS.MASCOTAS,   etiqueta: 'Mascotas',    icono: '🐾' },
  { a: RUTAS.CLIENTES,   etiqueta: 'Clientes',    icono: '👥' },
]

export default function Sidebar() {
  return (
    <aside className={styles.sidebar}>
      <div className={styles.marca}>
        <span aria-hidden="true">🐾</span>
        <span>VeterinariaNM</span>
      </div>
      <nav className={styles.nav} aria-label="Navegación principal">
        {ENLACES.map((e) => (
          <NavLink
            key={e.a}
            to={e.a}
            className={({ isActive }) =>
              `${styles.enlace} ${isActive ? styles.activo : ''}`
            }
          >
            <span className={styles.icono} aria-hidden="true">{e.icono}</span>
            {e.etiqueta}
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
