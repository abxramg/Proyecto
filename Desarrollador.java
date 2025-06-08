// Clase que representa a un usuario con el rol de Desarrollador

public class Desarrollador extends Usuario {

    /**
     * Constructor que inicializa un nuevo Desarrollador
     * @param nombre
     * @param nickname
     * @param correo
     * @param contrasena
     */
    public Desarrollador(String nombre, String nickname, String correo, String contrasena) {
        super(nombre, nickname, correo, contrasena);
    }

    /** 
     * Devuelve el rol del usuario como cadena.
     * @return "Desarrollador"
     */
    @Override
    public String getRol() {
        return "Desarrollador";
    }
    
}
