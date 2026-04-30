import clienteHttp from '../../compartido/servicios/clienteHttp.js'
import { API } from '../../compartido/constantes/apiEndpoints.js'

export const mascotasServicio = {
  obtenerPorId: async (id) => {
    const { data } = await clienteHttp.get(API.MASCOTAS.POR_ID(id))
    return data
  },

  listarPorPropietario: async (idPropietario) => {
    const { data } = await clienteHttp.get(API.MASCOTAS.BASE, { params: { propietario: idPropietario } })
    return data
  },

  buscarPorNombre: async (nombre) => {
    const { data } = await clienteHttp.get(API.MASCOTAS.BASE, { params: { nombre } })
    return data
  },

  crear: async (datos) => {
    const { data } = await clienteHttp.post(API.MASCOTAS.BASE, datos)
    return data
  },

  actualizarPeso: async (id, peso) => {
    const { data } = await clienteHttp.patch(API.MASCOTAS.PESO(id), null, { params: { peso } })
    return data
  },

  agregarAlergia: async (id, alergia) => {
    const { data } = await clienteHttp.post(API.MASCOTAS.ALERGIAS(id), alergia)
    return data
  },

  desactivar: async (id) => {
    await clienteHttp.delete(API.MASCOTAS.POR_ID(id))
  },
}
