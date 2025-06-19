package models;

public class Visitante extends Pessoa {

    public Visitante(String nome, String documento) {
        super(nome, documento);
    }

    @Override
    public double calcularTarifa(long minutos) {
        return minutos * 0.20; // 20 centavos por minuto
    }
}
