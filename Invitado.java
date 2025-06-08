// Clase que representa a un usuario con el rol de Invitado

public class Invitado extends Usuario {

    /** 
     * Constructor para crear un nuevo Invitado
     * @param nombre Nombre real del Invitado
     * @param nickname Apodo del usuario
     * @param correo Correo electrónico
     * @param contrasena Contraseña del Invitado
     */
    public Invitado(String nombre, String nickname, String correo, String contrasena) {
        super(nombre, nickname, correo, contrasena);
    }

    /** 
     * Devuelve el rol del usuario como cadena.
     * @return "Invitado"
     */
    @Override
    public String getRol() {
        return "Invitado";
    }

}