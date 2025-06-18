public class Visitante extends Pessoa {
    public Visitante(String nome, String documento) {
        super(nome, documento);
    }

    @Override
    public double calcularTarifa(long minutosEstacionado) {
        return minutosEstacionado * 0.10;
    }
}
