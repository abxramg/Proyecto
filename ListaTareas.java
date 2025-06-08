import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ListaTareas {
    private List<Tarea> tareas;
    private final String archivo = "tareas.ser";

    public ListaTareas() {
        this.tareas = new ArrayList<>();
        cargarTareas();
    }

    public void agregarTarea(Tarea tarea) {
        tareas.add(tarea);
        guardarTareas();
    }

    public void eliminarTarea(int id) {
        tareas.removeIf(t -> t.getId() == id);
        guardarTareas();
    }

    public void modificarTarea(int id, Tarea nuevaTarea) {
        for (int i=0; i < tareas.size(); i++) {
            if (tareas.get(i).getId() == id ) {
                tareas.set(i, nuevaTarea);
                guardarTareas();
                break;
            }
        }
    }

    public void modificarMisTareas(int id, Usuario autor, Tarea nuevaTarea) {
        for (int i=0; i < tareas.size(); i++) {
            if (tareas.get(i).getId() == id && tareas.get(i).getAsignacion().equals(autor)) {
                tareas.set(i, nuevaTarea);
                guardarTareas();
                break;
            }
        }
    }

    public void mostrarTareas() {
        if (tareas.isEmpty()) {
            System.out.println("No hay tareas asignadas.");
            return;
        }

        for (Tarea t : tareas) {
            System.out.println(t);
        }
    }

    public void mostrarTareasPorEstado(String estado) {
        ArrayList<Tarea> filtradas = new ArrayList<>();
        for (Tarea t : tareas) {
            if (t.getEstado().toString().equalsIgnoreCase(estado)) {
                filtradas.add(t);
            }
        }
        if (filtradas.isEmpty()) {
            System.out.println("No hay tareas en el estado seleccionado.\n");
        } else {
            for (Tarea t : filtradas) {
                System.out.println(t);
            }
        }
    }

    public void mostrarTareasPorUsuario(Usuario usuario) {
        ArrayList<Tarea> filtradas = new ArrayList<>();
        for (Tarea t : tareas) {
            if (t.getAsignacion().getId() == usuario.getId()) {
                filtradas.add(t);
            }
        }
        if (filtradas.isEmpty()) {
            System.out.println("No hay tareas asignadas a tal usuario.\n");
        } else {
            for (Tarea tarea : filtradas) {
                System.out.println("\n" + tarea);
            }
        }
    }

    public void mostrarTareasPorUsuarioYEstado(Usuario usuario, String estado) {
        ArrayList<Tarea> filtradas = new ArrayList<>();
        for (Tarea t : tareas) {
            if (t.getAsignacion().getId() == usuario.getId() && t.getEstado().toString().equalsIgnoreCase(estado)) {
                filtradas.add(t);
            }
        }
        if (filtradas.isEmpty()) {
            System.out.println("No tienes tareas con ese estado.");
        } else {
            for (Tarea tarea : filtradas) {
                System.out.println("\n" + tarea);
            }
        }
    }

    public Tarea buscarPorID(int id) {
        for (Tarea t : tareas) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    private void guardarTareas() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(tareas);
        } catch (IOException e) {
            System.out.println("Error al guardar tareas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarTareas() {
        File f = new File(archivo);
        if (f.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
                tareas = (List<Tarea>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar tareas: " + e.getMessage());
            }
        }
    }

    public List<Tarea> getTareas() {
        return tareas;
    }
}
