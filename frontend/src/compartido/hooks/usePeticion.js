import { useCallback, useState } from 'react'

/**
 * Hook genérico para gestionar el estado de una llamada asíncrona a la API.
 * Evita repetir la triada {datos, cargando, error} en cada feature.
 *
 * @param {Function} funcionPeticion - Función async que realiza la llamada HTTP
 * @returns {{ ejecutar, datos, cargando, error, reiniciar }}
 */
export function usePeticion(funcionPeticion) {
  const [datos, setDatos]       = useState(null)
  const [cargando, setCargando] = useState(false)
  const [error, setError]       = useState(null)

  const ejecutar = useCallback(async (...args) => {
    setCargando(true)
    setError(null)
    try {
      const resultado = await funcionPeticion(...args)
      setDatos(resultado)
      return resultado
    } catch (err) {
      const mensaje = err.response?.data?.mensaje ?? err.message ?? 'Error desconocido'
      setError(mensaje)
      throw err
    } finally {
      setCargando(false)
    }
  }, [funcionPeticion])

  const reiniciar = useCallback(() => {
    setDatos(null)
    setError(null)
    setCargando(false)
  }, [])

  return { ejecutar, datos, cargando, error, reiniciar }
}
