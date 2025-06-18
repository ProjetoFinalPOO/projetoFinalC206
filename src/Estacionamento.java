import java.util.ArrayList;
import java.util.List;

public class Estacionamento {
    private List<RegistroAcesso> registros = new ArrayList<>();
    private final int limiteVagas;

    public Estacionamento(int limiteVagas) {
        this.limiteVagas = limiteVagas;
    }

    public boolean registrarEntrada(Pessoa pessoa, Veiculo veiculo) {
        if (registros.size() < limiteVagas) {
            registros.add(new RegistroAcesso(pessoa, veiculo));
            return true;
        }
        return false; 
    }

    public void registrarSaida(String placa) {
        for (RegistroAcesso registro : registros) {
            if (registro.getPlaca().equals(placa) && registro.saida == null) {
                registro.registrarSaida();
                double valor = registro.calcularValor();
                System.out.println("Valor a pagar: R$ " + valor);
                break;
            }
        }
    }
}
