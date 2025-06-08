import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdminPanel extends JFrame {
    private Administrador admin;
    private JTable tabla;
    private TareaTableModel tableModel;
    private JComboBox<String> filtroEstadoCombo;
    private JComboBox<String> filtroUsuarioCombo;


    public AdminPanel(Usuario admin) {
        if (!(admin instanceof Administrador)) {
        throw new IllegalArgumentException("El usuario proporcionado no es un administrador.");
    }
        this.admin = (Administrador) admin; 

        setTitle("Panel de Administrador - Bienvenido " + this.admin.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);


        // Tabla de tareas
        tableModel = new TareaTableModel(GestorTareas.getTareas());
        tabla = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(tabla);

        // Botones
        JButton btnNuevaTarea = new JButton("Nueva Tarea");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        JButton btnRegistrar = new JButton("Registrar nuevo usuario");
        JButton btnEliminarUsuario = new JButton("Eliminar usuario");

        // Filtros
        filtroEstadoCombo = new JComboBox<>(new String[]{"Todos", "Pendiente", "En Curso", "Completada"});
        filtroUsuarioCombo = new JComboBox<>();
        filtroUsuarioCombo.addItem("Todos");
        for (Usuario u : GestorUsuarios.getUsuarios()) {
            filtroUsuarioCombo.addItem(u.getNombre());
        }

        JPanel panelFiltros = new JPanel();
        panelFiltros.add(new JLabel("Filtrar por estado:"));
        panelFiltros.add(filtroEstadoCombo);
        panelFiltros.add(new JLabel("Filtrar por usuario:"));
        panelFiltros.add(filtroUsuarioCombo);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnNuevaTarea);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrarSesion);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEliminarUsuario);

        // Listeners
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new MainGui(); // Vuelve al login
        });

        filtroEstadoCombo.addActionListener(e -> aplicarFiltros());
        filtroUsuarioCombo.addActionListener(e -> aplicarFiltros());

        btnEliminarUsuario.addActionListener(e -> {
    List<Usuario> usuarios = GestorUsuarios.getUsuarios();
    
    String[] opciones = usuarios.stream()
        .map(Usuario::getNombre)
        .filter(nombre -> !nombre.equalsIgnoreCase(admin.getNombre())) // No eliminarse a sí mismo
        .toArray(String[]::new);

    if (opciones.length == 0) {
        JOptionPane.showMessageDialog(this, "No hay usuarios disponibles para eliminar.");
        return;
    }

    String usuarioSeleccionado = (String) JOptionPane.showInputDialog(
        this,
        "Selecciona un usuario para eliminar:",
        "Eliminar usuario",
        JOptionPane.QUESTION_MESSAGE,
        null,
        opciones,
        opciones[0]
    );

    if (usuarioSeleccionado != null) {
        Usuario u = GestorUsuarios.buscarPorCorreoONick(usuarioSeleccionado);
        if (u != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar al usuario \"" + u.getNombre() + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                GestorUsuarios.eliminarUsuario(u);
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
            }
        }
    }
});

        btnNuevaTarea.addActionListener(e -> {
            TareaForm formulario = new TareaForm(null, admin, true);
            aplicarFiltros();  // Actualiza la tabla y los filtros
        });

        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                Tarea tarea = tableModel.getTareaEn(filaSeleccionada);
                TareaForm formulario = new TareaForm(tarea, admin, false);
                aplicarFiltros();  // Actualiza la tabla y los filtros
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una tarea para editar.");
            }
        });

        btnRegistrar.addActionListener(e -> abrirRegistro()); 
    

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada != -1) {
                Tarea tarea = tableModel.getTareaEn(filaSeleccionada);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar tarea?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    GestorTareas.eliminarTarea(tarea);
                    GestorTareas.guardarTareas();
                    aplicarFiltros();  // Actualiza la tabla y los filtros
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una tarea para eliminar.");
            }
        });

        // Layout
        setLayout(new BorderLayout());
        add(panelFiltros, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }


    private void abrirRegistro() {
    JFrame registroFrame = new JFrame("Registrar nuevo usuario");
    registroFrame.setSize(400, 300);
    registroFrame.setLocationRelativeTo(this);
    registroFrame.setLayout(new GridLayout(6, 2));

    JTextField nombreField = new JTextField();
    JTextField nicknameField = new JTextField();
    JTextField emailField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    String[] tipos = {"Administrador", "Desarrollador", "Invitado"};
    JComboBox<String> tipoBox = new JComboBox<>(tipos);
    JButton registrarBtn = new JButton("Registrar");

    registroFrame.add(new JLabel("Nombre:"));
    registroFrame.add(nombreField);
    registroFrame.add(new JLabel("Nickname:"));
    registroFrame.add(nicknameField);
    registroFrame.add(new JLabel("Email:"));
    registroFrame.add(emailField);
    registroFrame.add(new JLabel("Contraseña:"));
    registroFrame.add(passwordField);
    registroFrame.add(new JLabel("Tipo de usuario:"));
    registroFrame.add(tipoBox);
    registroFrame.add(new JLabel());
    registroFrame.add(registrarBtn);

    registrarBtn.addActionListener(e -> {
        String nombre = nombreField.getText().trim();
        String nickname = nicknameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String tipo = (String) tipoBox.getSelectedItem();

        if (nombre.isEmpty() || nickname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(registroFrame, "Todos los campos son obligatorios.");
            return;
        }

        Usuario nuevo;
        switch (tipo) {
            case "Administrador":
                nuevo = new Administrador(nombre, nickname, email, password);
                break;
            case "Desarrollador":
                nuevo = new Desarrollador(nombre, nickname, email, password);
                break;
            default:
                nuevo = new Invitado(nombre, nickname, email, password);
                break;
        }

        boolean exito = GestorUsuarios.registrarUsuario(nuevo);
        if (exito) {
            JOptionPane.showMessageDialog(registroFrame, "Usuario registrado con éxito.");
            registroFrame.dispose();

        filtroUsuarioCombo.addItem(nuevo.getNombre());
}
    });

    registroFrame.setVisible(true);
}

    private void aplicarFiltros() {
        String estadoSeleccionado = (String) filtroEstadoCombo.getSelectedItem();
        String usuarioSeleccionado = (String) filtroUsuarioCombo.getSelectedItem();

        List<Tarea> tareasFiltradas = GestorTareas.getTareas().stream()
            .filter(t -> estadoSeleccionado.equals("Todos") || 
                t.getEstado().name().replace("_", " ").equalsIgnoreCase(estadoSeleccionado))
            .filter(t -> usuarioSeleccionado.equals("Todos") || 
            (t.getUsuario() != null && usuarioSeleccionado.equals(t.getUsuario().getNombre())))
            .collect(Collectors.toList());

        tableModel.setTareas(tareasFiltradas);
        tableModel.fireTableDataChanged();

    }
}


