package models;

public class Funcionario extends Pessoa {
    private String matricula;

    public Funcionario(String nome, String documento, String matricula) {
        super(nome, documento);
        this.matricula = matricula;
    }

    @Override
    public double calcularTaxa(double valor){
        return 0.0; // funcionario nao paga tarifa
    }
    // Getters e Setters
    public String getMatricula() {
        return matricula;
    }

}