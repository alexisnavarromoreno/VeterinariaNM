import clienteHttp from '../../compartido/servicios/clienteHttp.js'
import { API } from '../../compartido/constantes/apiEndpoints.js'

export const autenticacionServicio = {
  /**
   * @param {string} email
   * @param {string} password
   * @returns {Promise<TokenRespuesta>} — { token, tipo, expiraEn, idUsuario, nombreCompleto, email, rol }
   */
  login: async (email, password) => {
    const { data } = await clienteHttp.post(API.AUTENTICACION.LOGIN, { email, password })
    return data
  },
}
