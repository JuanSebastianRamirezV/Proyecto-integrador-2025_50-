package controlador;

import modelo.Municipio;
import modelo.MunicipioDAO;
import java.util.List;

public class MunicipioController {
    private MunicipioDAO municipioDAO;

    public MunicipioController() {
        this.municipioDAO = new MunicipioDAO();
    }

    public boolean agregarMunicipio(Municipio municipio) {
        return municipioDAO.insertar(municipio);
    }

    public boolean actualizarMunicipio(Municipio municipio) {
        return municipioDAO.actualizar(municipio);
    }

    public boolean eliminarMunicipio(int idMunicipio) {
        return municipioDAO.eliminar(idMunicipio);
    }

    public Municipio obtenerMunicipio(int idMunicipio) {
        return municipioDAO.obtenerPorId(idMunicipio);
    }

    public List<Municipio> obtenerTodosMunicipios() {
        return municipioDAO.obtenerTodos();
    }

    public List<Municipio> buscarMunicipios(String criterio) {
        return municipioDAO.buscar(criterio);
    }
}