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
    // Getters
    public boolean isFuncionario() {
        return pessoa instanceof Funcionario;
    }

    public void registrarSaida() {
        this.saida = LocalDateTime.now();
    }
    public double calcularValor() {
        if (saida == null) {
            return 0; // Saída não registrada
        }
        
        long minutosNoEstacionamento = Duration.between(entrada, saida).toMinutes();
        if (minutosNoEstacionamento < 0) {
            return 0; // Saída antes da entrada
        }
        else if (minutosNoEstacionamento == 0) {
            return 0; // Veículo saiu no mesmo minuto
        }
        else if (minutosNoEstacionamento <= 15) {
            return 0; // Primeiros 15 minutos são gratuitos
        }
        long horas = minutosNoEstacionamento / 60;
      
        
        if (pessoa instanceof Funcionario) {
            return 0; // Funcionários não pagam
        } 
        else if (pessoa instanceof Visitante) {
            return ((Visitante)pessoa).calcularValorEstacionamento(horas);
        }
        return 0;
    }

 
    
}
