import axios from 'axios'

/**
 * Instancia de Axios configurada con el token JWT.
 * <p>
 * El interceptor de petición adjunta automáticamente el Bearer token
 * a todas las llamadas autenticadas. El interceptor de respuesta maneja
 * errores 401 (token expirado) redirigiendo al login.
 * </p>
 */
const clienteHttp = axios.create({
  baseURL: '/',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Interceptor de petición: adjunta el token JWT
clienteHttp.interceptors.request.use(
  (config) => {
    const token = sessionStorage.getItem('veterinaria_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

// Interceptor de respuesta: maneja token expirado
clienteHttp.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      sessionStorage.removeItem('veterinaria_token')
      sessionStorage.removeItem('veterinaria_usuario')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  },
)

export default clienteHttp
