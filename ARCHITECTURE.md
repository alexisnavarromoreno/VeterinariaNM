# VeterinariaNM — Arquitectura del Sistema

## Stack técnico

| Capa | Tecnología |
|------|-----------|
| Backend | Java 21 + Spring Boot 3.2 |
| Arquitectura | Hexagonal (Ports & Adapters) |
| Base de datos | PostgreSQL 16 |
| Seguridad | Spring Security + JWT (JJWT 0.12) |
| Frontend | React 18 + Vite 5 |
| Estilos | CSS Modules + Design Tokens (CSS Custom Properties) |
| Comunicación | REST API JSON |
| Mapeo | MapStruct (compile-time, sin reflexión) |
| Boilerplate | Lombok |

---

## Fase 1 — Arquitectura hexagonal

### Las cuatro capas y sus responsabilidades

```
┌─────────────────────────────────────────────────────────────────┐
│  ADAPTADORES DE ENTRADA              ADAPTADORES DE SALIDA      │
│  (REST Controllers)                  (JPA Repositories)         │
│           │                                   ▲                 │
│           ▼                                   │                 │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │              CAPA DE APLICACIÓN                         │    │
│  │  Casos de uso · DTOs (Records) · Mapeadores             │    │
│  │           │                   ▲                         │    │
│  │           ▼                   │                         │    │
│  │  ┌──────────────────────────────────────────────────┐   │    │
│  │  │               DOMINIO                            │   │    │
│  │  │  Modelos · Puertos E/S · Excepciones · Enums     │   │    │
│  │  └──────────────────────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │              INFRAESTRUCTURA                            │    │
│  │  JPA Entidades · Seguridad JWT · Configuración Spring   │    │
│  └─────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
```

### Capas en detalle

#### `dominio/`
- **Contiene:** Entidades de dominio, Value Objects, Puertos (interfaces), Excepciones, Enums de negocio
- **No contiene:** Anotaciones de Spring, JPA, Jackson, lógica de red
- **Regla:** El dominio no depende de nadie. Es el núcleo estable.

#### `aplicacion/`
- **Contiene:** Casos de uso, DTOs como Records, Mapeadores (MapStruct)
- **No contiene:** Lógica de dominio, código de persistencia, lógica HTTP
- **Regla:** Orquesta sin decidir. Delega al dominio las reglas de negocio.

#### `infraestructura/`
- **Contiene:** Entidades JPA, implementaciones de repositorios, JWT, configuración Spring Security
- **No contiene:** Lógica de negocio, reglas de dominio
- **Regla:** Implementa los puertos de salida del dominio. Conoce Spring, JPA y el exterior.

#### `adaptadores/entrada/rest/`
- **Contiene:** Controladores REST, manejador de excepciones global
- **No contiene:** Lógica de negocio, acceso directo a repositorios
- **Regla:** Solo traduce HTTP → llamada al puerto de entrada → HTTP.

### Flujo de una petición

```
POST /api/v1/mascotas
        │
        ▼
MascotaControlador          (deserializa JSON, valida @Valid)
        │
        ▼
CrearMascotaCasoUso         (verifica propietario, microchip único)
        │
        ▼
Mascota.crear(...)          (reglas de negocio: microchip ISO, peso > 0...)
        │
        ▼
RepositorioMascota.guardar  (puerto de salida → implementación JPA)
        │
        ▼
MascotaMapeador             (entidad dominio → DTO respuesta)
        │
        ▼
201 Created { MascotaRespuesta }
```

### Decisiones técnicas

**¿Por qué DTOs?**
Los DTOs (como Records) desacoplan la representación HTTP del modelo de dominio.
El modelo de dominio puede evolucionar sin romper contratos de API y viceversa.

**¿Por qué Records para DTOs?**
Inmutables por definición, sin boilerplate, validación compacta.
Perfectos para datos que solo viajan de A a B sin comportamiento.

**¿Por qué repositorios como puertos?**
El dominio define QUÉ necesita (`RepositorioMascota.guardar`).
La infraestructura define CÓMO (`MascotaRepositorioJpaAdaptador`).
Cambiar de JPA a JDBC o MongoDB no toca el dominio ni los casos de uso.

---

## Fase 2 — Modelo de dominio

### Entidades principales

