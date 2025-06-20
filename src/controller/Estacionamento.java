package controller;

import db.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import models.*;

public class Estacionamento {
    private List<Vaga> vagas = new ArrayList<>();
    private List<RegistroAcesso> registros = new ArrayList<>();
    private DataBase db;

    public Estacionamento(int totalVagas) {
        this.db = new DataBase();
        for (int i = 1; i <= totalVagas; i++) {
            vagas.add(new Vaga(i));
        }

        RegistroDB.criarTabela(db);
    }

    public boolean registrarEntrada(Pessoa pessoa, Veiculo veiculo, int numeroVaga) {
        // Verifica se a vaga existe
        if (numeroVaga < 1 || numeroVaga > vagas.size()) {
            return false;
        }
        
        Vaga vaga = vagas.get(numeroVaga - 1); // -1 porque a lista começa em 0
              if (vaga.getEstado()){
                  return false;

              }
        
        RegistroAcesso registro = new RegistroAcesso(pessoa, veiculo, numeroVaga);
        vaga.ocuparVaga(registro);
        RegistroDB.registrarEntrada(db, registro);
        return true;
       
     
    }

    public double registrarSaida(String placa) {
        for (Vaga vaga : vagas) {
            RegistroAcesso registro = vaga.getRegistro();
            if (registro != null && registro.getPlaca().equals(placa)) {
                try {
                    double valor = RegistroDB.registrarSaida(db, placa);
                    registro.registrarSaida();

                    RegistroDB.registrarSaida(db, placa);
                    vaga.liberarVaga();

                    // Remove o registro do banco de dados
                    boolean removido = RegistroDB.removerRegistro(db, placa);
                    if (!removido) {
                        System.out.println("Aviso: Registro não foi removido do banco de dados");
                    }
                    return valor;

                } catch (SQLException e) {
                    System.out.println("Erro ao registrar saída: "  + e.getMessage());
                    return -1; // Indica erro
                }
            }
        }
        System.out.println("Veículo com placa " + placa + " não encontrado ou já saiu.");
        return -1;
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
                             registro.getSaida() + "," 
                        );
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar registros: " + e.getMessage());
        }
    }


}
