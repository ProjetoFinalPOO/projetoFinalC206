
public class Funcionario extends Pessoa {
    public Funcionario(String nome, String documento) {
        super(nome, documento);
    }

    @Override
    public double calcularTarifa(long minutosEstacionado) {
        return minutosEstacionado * 0.05; // tarifa reduzida
    }
}
