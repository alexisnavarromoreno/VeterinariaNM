import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { usePeticion } from '../../compartido/hooks/usePeticion.js'
import { mascotasServicio } from '../servicios/mascotasServicio.js'
import Cargando from '../../compartido/componentes/Cargando.jsx'
import Boton from '../../compartido/componentes/Boton.jsx'
import AlertaClinica from '../../compartido/componentes/AlertaClinica.jsx'
import { rutaDetalleMascota } from '../../compartido/constantes/rutas.js'
import styles from './PaginaMascotas.module.css'

/**
 * Listado principal de mascotas.
 * Punto de entrada para buscar, filtrar y registrar nuevas mascotas.
 */
export default function PaginaMascotas() {
  const navegar = useNavigate()
  const { ejecutar: buscar, datos: mascotas, cargando, error } = usePeticion(
    (nombre) => mascotasServicio.buscarPorNombre(nombre)
  )

  // Estado vacío con guía de acción
  const SinMascotas = () => (
    <div className={styles.estadoVacio}>
      <span className={styles.iconoVacio} aria-hidden="true">🐾</span>
      <h3>No hay mascotas registradas</h3>
      <p>Registra la primera mascota para comenzar.</p>
      <Boton variante="primario">Registrar mascota</Boton>
    </div>
  )

  return (
    <div className={styles.pagina}>
      <div className={styles.cabecera}>
        <h1>Mascotas</h1>
        <Boton variante="primario">+ Nueva mascota</Boton>
      </div>

      {error && <AlertaClinica tipo="critica">{error}</AlertaClinica>}
      {cargando && <Cargando centrado />}
      {!cargando && mascotas?.length === 0 && <SinMascotas />}

      {/* TODO: Listado con TarjetaMascota + filtros por especie/raza */}
    </div>
  )
}
