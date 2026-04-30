import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAutenticacion } from '../hooks/useAutenticacion.js'
import Boton from '../../compartido/componentes/Boton.jsx'
import AlertaClinica from '../../compartido/componentes/AlertaClinica.jsx'
import { RUTAS } from '../../compartido/constantes/rutas.js'
import styles from './PaginaLogin.module.css'

/**
 * Pantalla de acceso al sistema.
 * Diseño minimalista centrado: logotipo, formulario, mensaje de error.
 * No hay registro público — los usuarios los crea el administrador.
 */
export default function PaginaLogin() {
  const [email, setEmail]       = useState('')
  const [password, setPassword] = useState('')
  const { login, cargando, error } = useAutenticacion()
  const navegar = useNavigate()

  const manejarEnvio = async (e) => {
    e.preventDefault()
    try {
      await login(email, password)
      navegar(RUTAS.DASHBOARD, { replace: true })
    } catch {
      // El error ya está en el estado del hook
    }
  }

  return (
    <div className={styles.pagina}>
      <div className={styles.tarjeta}>
        <div className={styles.cabecera}>
          <span className={styles.logo} aria-hidden="true">🐾</span>
          <h1 className={styles.titulo}>VeterinariaNM</h1>
          <p className={styles.subtitulo}>Sistema de gestión clínica</p>
        </div>

        <form onSubmit={manejarEnvio} className={styles.formulario} noValidate>
          {error && (
            <AlertaClinica tipo="critica" titulo="Credenciales incorrectas">
              {error}
            </AlertaClinica>
          )}

          <div className={styles.campo}>
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="nombre@veterinaria.com"
              autoComplete="email"
              required
            />
          </div>

          <div className={styles.campo}>
            <label htmlFor="password">Contraseña</label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              autoComplete="current-password"
              required
            />
          </div>

          <Boton tipo="submit" variante="primario" tamano="lg" cargando={cargando} className={styles.botonLogin}>
            Iniciar sesión
          </Boton>
        </form>
      </div>
    </div>
  )
}
