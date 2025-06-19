import controller.Estacionamento;
import models.*;
import sensor.Sensor;

public class Main {
    public static void main(String[] args) {
        Estacionamento estacionamento = new Estacionamento(5);

        Pessoa visitante = new Visitante("Maria", "123456");
        Veiculo carro = new Veiculo("ABC-1234", "Fiat Uno");

        estacionamento.registrarEntrada(visitante, carro);

        estacionamento.exibirVagasDisponiveis();

        // Simula espera
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        estacionamento.registrarSaida("ABC-1234");

        estacionamento.salvarRegistros("registros.csv");
    }
}
