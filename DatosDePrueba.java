import java.time.LocalDate;

public class DatosDePrueba {

    public static void cargar(GestorUsuarios gestorUsuarios, ListaTareas listaTareas) {

        if (GestorUsuarios.getUsuarios().isEmpty()) {
            Administrador admin = new Administrador("Abigail", "Abigail", "abigail@gmail.com", "1234");
            Desarrollador dev = new Desarrollador("Emma", "Emma", "emma@gmail.com", "2345");
            Invitado invitado = new Invitado("Jonathan", "Jonathan", "jonathan@gmail.com", "3456");

            GestorUsuarios.agregarUsuario(admin);
            GestorUsuarios.agregarUsuario(dev);
            GestorUsuarios.agregarUsuario(invitado);

            System.out.println("Usuarios de prueba creados.");
        }

        if (listaTareas.getTareas().isEmpty()) {
            Usuario dev = GestorUsuarios.buscarPorCorreoONick("dev");

            LocalDate hoy = LocalDate.now();
            LocalDate inicio = hoy.plusDays(1);
            LocalDate fin = hoy.plusDays(5);

            Tarea ejemplo = new Tarea("Informe", "Realizar informe del proyecto", dev, inicio, fin);
            listaTareas.agregarTarea(ejemplo);

            System.out.println("Tarea de prueba creada.");
        }
    }
}
