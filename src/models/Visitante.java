package models;

public class Visitante extends Pessoa implements Tarifa {
    // visitante tem que ter uma agregação com carro pois ele pode ter ou nao um carro
    // mas o carro nao pode existir sem um visitante
    public Visitante(String nome, String documento) {
        super(nome, documento);
    }

    @Override
    public double calcularTaxa(double valor) {
        return valor; // 20 centavos por minuto
    }
    
    @Override
    public double calcularValorEstacionamento(long minutos) {
        float taxaPorMinuto = 0.20f; // 20 centavos por minuto
        if (minutos < 0) {
            throw new IllegalArgumentException("Minutos não podem ser negativos.");
        }
        if (minutos == 0) {
            return 0.0; // Se não houve tempo de estacionamento, não há taxa
        }
        // Calcula o valor total com base nos minutos
        return minutos * 0.20; // 20 centavos por minuto
    }
}
