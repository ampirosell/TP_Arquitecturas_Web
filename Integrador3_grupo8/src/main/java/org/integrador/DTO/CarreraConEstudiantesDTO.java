package org.integrador.DTO;

public class CarreraConEstudiantesDTO {
    private Long carreraId;
    private String nombre;
    private Integer duracion;
    private Long cantidadEstudiantes;

    public CarreraConEstudiantesDTO() {}

    public CarreraConEstudiantesDTO(Long carreraId, String nombre, Integer duracion, Long cantidadEstudiantes) {
        this.carreraId = carreraId;
        this.nombre = nombre;
        this.duracion = duracion;
        this.cantidadEstudiantes = cantidadEstudiantes;
    }

    // Getters y Setters
    public Long getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Long carreraId) {
        this.carreraId = carreraId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Long getCantidadEstudiantes() {
        return cantidadEstudiantes;
    }

    public void setCantidadEstudiantes(Long cantidadEstudiantes) {
        this.cantidadEstudiantes = cantidadEstudiantes;
    }
}
