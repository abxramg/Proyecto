import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class GestorUsuarios {


    // Lista principal de usuarios.
    private static List<Usuario> usuarios = new ArrayList<>();

    // Archivo donde se guardan los datos.
    private static final String archivo = "usuarios.ser";

    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    static {
    cargarUsuarios();
}



    public static Usuario autenticar(String identificador, String contrasena) {
        Usuario u = buscarPorCorreoONick(identificador);

        if ( u != null && u.getContrasena().equals(contrasena)) {
            return u;
        }
        return null;
    }

    public static void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        guardarUsuarios();
    }

    public static Usuario buscarPorCorreoONick (String correoONick) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correoONick) || u.getNickname().equalsIgnoreCase(correoONick)) {
                return u;
            }
        }
            return null;
    }

    public static boolean registrarUsuario(Usuario nuevo) {
    for (Usuario u : usuarios) {
        if (u.getCorreo().equalsIgnoreCase(nuevo.getCorreo())) {
            JOptionPane.showMessageDialog(null, "Ese correo ya está registrado.");
            return false;
        }
        if (u.getNombre().equalsIgnoreCase(nuevo.getNombre())) {
            JOptionPane.showMessageDialog(null, "Ese nombre de usuario ya existe.");
            return false;
        }

        if (u.getNickname().equalsIgnoreCase(nuevo.getNickname())) {
            JOptionPane.showMessageDialog(null, "Ese nickname ya está registrado.");
            return false;
        }
    }

        if (!nuevo.getCorreo().matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(null, "Correo inválido. Debe tener formato nombre@dominio.com");
            return false;
    }

    usuarios.add(nuevo);
    guardarUsuarios();
    return true;
}
        
    private static void guardarUsuarios() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(usuarios);
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    public static void eliminarUsuario(Usuario usuario) {
    usuarios.remove(usuario);
    guardarUsuarios();
}


    @SuppressWarnings("unchecked")
    private static void cargarUsuarios() {
        File f = new File(archivo);
        if (f.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
                usuarios = (List<Usuario>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error al cargar usuarios: " + e.getMessage());
                usuarios = new ArrayList<>();
            }
        } else {
            usuarios = new ArrayList<>();
    }

}

}
