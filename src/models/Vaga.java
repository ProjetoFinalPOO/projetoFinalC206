package models;

public class Vaga {

    private int id;
    private boolean estado;
    private RegistroAcesso registro;

    public Vaga(int id) {
        this.id = id;
        this.estado = false; // false = livre, true = ocupada
    }

    public int getId() {
        return id;
    }

    public boolean getEstado() {
        return estado;
    }
    // Put vaga
    public void OcuparVaga(RegistroAcesso registro) {
        this.estado = true;
        this.registro = registro;
    }

    public void LiberarVaga() {
        this.estado = false;
        if (registro != null) {
            registro.registrarSaida();
        }
    }
    public RegistroAcesso getRegistro() {
        return registro;
    }
}
