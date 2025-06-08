import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.stream.Collectors;
import java.util.List;

public class GuestPanel extends JFrame {

    private JTable tabla;
    private TareaTableModel tableModel;
    private JComboBox<String> filtroEstadoCombo;
    private JComboBox<String> filtroUsuarioCombo;

    public GuestPanel(Usuario usuario) {

        setTitle("Panel de Invitado - Bienvenido " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);


        tableModel = new TareaTableModel(GestorTareas.getTareas());
        tabla = new JTable(tableModel);
        tabla.setEnabled(false); // Solo lectura

        JScrollPane scroll = new JScrollPane(tabla);
        
        //Filtros
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

        filtroEstadoCombo.addActionListener(e -> aplicarFiltros());
        filtroUsuarioCombo.addActionListener(e -> aplicarFiltros());

        JButton cerrarSesion = new JButton("Cerrar Sesión");
        cerrarSesion.addActionListener(e -> {
            dispose();
            new MainGui();
        });

        JPanel panelSur = new JPanel();
        panelSur.add(cerrarSesion);

        setLayout(new BorderLayout());
        add(panelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void aplicarFiltros() {
        String estadoSeleccionado = (String) filtroEstadoCombo.getSelectedItem();
        String usuarioSeleccionado = (String) filtroUsuarioCombo.getSelectedItem();

        List<Tarea> tareasFiltradas = GestorTareas.getTareas().stream()
            .filter(t -> estadoSeleccionado.equals("Todos") || t.getEstado().name().equalsIgnoreCase(estadoSeleccionado))
            .filter(t -> usuarioSeleccionado.equals("Todos") || 
                (t.getUsuario() != null && usuarioSeleccionado.equals(t.getUsuario().getNombre())))
            .collect(Collectors.toList());

        tableModel.setTareas(tareasFiltradas);
        tableModel.fireTableDataChanged();
    }
}
