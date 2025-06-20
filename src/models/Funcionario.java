package models;

public class Funcionario extends Pessoa {
    private int matricula;

    public Funcionario(String nome, String documento, int matricula) {
        super(nome, documento);
        this.matricula = matricula;
    }

    @Override
    public double calcularTaxa(double valor){
        return 0.0; // funcionario nao paga tarifa
    }
    // Getters e Setters
    public int getMatricula() {
        return matricula;
    }

}