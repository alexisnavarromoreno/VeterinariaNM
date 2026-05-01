package com.veterinaria.nm.aplicacion.mapeadores;

import com.veterinaria.nm.aplicacion.dto.respuesta.MascotaRespuesta;
import com.veterinaria.nm.dominio.modelo.Mascota;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// TODO Bloque 5: completar con resolución de nombreEspecie, nombreRaza, nombrePropietario
@Mapper(componentModel = "spring")
public interface MascotaMapeador {

    @Mapping(target = "edadDescriptiva", expression = "java(mascota.getEdadDescriptiva())")
    @Mapping(target = "nombreEspecie",   ignore = true)
    @Mapping(target = "nombreRaza",      ignore = true)
    @Mapping(target = "alergias",        ignore = true)
    @Mapping(target = "enfermedadesCronicas", ignore = true)
    @Mapping(target = "nombrePropietario", ignore = true)
    MascotaRespuesta aDtoRespuesta(Mascota mascota);
}
