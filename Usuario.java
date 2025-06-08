import java.io.Serializable;
import java.util.List;

/** 
 * Clase abstracta que representa un usuario dentro del sistema.
 * La clase define los atributos para todos los tipos de usuario.
*/

// Implementa Serializable para permitir la persistencia en archivos binarios.
public abstract class Usuario implements Serializable {

    /** 
     * Contador estático para generar un ID único para cada usuario.
     * Incrementa automáticamente cada que se crea un usuario nuevo.
    */
    private static int contadorID = 1;
    
    // Atributos principales de un usuario.
    protected int id;
    protected String nombre;
    protected String nickname;
    protected String correo;
    protected String contrasena;

        /** 
         * Constructor para crear un nuevo usuario. 
         * @param nombre Nombre real del usuario
         * @param nickname Apodo del usuario
         * @param correo Correo eléctronico 
         * @param contrasena Contraseña para autenticar al inicio de sesión
        */
        public Usuario(String nombre, String nickname, String correo, String contrasena) {
            this.id = contadorID++;
            this.nombre = nombre;
            this.nickname = nickname;
            this.correo = correo;
            this.contrasena = contrasena;
        }

    // Métodos getter y setter para acceder y modificar los atributos privados.

        public int getId() {
            return id; 
        }

        public String getNombre() {
            return nombre;
        }

        public String getNickname() {
            return nickname;
        }

        public String getCorreo() {
            return correo;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }

    // Método abstracto para implementar el rol del usuario 
        public abstract String getRol();
    
        //,
public class Administrador extends Usuario {
    public Administrador(String nombre, String nickname, String correo, String contrasena) {
        super(nombre, nickname, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "Administrador";
    }
}

public class Desarrollador extends Usuario {
    public Desarrollador(String nombre, String nickname, String correo, String contrasena) {
        super(nombre, nickname, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "Desarrollador";
    }
}

public class Invitado extends Usuario {
    public Invitado(String nombre, String nickname, String correo, String contrasena) {
        super(nombre, nickname, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "Invitado";
    }
}
//,

    /** 
     * Devuelve una representación amigable del usuario.
     * Incluye su rol, nombre y nickname.
     */
    @Override
    public String toString() {
        return "[" + getRol() + "]" + nombre + "(" + nickname + ")";
    }


    /**
     * Actualiza el contador que se utiliza para generar el ID único de cada usuario.
     * @param usuarios la lista de usuarios registrados.
     */
    public static void actualizarContadorID(List<Usuario> usuarios) {
        // No es necesario buscar el número más grande pues no existe una forma de borrar usuarios.
        contadorID = usuarios.size() + 1;
    }
    
}
