import styles from './AlertaClinica.module.css'

/**
 * Componente para mostrar alertas clínicas (alergias, condiciones crónicas).
 * Usa colores semánticos de los tokens: rojo para crítico, amarillo para advertencia.
 *
 * @param {'critica'|'advertencia'|'info'|'exito'} tipo
 * @param {string} titulo
 */
export default function AlertaClinica({ tipo = 'info', titulo, children, className = '' }) {
  const iconos = {
    critica:     '⚠',
    advertencia: '⚠',
    info:        'ℹ',
    exito:       '✓',
  }

  return (
    <div className={`${styles.alerta} ${styles[tipo]} ${className}`} role="alert">
      <span className={styles.icono} aria-hidden="true">{iconos[tipo]}</span>
      <div className={styles.contenido}>
        {titulo && <p className={styles.titulo}>{titulo}</p>}
        {children && <div className={styles.cuerpo}>{children}</div>}
      </div>
    </div>
  )
}
