import { createContext, useCallback, useContext, useMemo, useState } from 'react'

const ContextoAutenticacion = createContext(null)

const CLAVE_TOKEN    = 'veterinaria_token'
const CLAVE_USUARIO  = 'veterinaria_usuario'

/**
 * Proveedor del contexto de autenticación.
 * <p>
 * Gestiona el token JWT y los datos del usuario en sessionStorage.
 * Se usa sessionStorage (no localStorage) para que la sesión se cierre
 * al cerrar el navegador, behavior apropiado para un sistema clínico
 * con múltiples usuarios en el mismo equipo.
 * </p>
 */
export function ProveedorAutenticacion({ children }) {
  const [usuario, setUsuario] = useState(() => {
    try {
      const datos = sessionStorage.getItem(CLAVE_USUARIO)
      return datos ? JSON.parse(datos) : null
    } catch {
      return null
    }
  })

  const iniciarSesion = useCallback((datosToken) => {
    sessionStorage.setItem(CLAVE_TOKEN, datosToken.token)
    sessionStorage.setItem(CLAVE_USUARIO, JSON.stringify({
      id:            datosToken.idUsuario,
      nombreCompleto: datosToken.nombreCompleto,
      email:         datosToken.email,
      rol:           datosToken.rol,
    }))
    setUsuario({
      id:            datosToken.idUsuario,
      nombreCompleto: datosToken.nombreCompleto,
      email:         datosToken.email,
      rol:           datosToken.rol,
    })
  }, [])

  const cerrarSesion = useCallback(() => {
    sessionStorage.removeItem(CLAVE_TOKEN)
    sessionStorage.removeItem(CLAVE_USUARIO)
    setUsuario(null)
  }, [])

  const obtenerToken = useCallback(() => {
    return sessionStorage.getItem(CLAVE_TOKEN)
  }, [])

  const valor = useMemo(() => ({
    usuario,
    estaAutenticado: !!usuario,
    iniciarSesion,
    cerrarSesion,
    obtenerToken,
    esVeterinario:     usuario?.rol === 'VETERINARIO',
    esAdministrador:   usuario?.rol === 'ADMINISTRADOR',
    esRecepcionista:   usuario?.rol === 'RECEPCIONISTA',
  }), [usuario, iniciarSesion, cerrarSesion, obtenerToken])

  return (
    <ContextoAutenticacion.Provider value={valor}>
      {children}
    </ContextoAutenticacion.Provider>
  )
}

/** Hook para consumir el contexto de autenticación desde cualquier componente. */
export function useContextoAutenticacion() {
  const contexto = useContext(ContextoAutenticacion)
  if (!contexto) throw new Error('useContextoAutenticacion debe usarse dentro de ProveedorAutenticacion')
  return contexto
}
