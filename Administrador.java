// Clase que representa a un usuario con el rol de Administrador 

public class Administrador extends Usuario {

    /** 
     * Constructor que inicializa un nuevo Administrador con los datos que se proporcionen.
     * @param nombre Nombre real del Administrador
     * @param nickname Apodo del usuario
     * @param correo Correo electrónico
     * @param contrasena Contraseña del Administrador
     */
    public Administrador(String nombre, String nickname, String correo, String contrasena) {
        super(nombre, nickname, correo, contrasena);
    }

    /** 
     * Devuelve el rol del usuario como cadena.
     * @return "Administrador"
     */
    @Override
    public String getRol() {
        return "Administrador";
    }

}
