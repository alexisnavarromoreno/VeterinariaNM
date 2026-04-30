import { useAutenticacion } from '../../autenticacion/hooks/useAutenticacion.js'
import Boton from './Boton.jsx'
import styles from './Topbar.module.css'

export default function Topbar() {
  const { usuario, logout } = useAutenticacion()

  return (
    <header className={styles.topbar}>
      <div className={styles.derecha}>
        <span className={styles.nombreUsuario}>{usuario?.nombreCompleto}</span>
        <span className={styles.rol}>{usuario?.rol}</span>
        <Boton variante="fantasma" tamano="sm" onClick={logout}>
          Cerrar sesión
        </Boton>
      </div>
    </header>
  )
}
