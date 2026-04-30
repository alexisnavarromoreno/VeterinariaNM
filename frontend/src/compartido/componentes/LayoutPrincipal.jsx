import { Outlet } from 'react-router-dom'
import Sidebar from './Sidebar.jsx'
import Topbar from './Topbar.jsx'
import styles from './LayoutPrincipal.module.css'

/**
 * Layout base para todas las páginas protegidas.
 * Estructura: Sidebar fijo a la izquierda + área de contenido (Topbar + página).
 */
export default function LayoutPrincipal() {
  return (
    <div className={styles.contenedor}>
      <Sidebar />
      <div className={styles.principal}>
        <Topbar />
        <main className={styles.contenido}>
          <Outlet />
        </main>
      </div>
    </div>
  )
}
