package models;

public class Funcionario extends Pessoa {
    public Funcionario(String nome, String documento) {
        super(nome, documento);
    }

    @Override
    public double calcularTarifa(long minutos){
        return 0.0; // funcionario nao paga tarifa
    }

}
