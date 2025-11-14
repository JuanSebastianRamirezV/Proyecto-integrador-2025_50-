package controlador;

import modelo.RelacionAsociados;
import modelo.RelacionAsociadosDAO;
import java.util.List;

public class RelacionAsociadosController {
    
    private RelacionAsociadosDAO relacionDAO;

    public RelacionAsociadosController() {
        this.relacionDAO = new RelacionAsociadosDAO();
    }

    public boolean agregarRelacion(RelacionAsociados relacion) {
        return relacionDAO.insertar(relacion);
    }

    public boolean actualizarRelacion(RelacionAsociados relacion) {
        return relacionDAO.actualizar(relacion);
    }

    public boolean eliminarRelacion(int idPlaga, int idCultivo, int idInspeccion, int idLugarProduccion) {
        return relacionDAO.eliminar(idPlaga, idCultivo, idInspeccion, idLugarProduccion);
    }

    public List<RelacionAsociados> obtenerTodasRelaciones() {
        return relacionDAO.obtenerTodasRelaciones();
    }

    public List<RelacionAsociados> buscarRelaciones(String criterio) {
        return relacionDAO.buscarRelaciones(criterio);
    }

    public boolean existeRelacion(int idPlaga, int idCultivo, int idInspeccion, int idLugarProduccion) {
        return relacionDAO.existeRelacion(idPlaga, idCultivo, idInspeccion, idLugarProduccion);
    }

    public RelacionAsociados obtenerRelacionPorIds(int idPlaga, int idCultivo, int idInspeccion, int idLugarProduccion) {
        return relacionDAO.obtenerPorIds(idPlaga, idCultivo, idInspeccion, idLugarProduccion);
    }
}