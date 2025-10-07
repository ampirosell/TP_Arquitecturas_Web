package org.integrador.DTO;


import org.integrador.entity.Estudiante;

public class EstudianteDTO {
    private String nombre;
    private String apellido;
    private String LU;

    public EstudianteDTO(Estudiante estudiante){
        this.nombre = estudiante.getNombre();
        this.apellido = estudiante.getApellido();
        this.LU = estudiante.getNumeroLU();
    }

    @Override
    public String toString() {
        return "\nLU = " + LU +
                ", apellido = " + apellido +
                ", nombre = " + nombre;
    }
}