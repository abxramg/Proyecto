import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Tarea implements Serializable {

    private static int contadorID = 1;

    private int id;
    private String titulo;
    private String descripcion;
    private Estado estado;
    private Usuario usuario;
    private LocalDate fechaFinalizacion;

    private LocalDate fechaEstimadaInicio;
    private LocalDate fechaInicio;
    private LocalDate fechaEstimadaFinal;
    private LocalDate fechaFinal;

    public enum Estado {
        PENDIENTE, EN_CURSO, COMPLETADA;
    }

    // ✅ Constructor principal
    public Tarea(String titulo, String descripcion, Usuario asignacion, LocalDate fechaEstimadaInicio, LocalDate fechaEstimadaFinal) {
        this.id = contadorID++;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.usuario = asignacion;
        this.estado = Estado.PENDIENTE;

        if (fechaEstimadaInicio.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Fecha inválida.");
        }

        if (fechaEstimadaFinal.isBefore(fechaEstimadaInicio)) {
            throw new IllegalArgumentException("Fecha inválida.");
        }

        this.fechaEstimadaInicio = fechaEstimadaInicio;
        this.fechaEstimadaFinal = fechaEstimadaFinal;
        this.fechaInicio = null;
        this.fechaFinal = null;
    }

    // ✅ PUNTO 2 – Constructor más simple (sin fechas)
    public Tarea(String titulo, String descripcion, Usuario asignacion) {
        this(titulo, descripcion, asignacion, LocalDate.now().plusDays(1), LocalDate.now().plusDays(7));
    }

    public void avanzarEstado() {
        switch (estado) {
            case PENDIENTE:
                this.estado = Estado.EN_CURSO;
                this.fechaInicio = LocalDate.now();
                break;
            case EN_CURSO:
                this.estado = Estado.COMPLETADA;
                this.fechaFinal = LocalDate.now();
                break;
            case COMPLETADA:
                System.out.println("¡Tarea completada!");
                break;
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public LocalDate getFechaEstimadaInicio() {
        return fechaEstimadaInicio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaEstimadaFinal() {
        return fechaEstimadaFinal;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Usuario getAsignacion() {
        return usuario;
    }

    public LocalDate getFechaEstimadaFin() {
        return fechaEstimadaFinal;
    }

    public LocalDate getFechaFin() {
        return fechaFinalizacion;
    }

    // Setters
    public void setTitulo(String titulo) {
        if (estado != Estado.PENDIENTE) {
            System.out.println("No se puede cambiar el título una vez iniciada.");
            return;
        }
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        if (estado != Estado.PENDIENTE) {
            System.out.println("No se puede cambiar la descripción una vez iniciada.");
            return;
        }
        this.descripcion = descripcion;
        System.out.println("Descripción actualizada.");
    }

    public void setFechaEstimadaInicio(LocalDate fecha) {
        if (estado != Estado.PENDIENTE) {
            System.out.println("No se puede modificar la fecha estimada de inicio una vez iniciada.");
            return;
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha estimada de inicio no puede estar en el pasado.");
        }
        if (fecha.isAfter(fechaEstimadaFinal)) {
            throw new IllegalArgumentException("La fecha estimada de inicio no puede ser posterior a la fecha estimada de finalización.");
        }
        this.fechaEstimadaInicio = fecha;
    }

    public void setFechaEstimadaFin(LocalDate fecha) {
        if (estado != Estado.PENDIENTE) {
            System.out.println("La fecha estimada de finalización no puede modificarse una vez iniciada.");
            return;
        }
        if (fecha.isBefore(fechaEstimadaInicio)) {
            throw new IllegalArgumentException("La fecha estimada de finalización no puede ser anterior a la fecha estimada de inicio.");
        }
        this.fechaEstimadaFinal = fecha;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    // ✅ PUNTO 4 – toString actualizado con el título
    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "ID: " + id +
                "\nTítulo: " + titulo +
                "\nDescripción: " + descripcion +
                "\nAsignación: " + usuario.getNickname() +
                "\nEstado: " + estado +
                "\nFecha estimada de inicio: " + formato.format(fechaEstimadaInicio) +
                "\nFecha estimada de finalización: " + formato.format(fechaEstimadaFinal) +
                (fechaInicio != null ? "\nFecha de inicio: " + formato.format(fechaInicio) : "") +
                (fechaFinal != null ? "\nFecha de finalización: " + formato.format(fechaFinal) : "") + "\n";
    }

    public static void actualizarContadorID(List<Tarea> tareas) {
        int nuevoID = 1;
        for (Tarea tarea : tareas) {
            if (tarea.getId() > nuevoID) {
                nuevoID = tarea.getId();
            }
        }
        contadorID = nuevoID + 1;
    }
}
