package com.veterinaria.nm.dominio.puertos.entrada;

import com.veterinaria.nm.dominio.modelo.Cita;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ServicioCita {

    Cita asignarCita(Cita cita);

    Cita obtenerCitaPorId(UUID id);

    List<Cita> listarCitasPorFecha(LocalDate fecha);

    List<Cita> listarCitasPorMascota(UUID idMascota);

    List<Cita> listarCitasPorVeterinario(UUID idVeterinario);

    Cita confirmarCita(UUID id);

    Cita cancelarCita(UUID id);

    Cita completarCita(UUID id, String observaciones);

    Cita reprogramarCita(UUID id, Cita citaActualizada);
}
