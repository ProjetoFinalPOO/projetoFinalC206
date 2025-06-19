package models;
import java.time.Duration;
import java.time.LocalDateTime;

public class RegistroAcesso {
    
    private int id;
    private Pessoa pessoa;
    private Veiculo veiculo;
    private LocalDateTime entrada;
    private LocalDateTime saida;

    public RegistroAcesso(Pessoa pessoa, Veiculo veiculo, int idVaga) {
        this.id = idVaga;
        this.pessoa = pessoa;
        this.veiculo = veiculo;
        this.entrada = LocalDateTime.now();
    }
    public String getPlaca(){
        return this.veiculo.getPlaca();
    }
    public Pessoa getPessoa(){
        return this.pessoa;
    }
    public LocalDateTime getSaida() {
        return this.saida;
    }
    public LocalDateTime getEntrada() {
        return this.entrada;
    }
    public Veiculo getVeiculo() {
        return veiculo;
    }
    public int getId() {
        return id;
    }
    public void registrarSaida() {
        this.saida = LocalDateTime.now();
    }
  
    public double calcularValor() {
        if(saida == null){
            System.out.println("Saída não registrada para o veículo com placa: " + veiculo.getPlaca());
            return 0.0; // Saída não registrada
        }
        // Verificar calculo de tarifa
        long minutos = Duration.between(entrada, saida).toMinutes();
        return pessoa.calcularTarifa(minutos);
    }
}
