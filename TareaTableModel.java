import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TareaTableModel extends AbstractTableModel {

    private final String[] columnas = {
        "ID", "Descripción", "Estado", "Usuario", "Fecha Estimada Inicio", "Fecha Inicio", 
        "Fecha Estimada Fin", "Fecha Fin"
    };

    private List<Tarea> tareas;

    public TareaTableModel(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
        fireTableDataChanged(); // Actualiza la tabla
    }

    @Override
    public int getRowCount() {
        return tareas.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tarea tarea = tareas.get(rowIndex);
        switch (columnIndex) {
            case 0: return tarea.getId();
            case 1: return tarea.getDescripcion();
            case 2: return tarea.getEstado();
            case 3: return tarea.getUsuario() != null ? tarea.getUsuario().getNickname(): "Sin usuario";
            case 4: return tarea.getFechaEstimadaInicio();
            case 5: return tarea.getFechaInicio();
            case 6: return tarea.getFechaEstimadaFin();
            case 7: return tarea.getFechaFin();
            default: return null;
        }
    }

    public Tarea getTareaEn(int fila) {
        return tareas.get(fila);
    }
}
