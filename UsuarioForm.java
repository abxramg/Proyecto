import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UsuarioForm extends JDialog {

    private JTextField txtNombre;
    private JTextField txtNickname;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JComboBox<String> comboTipoUsuario;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private Usuario usuarioEdicion;
    private UsuarioFormListener listener;

    public UsuarioForm(Frame owner, UsuarioFormListener listener) {
        super(owner, "Crear usuario", true);
        this.listener = listener;
        inicializarComponentes();
        agregarEventos();
        pack();
        setLocationRelativeTo(owner);
    }

    private void inicializarComponentes() {
        txtNombre = new JTextField(20);
        txtNickname = new JTextField(20);
        txtCorreo = new JTextField(20);
        txtPassword = new JPasswordField(20);
        comboTipoUsuario = new JComboBox<>(new String[]{"Administrador", "Desarrollador", "Invitado"});
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        setLayout(new GridLayout(6, 2, 5, 5));
        add(new JLabel("Nombre:"));
        add(txtNombre);
        add(new JLabel("Nickname:"));
        add(txtNickname);
        add(new JLabel("Correo:"));
        add(txtCorreo);
        add(new JLabel("Contraseña:"));
        add(txtPassword);
        add(new JLabel("Tipo de usuario:"));
        add(comboTipoUsuario);
        add(btnGuardar);
        add(btnCancelar);
    }

    private void agregarEventos() {
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarUsuario() {
        String nombre = txtNombre.getText().trim();
        String nickname = txtNickname.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtPassword.getPassword());
        String tipoUsuario = (String) comboTipoUsuario.getSelectedItem();

        if (nombre.isEmpty() || nickname.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuarioEdicion == null) {
            // Crear nuevo usuario del tipo seleccionado
            switch (tipoUsuario) {
                case "Administrador":
                    usuarioEdicion = new Administrador(nombre, nickname, correo, contrasena);
                    break;
                case "Desarrollador":
                    usuarioEdicion = new Desarrollador(nombre, nickname, correo, contrasena);
                    break;
                case "Invitado":
                    usuarioEdicion = new Invitado(nombre, nickname, correo, contrasena);
                    break;
            }
        } else {
            // Editar usuario existente
            usuarioEdicion.setNombre(nombre);
            usuarioEdicion.setNickname(nickname);
            usuarioEdicion.setCorreo(correo);
            usuarioEdicion.setContrasena(contrasena);
            // El tipo de usuario no se cambia aquí por simplicidad
        }

        listener.usuarioFormGuardado(usuarioEdicion);
        dispose();
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioEdicion = usuario;
        txtNombre.setText(usuario.getNombre());
        txtNickname.setText(usuario.getNickname());
        txtCorreo.setText(usuario.getCorreo());
        txtPassword.setText(usuario.getContrasena());
        comboTipoUsuario.setSelectedItem(usuario.getRol());
    }

    public interface UsuarioFormListener {
        void usuarioFormGuardado(Usuario usuario);
    }
}
