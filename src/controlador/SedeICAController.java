package controlador;

import modelo.SedeICA;
import modelo.SedeICADAO;
import java.util.List;

public class SedeICAController {
    private SedeICADAO sedeICADAO;

    public SedeICAController() {
        this.sedeICADAO = new SedeICADAO();
    }

    public boolean agregarSedeICA(SedeICA sede) {
        return sedeICADAO.insertar(sede);
    }

    public boolean actualizarSedeICA(SedeICA sede) {
        return sedeICADAO.actualizar(sede);
    }

    public boolean eliminarSedeICA(int idSede) {
        return sedeICADAO.eliminar(idSede);
    }

    public SedeICA obtenerSedeICA(int idSede) {
        return sedeICADAO.obtenerPorId(idSede);
    }

    public List<SedeICA> obtenerTodasSedesICA() {
        return sedeICADAO.obtenerTodos();
    }

    public List<SedeICA> buscarSedesICA(String criterio) {
        return sedeICADAO.buscar(criterio);
    }
    public boolean existeSedeICA(String correoElectronico) {
        return sedeICADAO.existeCorreoElectronico(correoElectronico);
    }
    
    public boolean existeSedeICAPorCorreo(String correoElectronico) {
        return sedeICADAO.existeCorreoElectronico(correoElectronico);
    }

    public boolean existeSedeICAPorTelefono(String telefono) {
        return sedeICADAO.existeTelefono(telefono);
    }
}
