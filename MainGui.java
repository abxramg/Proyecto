import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGui extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;



    public MainGui() {
        setTitle("To-Do List - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Iniciar Sesión");

        add(new JLabel("Correo o Nickname:"));
        add(emailField);
        add(new JLabel("Contraseña:"));
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(e -> login());

        setVisible(true);

    }


    private void login() {
        String userInput = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        Usuario usuario = GestorUsuarios.autenticar(userInput, password);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dispose();

        System.out.println("Tipo de usuario autenticado: " + usuario.getClass().getName());

        if (usuario instanceof Administrador) {
            new AdminPanel((Administrador) usuario);
        } else if (usuario instanceof Desarrollador) {
            new DeveloperPanel((Desarrollador) usuario);
        } else if (usuario instanceof Invitado) {
        JFrame frame = new JFrame("Panel de Invitado");
        new GuestPanel(usuario);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Cargar tareas desde el archivo
            GestorTareas.cargarTareas();

            //  Crear usuarios de prueba si es necesario
        if (GestorUsuarios.getUsuarios().isEmpty()) {
            System.out.println("Agregando usuarios de prueba...");
            GestorUsuarios gestor = new GestorUsuarios();
            GestorUsuarios.agregarUsuario(new Administrador("Abigail", "Abigail", "abigail@gmail.com", "1234")); 
            GestorUsuarios.agregarUsuario(new Desarrollador("Emma", "Emma", "emma@gmail.com", "2345"));
            GestorUsuarios.agregarUsuario(new Invitado("Jonathan", "Jonathan", "jonathan@gmail.com", "3456"));
        }
        //Iniciar interfaz grafica
        new MainGui();
    });

    }


    public void mostrarPanel(GuestPanel guestPanel, String string) {
       
        throw new UnsupportedOperationException("Unimplemented method 'mostrarPanel'");
    }


    public void mostrarPanel(DeveloperPanel developerPanel, String string) {
        
        throw new UnsupportedOperationException("Unimplemented method 'mostrarPanel'");
    }


    public void mostrarPanel(AdminPanel adminPanel, String string) {
        
        throw new UnsupportedOperationException("Unimplemented method 'mostrarPanel'");
    }
}
