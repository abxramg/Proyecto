import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private MainGui mainGui; 

    public LoginPanel(MainGui mainGui) {
        this.mainGui = mainGui; 
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Iniciar sesión"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Usuario:");
        gbc.gridx = 0; gbc.gridy = 0;
        add(lblUsuario, gbc);

        txtUsuario = new JTextField(15);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        JButton btnIniciarSesion = new JButton("Iniciar sesión");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnIniciarSesion, gbc);

        btnIniciarSesion.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());
        Usuario u = GestorUsuarios.autenticar(usuario, contrasena);

        if (u != null) {
            switch (u.getRol()) {
                case "admin":
                    mainGui.mostrarPanel(new AdminPanel(u), "Admin");
                    break;
                case "developer":
                    mainGui.mostrarPanel(new DeveloperPanel(u), "Developer");
                    break;
                case "guest":
                    mainGui.mostrarPanel(new GuestPanel(u),"Guest");
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Rol desconocido.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
        }
    }
}
