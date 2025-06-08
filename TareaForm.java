import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TareaForm extends JDialog {
    private JTextField tituloField; 
    private JTextField descripcionField;
    private JComboBox<Usuario> usuarioCombo;
    private JTextField fechaInicioEstimadaField;
    private JTextField fechaFinEstimadaField;
    private JComboBox<String> estadoCombo;

    private boolean esNueva;
    private Tarea tarea;

    public TareaForm(Tarea tarea, Usuario u2, boolean esNueva) {
        this.esNueva = esNueva;
        this.tarea = tarea;

        setTitle(esNueva ? "Crear Tarea" : "Editar Tarea");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new GridLayout(0, 2, 5, 5));

        add(new JLabel("Título:"));
        tituloField = new JTextField(); 
        add(tituloField);

        add(new JLabel("Descripción:"));
        descripcionField = new JTextField();
        add(descripcionField);

        add(new JLabel("Usuario:"));
        usuarioCombo = new JComboBox<>();
        for (Usuario u : GestorUsuarios.getUsuarios()) {
            usuarioCombo.addItem(u);
        }
        if (usuarioCombo.getItemCount() > 0) {
        usuarioCombo.setSelectedIndex(0);  // Selecciona al menos un usuario
        }

        add(usuarioCombo);

        add(new JLabel("Estado:"));
        estadoCombo = new JComboBox<>(new String[]{"Pendiente", "En Curso", "Completada"});
        add(estadoCombo);

        add(new JLabel("Fecha estimada de inicio (YYYY-MM-DD):"));
        fechaInicioEstimadaField = new JTextField();
        add(fechaInicioEstimadaField);

        add(new JLabel("Fecha estimada de fin (YYYY-MM-DD):"));
        fechaFinEstimadaField = new JTextField();
        add(fechaFinEstimadaField);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        add(btnGuardar);
        add(btnCancelar);

        // Si está editando, rellenar campos
        if (!esNueva && tarea != null) {
            tituloField.setText(tarea.getTitulo());
            descripcionField.setText(tarea.getDescripcion());
            usuarioCombo.setSelectedItem(tarea.getUsuario());
            estadoCombo.setSelectedItem(tarea.getEstado());
            
            if (tarea.getFechaEstimadaInicio() != null)
            fechaInicioEstimadaField.setText(tarea.getFechaEstimadaInicio().toString());

            if (tarea.getFechaEstimadaFin() != null)
            fechaFinEstimadaField.setText(tarea.getFechaEstimadaFin().toString());
        }

        btnGuardar.addActionListener(e -> guardarTarea());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void guardarTarea() {
        try {
            String titulo = tituloField.getText().trim();
            String descripcion = descripcionField.getText().trim();
            String estadoStr = (String) estadoCombo.getSelectedItem();
            Usuario usuario = (Usuario) usuarioCombo.getSelectedItem();
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar un usuario.");
                return;
            }
            Tarea.Estado estado = Tarea.Estado.valueOf(estadoStr.replace(" ", "_").toUpperCase());
            LocalDate fechaInicioEstimada = LocalDate.parse(fechaInicioEstimadaField.getText().trim());
            LocalDate fechaFinEstimada = LocalDate.parse(fechaFinEstimadaField.getText().trim());

            if (descripcion.isBlank()) {
                JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.");
                return;
            }

            if (fechaFinEstimada.isBefore(fechaInicioEstimada)) {
                JOptionPane.showMessageDialog(this, "La fecha de fin no puede ser antes que la de inicio.");
                return;
            }

            if (esNueva) {
                Tarea nueva = new Tarea(titulo, descripcion, usuario, fechaInicioEstimada, fechaFinEstimada);
                nueva.setEstado(estado);
                GestorTareas.agregarTarea(nueva);
            } else {
                tarea.setTitulo(titulo);
                tarea.setDescripcion(descripcion);
                tarea.setUsuario(usuario);
                tarea.setEstado(estado);
                tarea.setFechaEstimadaInicio(fechaInicioEstimada);
                tarea.setFechaEstimadaFin(fechaFinEstimada);

            }

            GestorTareas.guardarTareas();
            dispose();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa YYYY-MM-DD.");
        } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, "Estado inválido. Usa un valor permitido.");
    }
}

}
