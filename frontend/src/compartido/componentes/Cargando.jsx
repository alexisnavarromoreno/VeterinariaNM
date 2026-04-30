import styles from './Cargando.module.css'

/**
 * Spinner de carga reutilizable.
 *
 * @param {'sm'|'md'|'lg'} tamano
 * @param {'primario'|'blanco'|'actual'} color
 * @param {string} etiqueta - Texto accesible para lectores de pantalla
 */
export default function Cargando({
  tamano = 'md',
  color = 'primario',
  etiqueta = 'Cargando...',
  centrado = false,
}) {
  return (
    <div className={`${styles.contenedor} ${centrado ? styles.centrado : ''}`}>
      <div
        className={`${styles.spinner} ${styles[tamano]} ${styles[color]}`}
        role="status"
        aria-label={etiqueta}
      />
      <span className="solo-lector">{etiqueta}</span>
    </div>
  )
}
