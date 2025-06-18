public abstract class Pessoa {
    protected String nome;
    protected String documento;

    public Pessoa(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }

    public abstract double calcularTarifa(long minutosEstacionado);
}
