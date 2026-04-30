/**
 * Endpoints de la API REST del backend.
 * Versión prefijada con /api/v1 para facilitar migraciones de versión.
 */
const BASE = '/api/v1'

export const API = {
  AUTENTICACION: {
    LOGIN: `${BASE}/autenticacion/login`,
  },
  MASCOTAS: {
    BASE:            `${BASE}/mascotas`,
    POR_ID:          (id) => `${BASE}/mascotas/${id}`,
    POR_MICROCHIP:   (num) => `${BASE}/mascotas/microchip/${num}`,
    PESO:            (id) => `${BASE}/mascotas/${id}/peso`,
    ALERGIAS:        (id) => `${BASE}/mascotas/${id}/alergias`,
    CONDICIONES:     (id) => `${BASE}/mascotas/${id}/condiciones-cronicas`,
  },
  CLIENTES: {
    BASE:    `${BASE}/clientes`,
    POR_ID:  (id) => `${BASE}/clientes/${id}`,
  },
  CITAS: {
    BASE:         `${BASE}/citas`,
    POR_ID:       (id) => `${BASE}/citas/${id}`,
    CONFIRMAR:    (id) => `${BASE}/citas/${id}/confirmar`,
    CANCELAR:     (id) => `${BASE}/citas/${id}/cancelar`,
    COMPLETAR:    (id) => `${BASE}/citas/${id}/completar`,
    REPROGRAMAR:  (id) => `${BASE}/citas/${id}/reprogramar`,
  },
  HISTORIAL: {
    BASE:            `${BASE}/historial`,
    POR_ID:          (id) => `${BASE}/historial/${id}`,
    POR_MASCOTA:     (idMascota) => `${BASE}/historial/mascota/${idMascota}`,
    CERRAR:          (id) => `${BASE}/historial/${id}/cerrar`,
    RESULTADOS:      (id) => `${BASE}/historial/${id}/resultados-pruebas`,
  },
  MEDICAMENTOS: {
    BASE:         `${BASE}/medicamentos`,
    POR_ID:       (id) => `${BASE}/medicamentos/${id}`,
    COMPATIBLES:  (especie) => `${BASE}/medicamentos/compatibles?especie=${especie}`,
  },
  PRESCRIPCIONES: {
    BASE:          `${BASE}/prescripciones`,
    POR_ID:        (id) => `${BASE}/prescripciones/${id}`,
    EMITIR:        (id) => `${BASE}/prescripciones/${id}/emitir`,
    COMPATIBILIDAD:(id) => `${BASE}/prescripciones/${id}/compatibilidad`,
  },
  ESPECIES: {
    BASE: `${BASE}/especies`,
  },
  RAZAS: {
    BASE:         `${BASE}/razas`,
    POR_ESPECIE:  (idEspecie) => `${BASE}/razas?especie=${idEspecie}`,
  },
  VACUNAS: {
    BASE:          `${BASE}/vacunas`,
    POR_ESPECIE:   (especie) => `${BASE}/vacunas?especie=${especie}`,
  },
}
