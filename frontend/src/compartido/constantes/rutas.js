/**
 * Constantes de rutas de la aplicación.
 * Centralizar aquí evita strings dispersos y facilita refactorizaciones.
 */
export const RUTAS = {
  LOGIN:            '/login',
  DASHBOARD:        '/dashboard',
  MASCOTAS:         '/mascotas',
  DETALLE_MASCOTA:  '/mascotas/:id',
  CLIENTES:         '/clientes',
  DETALLE_CLIENTE:  '/clientes/:id',
  CITAS:            '/citas',
  HISTORIAL:        '/historial/:idMascota',
  MEDICAMENTOS:     '/medicamentos',
  USUARIOS:         '/usuarios',
}

/** Genera la URL de detalle de una mascota con el id real. */
export const rutaDetalleMascota = (id) => `/mascotas/${id}`
export const rutaDetalleCliente = (id) => `/clientes/${id}`
export const rutaHistorial      = (idMascota) => `/historial/${idMascota}`
