import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 
import java.util.*;


public class GestorTareas {
    private static final String ARCHIVO_TAREAS = "tareas.dat";
    private static List<Tarea> tareas = new ArrayList<>();

    public static void agregarTarea(Tarea tarea) {
        tareas.add(tarea);
        guardarTareas();
    }

    public static void eliminarTarea(Tarea tarea) {
        tareas.remove(tarea);
        guardarTareas();
    }

    public static void actualizarTarea(Tarea tareaAntigua, Tarea tareaNueva) {
        int index = tareas.indexOf(tareaAntigua);
        if (index != -1) {
            tareas.set(index, tareaNueva);
            guardarTareas();
        }
    }

    public static List<Tarea> getTareas() {
        return tareas;
    }

    public static List<Tarea> getTareasPorUsuario(Usuario usuario) {
        return tareas.stream()
                .filter(t -> t.getUsuario().getId() == usuario.getId())
                .collect(Collectors.toList());
    }

    public static List<Tarea> getTareasPorEstado(String estado) {
        return tareas.stream()
                .filter(t -> t.getEstado().name().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    public static void cargarTareas() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_TAREAS))) {
            tareas = (List<Tarea>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudieron cargar las tareas (puede que sea la primera ejecución).");
            tareas = new ArrayList<>();
        }
    }

    public static void guardarTareas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_TAREAS))) {
            oos.writeObject(tareas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
