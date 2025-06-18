import java.util.ArrayList;
import java.util.List;

public class Estacionamento {
    private List<RegistroAcesso> registros = new ArrayList<>();
    private final int limiteVagas;

    public Estacionamento(int limiteVagas) {
        this.limiteVagas = limiteVagas;
    }

    public boolean registrarEntrada(Pessoa p, Veiculo v) {
        if (registros.size() < limiteVagas) {
            registros.add(new RegistroAcesso(p, v));
            return true;
        }
        return false; // estacionamento cheio
    }

    public void registrarSaida(String placa) {
        for (RegistroAcesso r : registros) {
            if (r.veiculo.getPlaca().equals(placa) && r.saida == null) {
                r.registrarSaida();
                double valor = r.calcularValor();
                System.out.println("Valor a pagar: R$ " + valor);
                break;
            }
        }
    }
}
