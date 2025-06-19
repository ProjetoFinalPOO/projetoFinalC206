package models;

public abstract class Pessoa implements Tarifa {

    private String nome;
    private String documento;

    public Pessoa(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }
    // Getters e Setters

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
    // Método abstrato para calcular tarifa
    public abstract double calcularTarifa(long minutos); 
}
