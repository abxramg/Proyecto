import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class App {

    private static GestorUsuarios gestorUsuarios = new GestorUsuarios();
    private static ListaTareas gestorTareas = new ListaTareas();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

public static void main(String[] args) {

    DatosDePrueba.cargar(gestorUsuarios, gestorTareas);
    Usuario.actualizarContadorID(GestorUsuarios.getUsuarios());
    Tarea.actualizarContadorID(gestorTareas.getTareas());

    System.out.println("Bienvenido al Sistema de Gestión de Tareas");
    while (true) {
        System.out.println("1. Iniciar sesión.");
        System.out.println("2. Salir");
        System.out.print("Selecciona una opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                iniciarSesion();
                break;
            case "2":
                System.out.println("Cerrando sesión.");
                return;
            default:
                System.out.println("Opción inválida.");
        }
    }
    
}

    // Solicitar datos y validar usuario para iniciar sesión
    private static void iniciarSesion() {
        System.out.println("Correo o nickname: ");
        String identificador = scanner.nextLine();
        System.out.println("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario usuario = GestorUsuarios.autenticar(identificador, contrasena);
        if (usuario == null) {
            System.out.println("Datos incorrectos. Intente de nuevo.");
            return;
        }
        
        System.out.println("Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");

        switch (usuario.getRol()) {
            case "Administrador":
                menuAdmin((Administrador) usuario);
                break;
            case "Desarrollador":
                menuDesarrollador((Desarrollador) usuario);
                break;
            case "Invitado":
                menuInvitado((Invitado) usuario);
                break;
        }
    }  

    // Menú mostrado para un usuario administrador

    private static void menuAdmin(Administrador admin) {
        while (true) {
            System.out.println("Menú Administrador:");
            System.out.println("1. Crear nuevo usuario");
            System.out.println("2. Crear tarea");
            System.out.println("3. Ver tareas");
            System.out.println("4. Filtrar tareas por estado");
            System.out.println("5. Filtrar tareas por usuario");
            System.out.println("6. Actualizar tarea");
            System.out.println("7. Eliminar tarea");
            System.out.println("8. Cerrar sesión");
            System.out.print("Elige una opción: ");
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    crearUsuario();
                    break;
                case "2":
                    crearTarea();
                    break;
                case "3": 
                    gestorTareas.mostrarTareas();
                    break;
                case "4":
                    filtrarPorEstado();
                    break;
                case "5":
                    filtrarPorUsuario();
                    break;
                case "6":
                    actualizarTarea(admin);
                    break;
                case "7":
                    eliminarTarea(admin);
                    break;
                case "8":
                    System.out.println("Cerrando sesión...");
                    return;  // Salir del menú administrador
                default:
                    System.out.println("Opción no válida.");
            }   
    

        }
    }

    // Menú mostrado para un usuario desarrollador

    private static void menuDesarrollador(Desarrollador dev) {
        while (true) {
            System.out.println("Menú Desarrollador: ");
            System.out.println("1. Crear tarea propia");
            System.out.println("2. Ver mis tareas");
            System.out.println("3. Filtrar mis tareas por estado");
            System.out.println("4. Actualizar mis tareas");
            System.out.println("5. Cerrar sesión");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    crearTarea(dev);
                    break;
                case "2": 
                    gestorTareas.mostrarTareasPorUsuario(dev);
                    break;
                case "3":
                    System.out.print("Estado (PENDIENTE, EN_CURSO, COMPLETADA): ");
                    String estado = scanner.nextLine().toUpperCase();
                    gestorTareas.mostrarTareasPorUsuarioYEstado(dev, estado);
                    break;
                case "4":
                    actualizarTarea(dev);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opcion inválida.");
            }
        }
    }

    // Menú mostrado para un usuario invitado

    private static void menuInvitado(Invitado invitado) {
        while (true) {
            System.out.println("Menú Invitado: ");
            System.out.println("1. Ver todas las tareas");;
            System.out.println("2. Filtrar tareas por estado");
            System.out.println("3. Filtrar tareas por usuario");
            System.out.println("4. Cerrar sesión");
            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    gestorTareas.mostrarTareas();
                    break;
                case "2":
                    filtrarPorEstado();
                    break;
                case "3":
                    filtrarPorUsuario();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void crearUsuario() {
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        for (Usuario usuario : GestorUsuarios.getUsuarios()) {
            if (nombre.equals(usuario.getNombre())) {
                System.out.println("Ese nombre ya está registrado.");
                return;
            }
        }
        
        System.out.print("Nickname: ");
        String nick = scanner.nextLine();
        for (Usuario usuario : GestorUsuarios.getUsuarios()) {
            if (nick.equals(usuario.getNickname())) {
                System.out.println("Nickname no disponible.");
                return;
            }
        }


        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        for (Usuario usuario : GestorUsuarios.getUsuarios()) {
            if (!correoValido(correo)) {
                System.out.println("Ingrese un correo válido.");
                return;
            }
            if (correo.equals(usuario.getCorreo())) {
                System.out.println("Correo ya registrado.");
                return;
            }
        }


        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        System.out.println("Tipo de usuario: ");
        System.out.println("1. Administrador");
        System.out.println("2. Desarrollador");
        System.out.println("3. Invitado");
        String tipo = scanner.nextLine();

        
        for (Usuario usuario : GestorUsuarios.getUsuarios()) {
            if (nombre.equals(usuario.getNombre())) {
                System.out.println("Nombre ya registrado.");
                return;
            }
        }

        Usuario nuevo = null;
        
        switch (tipo) {
            case "1":
                nuevo = new Administrador(nombre, nick, correo, pass);
                break;
            case "2":
                nuevo = new Desarrollador(nombre, nick, correo, pass);
                break;
            case "3":
                nuevo = new Invitado(nombre, nick, correo, pass);
                break;
            default:
                System.out.println("Tipo de usuario inválido.");
                break;
        }

        if (nuevo != null) {
            GestorUsuarios.agregarUsuario(nuevo);
            System.out.println("Usuario " + nuevo.getId() + " creado con éxito.");
        }
    }

    private static void crearTarea(Usuario usuario) {
    try {
        System.out.println("Crear una nueva tarea.");
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine();

        System.out.print("Fecha estimada de inicio (dd/mm/aaaa): ");
        LocalDate inicio = LocalDate.parse(scanner.nextLine(), formato);
        System.out.print("Fecha estimada de finalización (dd/mm/aaaa): ");
        LocalDate fin = LocalDate.parse(scanner.nextLine(), formato);

        Tarea nueva = new Tarea("Titulo", "Descripcion", usuario, inicio, fin);
        gestorTareas.agregarTarea(nueva);

        System.out.println("Tarea creada con éxito.");
        
        } catch (Exception e) {
            System.out.println("Error al crear tarea: " + e.getMessage());
        }
    }

    private static void crearTarea() {
        try {
            System.out.println("Crear una nueva tarea.");
            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine();
    
            System.out.print("Fecha estimada de inicio (dd/mm/aaaa): ");
            LocalDate inicio = LocalDate.parse(scanner.nextLine(), formato);
            System.out.print("Fecha estimada de finalización (dd/mm/aaaa): ");
            LocalDate fin = LocalDate.parse(scanner.nextLine(), formato);

            System.out.print("Nickname o nombre del usuario al que se asignará: ");
            String nONN = scanner.nextLine();
            Usuario usuario = null;
            for (Usuario u : GestorUsuarios.getUsuarios()) {
                if (nONN.equals(u.getNickname()) || nONN.equals(u.getNombre())) {
                    usuario = u;
                }
            }
            if (usuario == null) {
                throw new NoSuchElementException("No existe un usuario con ese nombre o apodo.");
            }
    
            Tarea nueva = new Tarea("Titulo", "Descripcion", usuario, inicio, fin);
            gestorTareas.agregarTarea(nueva);
    
            System.out.println("Tarea creada con éxito.");
            
            } catch (Exception e) {
                System.out.println("Error al crear tarea: " + e.getMessage());
            }
        }

    private static void actualizarTarea(Usuario usuario) {
        System.out.print("ID de la tarea a actualizar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Tarea tarea = gestorTareas.buscarPorID(id);
        if (tarea == null) {
            System.out.println("Tarea no encontrada.");
            return;
        }

        if (!tarea.getAsignacion().equals(usuario) && !usuario.getRol().equals("Administrador")) {
            System.out.println("No puedes modificar esta tarea.");
            return;
        }

        System.out.println("1. Avanzar estado");
        System.out.println("2. Cambiar descripción");
        System.out.println("3. Cambiar fecha estimada de inicio");
        System.out.println("4. Cambiar fecha estimada de finalización");
        if (usuario.getRol().equals("Administrador")) {
            System.out.println("5. Cambiar usuario asignado");
        }

        System.out.print("Opcion: ");
        String opc = scanner.nextLine();

        switch (opc) {
            case "1":
                tarea.avanzarEstado(); 
                System.out.println("Estado actualizado.");
                break;

            case "2":
                System.out.print("Nueva descripción: ");
                tarea.setDescripcion(scanner.nextLine());
                break;

            case "3":
                try {
                    System.out.print("Nueva fecha estimada de inicio (dd/mm/aaaa): ");
                    LocalDate nuevaInicio = LocalDate.parse(scanner.nextLine(), formato);
                    tarea.setFechaEstimadaInicio(nuevaInicio);
                    System.out.println("Fecha estimada de inicio actualizada.");

                    } catch (Exception e) {
                        System.out.println("Fecha inválida.");
                    }
                    break;

            case "4":
                try {
                    System.out.println("Nueva fecha estimada de finalización (dd/mm/aaaa): ");
                    LocalDate nuevaFin = LocalDate.parse(scanner.nextLine(), formato);
                    tarea.setFechaEstimadaFin(nuevaFin);
                    System.out.println("Fecha estimada de finalización actualizada.");

                    } catch (Exception e) {
                        System.out.println("Fecha inválida.");
                    }

                    break;

            case "5":
                if (!usuario.getRol().equals("Administrador")) {
                    System.out.println("Solo el administrador puede reasignar la tarea.");
                    break;
                }

                System.out.println("Nickname del nuevo usuario: ");
                String nick = scanner.nextLine();
                Usuario nuevoUsuario = GestorUsuarios.buscarPorCorreoONick(nick);
                if (nuevoUsuario != null) {
                    tarea.setUsuario(nuevoUsuario);
                    System.out.println("Usuario asignado actualizado.");
                } else { 
                    System.out.println("Usuario no encontrado.");
                }
                break;

            default:
                System.out.println("Opción inválida.");
        }
    }

    private static void eliminarTarea(Administrador admin) {
        System.out.println("ID de la tarea a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());
        gestorTareas.eliminarTarea(id);
        System.out.println("Tarea eliminada.");
    }

    private static void filtrarPorEstado() {
        System.out.print("Estado (PENDIENTE, EN_CURSO, COMPLETADA): ");
        String estado = scanner.nextLine().toUpperCase();
        gestorTareas.mostrarTareasPorEstado(estado);
    }

    private static void filtrarPorUsuario() {
        System.out.print("Nickname del usuario: ");
        String nick = scanner.nextLine();
        Usuario u = GestorUsuarios.buscarPorCorreoONick(nick);

        if (u !=null) {
            gestorTareas.mostrarTareasPorUsuario(u);
        } else { System.out.println("Usuario no encontrado."); }
        
    }

    private static boolean correoValido(String correo) {
        if (!correo.contains("@"))
            return false;
        String[] secciones = correo.split("@");
        if (secciones.length > 2)
            return false;
        if (secciones[0].trim().equals("") || secciones[1].trim().equals(""))
            return false;
        if (!secciones[1].contains("."))
            return false;
        for (String s : secciones[1].split(".")) {
            if (s.trim().equals(""))
                return false;
        }
        return true;
    }
}  