import { BrowserRouter } from 'react-router-dom'
import { ProveedorAutenticacion } from './app/contexto/ContextoAutenticacion.jsx'
import EnrutadorApp from './app/EnrutadorApp.jsx'

/**
 * Componente raíz de la aplicación.
 * Establece el contexto de autenticación y el enrutador antes de renderizar rutas.
 */
export default function App() {
  return (
    <BrowserRouter>
      <ProveedorAutenticacion>
        <EnrutadorApp />
      </ProveedorAutenticacion>
    </BrowserRouter>
  )
}
