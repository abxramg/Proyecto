import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DeveloperPanel extends JFrame {
    private Usuario usuario;
    private JTable tablaTareas;
    private TareaTableModel modeloTabla;
    private JComboBox<String> filtroEstadoCombo;
    private JButton btnCrear, btnEditar, btnAvanzar, btnCerrarSesion;


    public DeveloperPanel(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Panel de Desarrollador - " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modeloTabla = new TareaTableModel(obtenerTareasFiltradas(null));
        tablaTareas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        add(scrollPane, BorderLayout.CENTER);

        // Filtros
        filtroEstadoCombo = new JComboBox<>(new String[]{"Todos", "Pendiente", "En Curso", "Completada"});
        filtroEstadoCombo.addActionListener(e -> aplicarFiltro());

        JPanel panelFiltros = new JPanel();
        panelFiltros.add(new JLabel("Filtrar por estado:"));
        panelFiltros.add(filtroEstadoCombo);
        add(panelFiltros, BorderLayout.NORTH);

        // Botones
        JPanel panelBotones = new JPanel();

        btnCrear = new JButton("Crear Tarea");
        btnEditar = new JButton("Editar Tarea");
        btnAvanzar = new JButton("Avanzar Estado");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        panelBotones.add(btnCrear);
        panelBotones.add(btnEditar);
        panelBotones.add(btnAvanzar);
        panelBotones.add(btnCerrarSesion);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnCrear.addActionListener(e -> crearTarea());
        btnEditar.addActionListener(e -> editarTarea());
        btnAvanzar.addActionListener(e -> avanzarEstado());
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new MainGui();
        });

        setVisible(true);
    }

    private void aplicarFiltro() {
        String estadoSeleccionado = (String) filtroEstadoCombo.getSelectedItem();
        List<Tarea> filtradas = obtenerTareasFiltradas(estadoSeleccionado);
        modeloTabla.setTareas(filtradas);
        modeloTabla.fireTableDataChanged();
    }

    private List<Tarea> obtenerTareasFiltradas(String estado) {
        return GestorTareas.getTareas().stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getId() == usuario.getId())
                .filter(t -> estado == null || estado.equals("Todos") || t.getEstado().toString().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    private void crearTarea() {
    JTextField tituloField = new JTextField();
    JTextField descripcionField = new JTextField();
    JTextField fechaInicioField = new JTextField();
    JTextField fechaFinField = new JTextField();
    JComboBox<String> estadoBox = new JComboBox<>(new String[]{"Pendiente", "En Curso", "Completada"});

    Object[] campos = {
            "Título:", tituloField,
            "Descripción:", descripcionField,
            "Fecha de inicio (YYYY-MM-DD):", fechaInicioField,
            "Fecha de fin (YYYY-MM-DD):", fechaFinField,
            "Estado:", estadoBox
    };

    int opcion = JOptionPane.showConfirmDialog(this, campos, "Crear nueva tarea", JOptionPane.OK_CANCEL_OPTION);

    if (opcion == JOptionPane.OK_OPTION) {
        try {
            String titulo = tituloField.getText().trim();
            String descripcion = descripcionField.getText().trim();
            LocalDate inicio = LocalDate.parse(fechaInicioField.getText().trim());
            LocalDate fin = LocalDate.parse(fechaFinField.getText().trim());
            Tarea.Estado estado = Tarea.Estado.valueOf(((String) estadoBox.getSelectedItem()).replace(" ", "_").toUpperCase());

            if (titulo.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            if (fin.isBefore(inicio)) {
                JOptionPane.showMessageDialog(this, "La fecha de fin no puede ser anterior a la de inicio.");
                return;
            }

            Tarea nueva = new Tarea(titulo, descripcion, usuario, inicio, fin);
            nueva.setEstado(estado);
            GestorTareas.agregarTarea(nueva);
            aplicarFiltro();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la creación de tarea: " + e.getMessage());
        }
    }
}
            

    private void editarTarea() {
        int fila = tablaTareas.getSelectedRow();
        if (fila != -1) {
            Tarea tarea = modeloTabla.getTareaEn(fila);

            if (!tarea.getUsuario().equals(usuario)) {
                JOptionPane.showMessageDialog(this, "Solo puedes editar tus propias tareas.");
                return;
            }

            JTextField nombreField = new JTextField(tarea.getTitulo());
            JTextField descripcionField = new JTextField(tarea.getDescripcion());

            Object[] campos = {
                    "Nombre de la tarea:", nombreField,
                    "Descripción:", descripcionField
            };

            int opcion = JOptionPane.showConfirmDialog(this, campos, "Editar tarea", JOptionPane.OK_CANCEL_OPTION);

            if (opcion == JOptionPane.OK_OPTION) {
                tarea.setTitulo(nombreField.getText().trim());
                tarea.setDescripcion(descripcionField.getText().trim());
                GestorTareas.guardarTareas();
                aplicarFiltro();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para editar.");
        }
    }

    private void avanzarEstado() {
        int fila = tablaTareas.getSelectedRow();
        if (fila != -1) {
            Tarea tarea = modeloTabla.getTareaEn(fila);

            if (!tarea.getUsuario().equals(usuario)) {
                JOptionPane.showMessageDialog(this, "Solo puedes modificar tus propias tareas.");
                return;
            }

            tarea.avanzarEstado();
            GestorTareas.guardarTareas();
            aplicarFiltro();
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una tarea para avanzar su estado.");
        }
    }
}
