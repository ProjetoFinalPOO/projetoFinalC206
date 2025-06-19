package controller;

import models.*;
import sensor.Sensor;

import java.io.*;
import java.util.*;

public class Estacionamento {
    private List<Vaga> vagas = new ArrayList<>();
    private List<RegistroAcesso> registros = new ArrayList<>();

    public Estacionamento(int totalVagas) {
        for (int i = 1; i <= totalVagas; i++) {
            vagas.add(new Vaga(i));
        }
    }

    public boolean registrarEntrada(Pessoa pessoa, Veiculo veiculo) {
        for (Vaga vaga : vagas) {
            if (!vaga.getEstado()) {
                RegistroAcesso novo = new RegistroAcesso(pessoa, veiculo, vaga.getId());
                vaga.ocuparVaga(novo);
                registros.add(novo);
                return true;
            }
        }
        return false;
    }

    public void registrarSaida(String placa) {
        for (Vaga vaga : vagas) {
            RegistroAcesso r = vaga.getRegistro();
            if (r != null && r.getPlaca().equals(placa) && r.getSaida() == null) {
                vaga.liberarVaga();
                double valor = r.calcularValor();
                System.out.println("Saída registrada. Valor a pagar: R$ " + valor);
                return;
            }
        }
    }

    public void exibirVagasDisponiveis() {
        for (Vaga vaga : vagas) {
            if (!vaga.getEstado()) {
                System.out.println("Vaga " + vaga.getId() + " está LIVRE");
            }
        }
    }

    public void salvarRegistros(String caminho) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            writer.println("Nome,Documento,Placa,Modelo,Vaga,Entrada,Saida,Valor");
            for (RegistroAcesso r : registros) {
                writer.println(r.getPessoa().getNome() + "," +
                               r.getPessoa().getDocumento() + "," +
                               r.getPlaca() + "," +
                               r.getVeiculo().getModelo() + "," +
                               r.getIdVaga() + "," +
                               r.getEntrada() + "," +
                               r.getSaida() + "," +
                               r.calcularValor());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
