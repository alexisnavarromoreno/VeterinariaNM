import { Routes, Route, Navigate } from 'react-router-dom'
import { useAutenticacion } from '../autenticacion/hooks/useAutenticacion.js'
import PaginaLogin from '../autenticacion/paginas/PaginaLogin.jsx'
import PaginaDashboard from '../dashboard/paginas/PaginaDashboard.jsx'
import PaginaMascotas from '../mascotas/paginas/PaginaMascotas.jsx'
import PaginaDetalleMascota from '../mascotas/paginas/PaginaDetalleMascota.jsx'
import PaginaClientes from '../clientes/paginas/PaginaClientes.jsx'
import PaginaDetalleCliente from '../clientes/paginas/PaginaDetalleCliente.jsx'
import PaginaCitas from '../citas/paginas/PaginaCitas.jsx'
import PaginaHistorial from '../historial/paginas/PaginaHistorial.jsx'
import LayoutPrincipal from '../compartido/componentes/LayoutPrincipal.jsx'
import { RUTAS } from '../compartido/constantes/rutas.js'

/**
 * Define la jerarquía de rutas de la aplicación.
 * Las rutas protegidas redirigen al login si no hay sesión activa.
 */
export default function EnrutadorApp() {
  const { estaAutenticado } = useAutenticacion()

  if (!estaAutenticado) {
    return (
      <Routes>
        <Route path={RUTAS.LOGIN} element={<PaginaLogin />} />
        <Route path="*" element={<Navigate to={RUTAS.LOGIN} replace />} />
      </Routes>
    )
  }

  return (
    <Routes>
      <Route element={<LayoutPrincipal />}>
        <Route index element={<Navigate to={RUTAS.DASHBOARD} replace />} />
        <Route path={RUTAS.DASHBOARD} element={<PaginaDashboard />} />
        <Route path={RUTAS.MASCOTAS} element={<PaginaMascotas />} />
        <Route path={RUTAS.DETALLE_MASCOTA} element={<PaginaDetalleMascota />} />
        <Route path={RUTAS.CLIENTES} element={<PaginaClientes />} />
        <Route path={RUTAS.DETALLE_CLIENTE} element={<PaginaDetalleCliente />} />
        <Route path={RUTAS.CITAS} element={<PaginaCitas />} />
        <Route path={RUTAS.HISTORIAL} element={<PaginaHistorial />} />
      </Route>
      <Route path="*" element={<Navigate to={RUTAS.DASHBOARD} replace />} />
    </Routes>
  )
}
