package controller;

import java.io.*;
import java.util.*;
import models.*;

public class Estacionamento {
    private List<Vaga> vagas = new ArrayList<>();
    private List<RegistroAcesso> registros = new ArrayList<>();

    public Estacionamento(int totalVagas) {
        for (int i = 1; i <= totalVagas; i++) {
            vagas.add(new Vaga(i));
        }
    }

    public boolean registrarEntrada(Pessoa pessoa, Veiculo veiculo, int numeroVaga) {
        // Verifica se a vaga existe
        if (numeroVaga < 1 || numeroVaga > vagas.size()) {
            return false;
        }
        
        Vaga vaga = vagas.get(numeroVaga - 1); // -1 porque a lista começa em 0
        
        if (!vaga.getEstado()) {
            RegistroAcesso novo = new RegistroAcesso(pessoa, veiculo, numeroVaga);
            vaga.ocuparVaga(novo);
            registros.add(novo);
            return true;
        }
        return false;
    }

    public void registrarSaida(String placa) {
        for (Vaga vaga : vagas) {
            RegistroAcesso registro = vaga.getRegistro();
            if (registro != null && registro.getPlaca().equals(placa) && registro.getSaida() == null) {
                vaga.liberarVaga();
                double valor = registro.calcularValor();
                System.out.println("Saída registrada. Valor a pagar: R$ " + valor);
                return;
            }
        }
        System.out.println("Veículo com placa " + placa + " não encontrado ou já saiu.");
    }

    public void exibirVagasDisponiveis() {
        System.out.println("Vagas Disponíveis:");
        boolean temVagaDisponivel = false;
        
        for (Vaga vaga : vagas) {
            if (!vaga.getEstado()) {
                System.out.println("  Vaga " + vaga.getId() + " - LIVRE");
                temVagaDisponivel = true;
            }
        }
        
        if (!temVagaDisponivel) {
            System.out.println("  Nenhuma vaga disponível no momento.");
        }
    }

    public void salvarRegistros(String caminho) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            writer.println("Nome,Documento,Placa,Modelo,Vaga,Entrada,Saida,Valor");
            for (RegistroAcesso registro : registros) {
                writer.println(registro.getPessoa().getNome() + "," +
                             registro.getPessoa().getDocumento() + "," +
                             registro.getPlaca() + "," +
                             registro.getVeiculo().getModelo() + "," +
                             registro.getId() + "," +
                             registro.getEntrada() + "," +
                             registro.getSaida() + "," +
                             registro.calcularValor());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar registros: " + e.getMessage());
        }
    }
}