```
Cliente ──────────── Mascota ──────────── Especie
   │                    │                    │
   │                    ├── Raza ─────────── ┘
   │                    ├── Alergia (tipo: MEDICAMENTO/ALIMENTARIA/...)
   │                    └── CondicionCronica (tipo enum con 30+ condiciones)
   │
   └── Cita ──────────── HistorialClinico
                                │
                                ├── PruebaDiagnostica (tipo enum: HEMOGRAMA, ECO...)
                                ├── ProcedimientoClinico (tipo enum: SUTURA, CATETER...)
                                └── Prescripcion
                                        └── LineaPrescripcion
                                                └── Medicamento
                                                        └── Dosis (record)
```

### Reglas de negocio clave

| Regla | Dónde vive |
|-------|-----------|
| Microchip: 15 dígitos ISO 11784 | `Mascota.validarMicrochip()` |
| Cita solo puede iniciarse si está CONFIRMADA | `Cita.iniciar()` |
| Prescripción inmutable tras emitirse | `Prescripcion.validarNoEmitida()` |
| Historial inmutable (diagnóstico) tras cerrarse | `HistorialClinico.validarAbierto()` |
| Dosis mg/kg × peso del animal | `Dosis.calcularParaPeso()` |
| Alerta de alergia a medicamento al prescribir | `Mascota.posibleAlergiA()` |
| Verificar especie contraindicada en medicamento | `Medicamento.estaContraindicadoPara()` |

---

## Fase 3 — Estructura de proyecto

### Backend
```
backend/src/main/java/com/veterinaria/nm/
├── VeterinariaNmApplication.java
├── dominio/
│   ├── modelo/
│   │   ├── valor/          # Records: Dosis, Direccion, SignosVitales, CondicionCorporal
│   │   ├── Cliente.java    # @Getter @Builder(PRIVATE) — factory method estático
│   │   ├── Mascota.java    # Alergias y CondicionesCronicas como entidades hijas
│   │   ├── Cita.java       # Máquina de estados con validaciones en transiciones
│   │   ├── HistorialClinico.java # PruebaDiagnostica + ProcedimientoClinico
│   │   ├── Medicamento.java # Contraindicaciones por especie
│   │   ├── Prescripcion.java + LineaPrescripcion.java
│   │   ├── Vacuna.java + RegistroVacunacion.java
│   │   ├── Especie.java + Raza.java + PredisposicionGenetica.java
│   │   ├── Alergia.java + CondicionCronica.java
│   │   ├── PruebaDiagnostica.java + ProcedimientoClinico.java
│   │   ├── Usuario.java    # Set<EspecialidadVeterinaria>
│   │   └── EspecialidadVeterinaria.java (enum, 20 especialidades)
│   ├── puertos/
│   │   ├── entrada/        # ServicioMascota, ServicioCliente, etc.
│   │   └── salida/         # RepositorioMascota, RepositorioCliente, etc.
│   └── excepciones/
├── aplicacion/
│   ├── casos_uso/          # Un caso de uso por operación
│   ├── dto/
│   │   ├── peticion/       # Records con @Valid
│   │   └── respuesta/      # Records sin anotaciones de validación
│   └── mapeadores/         # Interfaces MapStruct
├── infraestructura/
│   ├── persistencia/
│   │   ├── entidades/      # @Entity JPA — distinto del modelo de dominio
│   │   ├── repositorios/   # JpaRepository interfaces
│   │   └── adaptadores/    # Implementan puertos de salida
│   ├── seguridad/          # GestorJwt, FiltroAutenticacionJwt
│   └── configuracion/      # ConfiguracionSeguridad, CORS
└── adaptadores/
    └── entrada/
        └── rest/           # *Controlador.java, ManejadorExcepciones.java
```

### Frontend
```
frontend/src/
├── main.jsx               # Entry point
├── App.jsx                # BrowserRouter + ProveedorAutenticacion
├── app/
│   ├── EnrutadorApp.jsx   # Árbol de rutas (protegidas/públicas)
│   └── contexto/
│       └── ContextoAutenticacion.jsx
├── autenticacion/
│   ├── componentes/       # FormularioLogin
│   ├── paginas/           # PaginaLogin
│   ├── servicios/         # autenticacionServicio.js (axios calls)
│   └── hooks/             # useAutenticacion.js
├── mascotas/              # Ídem estructura: componentes/paginas/servicios/hooks
├── clientes/
├── citas/
├── historial/
├── dashboard/
└── compartido/
    ├── componentes/       # Boton, Cargando, AlertaClinica, Sidebar, Topbar, Layout
    ├── estilos/           # tokens.css, global.css
    ├── hooks/             # usePeticion.js (estado async genérico)
    ├── servicios/         # clienteHttp.js (axios configurado)
    └── constantes/        # rutas.js, apiEndpoints.js
```

