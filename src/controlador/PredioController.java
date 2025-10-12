package controlador;

import modelo.Predio;
import modelo.PredioDAO;
import java.util.List;

public class PredioController {
    private PredioDAO predioDAO;

    public PredioController() {
        this.predioDAO = new PredioDAO();
    }

    public boolean agregarPredio(Predio predio) {
        return predioDAO.insertar(predio);
    }

    public boolean actualizarPredio(Predio predio) {
        return predioDAO.actualizar(predio);
    }

    public boolean eliminarPredio(int idPredio) {
        return predioDAO.eliminar(idPredio);
    }

    public Predio obtenerPredio(int idPredio) {
        return predioDAO.obtenerPorId(idPredio);
    }

    public List<Predio> obtenerTodosPredios() {
        return predioDAO.obtenerTodos();
    }

    public List<Predio> buscarPredios(String criterio) {
        return predioDAO.buscar(criterio);
    }
}