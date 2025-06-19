package models;

public class Visitante extends Pessoa {
    // visitante tem que ter uma agregação com carro pois ele pode ter ou nao um carro
    // mas o carro nao pode existir sem um visitante
    public Visitante(String nome, String documento) {
        super(nome, documento);
    }

    @Override
    public double calcularTarifa(long minutos) {
        return minutos * 0.20; // 20 centavos por minuto
    }
}
