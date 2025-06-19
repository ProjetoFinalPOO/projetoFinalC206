package models;
import java.time.LocalDateTime;
import java.time.Duration;

public class RegistroAcesso {
    
    private Pessoa pessoa;
    private Veiculo veiculo;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private int id;

    public RegistroAcesso(Pessoa pessoa, Veiculo veiculo, int idVaga) {
        this.pessoa = pessoa;
        this.veiculo = veiculo;
        this.id = idVaga;
        this.entrada = LocalDateTime.now();
    }
    public String getPlaca(){
        return this.veiculo.getPlaca();
    }
    public String getPessoa(){
        return this.pessoa.getNome();
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
