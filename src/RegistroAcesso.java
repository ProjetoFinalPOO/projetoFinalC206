import java.time.LocalDateTime;

public class RegistroAcesso {
    private Pessoa pessoa;
    private Veiculo veiculo;
    private LocalDateTime entrada;
    private LocalDateTime saida;

    public RegistroAcesso(Pessoa pessoa, Veiculo veiculo) {
        this.pessoa = pessoa;
        this.veiculo = veiculo;
        this.entrada = LocalDateTime.now();
    }

    public void registrarSaida() {
        this.saida = LocalDateTime.now();
    }

    public double calcularValor() {
        long minutos = java.time.Duration.between(entrada, saida).toMinutes();
        return pessoa.calcularTarifa(minutos);
    }
}
