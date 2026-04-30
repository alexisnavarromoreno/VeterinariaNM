import styles from './Boton.module.css'
import Cargando from './Cargando.jsx'

/**
 * Componente de botón base del sistema de diseño.
 *
 * @param {'primario'|'secundario'|'peligro'|'fantasma'} variante - Estilo visual
 * @param {'sm'|'md'|'lg'} tamano - Tamaño del botón
 * @param {boolean} cargando - Muestra spinner y deshabilita el botón
 * @param {boolean} iconoIzquierda - Icono antes del texto
 */
export default function Boton({
  children,
  variante = 'primario',
  tamano = 'md',
  cargando = false,
  disabled = false,
  iconoIzquierda = null,
  iconoDerecha = null,
  tipo = 'button',
  onClick,
  className = '',
  ...props
}) {
  return (
    <button
      type={tipo}
      className={`${styles.boton} ${styles[variante]} ${styles[tamano]} ${className}`}
      disabled={disabled || cargando}
      onClick={onClick}
      {...props}
    >
      {cargando ? (
        <Cargando tamano="sm" color="actual" />
      ) : (
        <>
          {iconoIzquierda && <span className={styles.icono}>{iconoIzquierda}</span>}
          {children}
          {iconoDerecha && <span className={styles.icono}>{iconoDerecha}</span>}
        </>
      )}
    </button>
  )
}