---

## Fase 4 — Diseño UI/UX

### Pantallas y estructura visual

#### Login
- Fondo con gradiente azul oscuro
- Tarjeta centrada: logo + título + subtítulo + formulario
- Estados: vacío → validando → error (AlertaClinica roja) → redirect

#### Dashboard
- Grid 2×2: Citas hoy / Alertas vacunación / Mascotas activas / Clientes
- Lista de próximas citas del día (orden cronológico)
- Alertas: mascotas con pruebas pendientes, vacunas vencidas

#### Listado de mascotas
- Buscador superior (nombre, microchip)
- Filtros: especie, raza, solo activas
- Grid de tarjetas: foto/avatar + nombre + especie/raza + propietario + alertas
- Alertas críticas (alergia ANAFILAXIA) en badge rojo

#### Detalle de mascota
- Header: nombre + especie + raza + edad + peso + esterilizado
- Sección alergias: badges por tipo (rojo=crítico, amarillo=moderado)
- Sección condiciones crónicas: list con estado y fecha diagnóstico
- Sección historial: timeline con las últimas consultas
- Sección vacunaciones: próximas pendientes primero

#### Gestión de citas (agenda)
- Vista semanal/diaria con slots de tiempo
- Color por estado: SOLICITADA (gris), CONFIRMADA (azul), EN_CURSO (verde), COMPLETADA (neutral)
- Formulario lateral al crear: mascota + veterinario + tipo + motivo + duración

#### Consulta activa (historial)
- Flujo en pasos: Signos vitales → Exploración → Diagnóstico → Pruebas → Tratamiento → Cierre
- Sección prescripción: selector de medicamentos con alerta automática de incompatibilidades
- Sección procedimientos: selección múltiple del enum TipoProcedimiento

### Sistema de alertas clínicas
- `AlertaClinica tipo="critica"`: borde rojo, fondo rojo claro — ANAFILAXIA, errores críticos
- `AlertaClinica tipo="advertencia"`: borde amarillo — vacunas vencidas, condiciones crónicas activas
- `AlertaClinica tipo="info"`: borde azul — recordatorios, notas
- `AlertaClinica tipo="exito"`: borde verde — operación completada

---

## Fase 5 — Integración con Figma

### Estructura de páginas en Figma

```
📁 VeterinariaNM Design System
  📄 Tokens               ← Variables de color, tipografía, espaciado
  📄 Componentes          ← Boton, Input, Alerta, Tarjeta, Badge, Modal
  📄 Iconografía          ← Set de iconos consistente

📁 Pantallas
  📄 Autenticación        ← Login
  📄 Dashboard            ← Dashboard principal
  📄 Mascotas             ← Listado + Detalle + Formulario crear
  📄 Clientes             ← Listado + Detalle
  📄 Citas                ← Agenda semanal + Formulario
  📄 Consulta activa      ← Flujo completo paso a paso
  📄 Historial            ← Timeline de consultas
```

### Tokens en Figma → CSS

