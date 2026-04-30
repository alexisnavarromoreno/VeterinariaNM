import { useContextoAutenticacion } from '../../app/contexto/ContextoAutenticacion.jsx'
import { autenticacionServicio } from '../servicios/autenticacionServicio.js'
import { usePeticion } from '../../compartido/hooks/usePeticion.js'
import { useCallback } from 'react'

/**
 * Hook de autenticación para ser usado en componentes.
 * Expone las acciones de login/logout y el estado del usuario.
 */
export function useAutenticacion() {
  const { usuario, estaAutenticado, iniciarSesion, cerrarSesion,
          esVeterinario, esAdministrador, esRecepcionista } = useContextoAutenticacion()

  const { ejecutar: ejecutarLogin, cargando, error } = usePeticion(autenticacionServicio.login)

  const login = useCallback(async (email, password) => {
    const respuesta = await ejecutarLogin(email, password)
    iniciarSesion(respuesta)
    return respuesta
  }, [ejecutarLogin, iniciarSesion])

  return {
    usuario,
    estaAutenticado,
    esVeterinario,
    esAdministrador,
    esRecepcionista,
    login,
    logout: cerrarSesion,
    cargando,
    error,
  }
}