| Figma Variable | CSS Custom Property |
|---------------|-------------------|
| `color/primario/600` | `--color-primario-600` (#2563eb) |
| `color/acento/600` | `--color-acento-600` (#16a34a) |
| `color/peligro/600` | `--color-peligro-600` (#dc2626) |
| `color/alerta/500` | `--color-alerta-500` (#f59e0b) |
| `tipografia/base` | `--fuente-base` (Inter) |
| `espacio/4` | `--esp-4` (1rem = 16px) |

### Nombrado de componentes (Figma ↔ React)

```
Figma: Boton/Primario/MD  →  React: <Boton variante="primario" tamano="md">
Figma: Alerta/Critica     →  React: <AlertaClinica tipo="critica">
Figma: Estado/Cargando    →  React: <Cargando centrado />
```

---

## Fase 6 — Dependencias

### Backend (`pom.xml`)

| Dependencia | Versión | Justificación |
|------------|---------|--------------|
| spring-boot-starter-web | 3.2.5 | Servidor HTTP + REST |
| spring-boot-starter-security | 3.2.5 | Autenticación/autorización |
| spring-boot-starter-data-jpa | 3.2.5 | ORM para persistencia |
| spring-boot-starter-validation | 3.2.5 | Bean Validation en DTOs |
| postgresql | Runtime | Driver JDBC para PostgreSQL |
| jjwt-api/impl/jackson | 0.12.5 | Generación y validación JWT |
| lombok | 1.18.32 | Reducir boilerplate en dominio |
| mapstruct | 1.5.5 | Mapeo compile-time dominio↔JPA↔DTO |
| spring-boot-starter-test | Test | JUnit 5 + Mockito |

### Frontend (`package.json`)

| Dependencia | Versión | Justificación |
|------------|---------|--------------|
| react + react-dom | 18.3 | UI library |
| react-router-dom | 6.26 | Enrutado SPA |
| axios | 1.7 | HTTP client con interceptores |
| date-fns | 3.6 | Formateo de fechas sin bundle pesado |
| vite | 5.3 | Build tool rápido (dev + prod) |

**Ausentes deliberadamente:**
- ❌ Redux/Zustand — Context API es suficiente para este dominio
- ❌ TailwindCSS — CSS Modules + tokens dan más control sin overhead
- ❌ React Query — `usePeticion` hook cubre el caso de uso actual
- ❌ Formik/React Hook Form — formularios sencillos no lo necesitan aún

---

## Fase 7 — Plan de implementación

### Sprint 0 — Base (ahora)
- [x] Estructura de proyecto backend y frontend
- [x] Modelos de dominio completos
- [x] Puertos de entrada y salida
- [x] DTOs como Records con validación
- [x] Configuración Spring Security + JWT
- [x] Design tokens CSS + componentes base

### Sprint 1 — Autenticación
1. Implementar `RepositorioUsuario` (JPA)
2. Implementar `ServicioAutenticacion` (login + JWT)
3. Seed de usuario administrador inicial (SQL migration con Flyway)
4. Frontend: `PaginaLogin` conectada al backend real

### Sprint 2 — Catálogos base
5. CRUD de Especies y Razas (sin frontend aún)
6. CRUD de Medicamentos con contraindicaciones
7. CRUD de Vacunas

### Sprint 3 — Clientes y Mascotas
8. CRUD completo Cliente (backend + frontend)
9. CRUD completo Mascota con alergias y condiciones crónicas
10. Búsqueda por microchip

### Sprint 4 — Agenda de citas
11. Backend: crear, confirmar, cancelar, completar citas
12. Verificación de solapamientos en agenda
13. Frontend: vista semanal de agenda

### Sprint 5 — Módulo clínico
14. Backend: iniciar consulta → signos vitales → exploración → diagnóstico
15. Backend: pruebas diagnósticas y procedimientos
16. Backend: prescripciones con verificación de compatibilidad
17. Frontend: flujo de consulta paso a paso

### Sprint 6 — Historial y vacunaciones
18. Timeline de historial por mascota
19. Registro de vacunaciones
20. Alertas de vacunas vencidas en dashboard

### Sprint 7 — Pulido y producción
21. Migraciones de BD con Flyway
22. Tests unitarios de casos de uso
23. Tests de integración de controladores
24. Dockerización (docker-compose: backend + frontend + postgres)
25. Variables de entorno y configuración de producción

---

## Convenciones del proyecto

### Nomenclatura
- Todo en español: clases, métodos, variables, endpoints, componentes
- Endpoints: `/api/v1/{recurso-en-plural}`
- Factory methods de dominio: `Entidad.crear(...)` o `Entidad.registrar(...)`
- Casos de uso: `VerboCasoUso.ejecutar(peticion)`
- Hooks React: `use{Nombre}.js`
- Servicios React: `{modulo}Servicio.js`

### Seguridad
- Contraseñas: BCrypt factor 12
- JWT: HS256, expira en 8h (configurable)
- Sesión en `sessionStorage` (se limpia al cerrar navegador)
- CORS configurado explícitamente por entorno
- Control de acceso por rol con `@PreAuthorize`

### Errores
- Dominio lanza excepciones específicas (`MascotaNoEncontradaException`)
- `ManejadorExcepciones` las traduce a `ErrorRespuesta` JSON uniforme
- Frontend interpreta el campo `mensaje` para mostrar al usuario
