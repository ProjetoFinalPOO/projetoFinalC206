package controller;

import db.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
                try (Connection conn = db.getConnection()) {
                    String sqlSelect = "SELECT entrada, is_funcionario FROM registros WHERE placa_veiculo = ? AND saida IS NULL";
                    String sqlDelete = "DELETE FROM registros WHERE placa_veiculo = ?";

                    LocalDateTime entrada;
                    boolean isFuncionario;

                    // Busca os dados necessários antes de apagar
                    try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
                        stmtSelect.setString(1, placa);
                        ResultSet rs = stmtSelect.executeQuery();
                        if (rs.next()) {
                            entrada = rs.getTimestamp("entrada").toLocalDateTime();
                            isFuncionario = rs.getBoolean("is_funcionario");
                        } else {
                            throw new SQLException("Veiculo com placa " + placa + " nao encontrado ou ja registrou saida.");
                        }
                    }

                    // Apaga o registro
                    try (PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {
                        stmtDelete.setString(1, placa);
                        stmtDelete.executeUpdate();
                    }

                    registro.registrarSaida();
                    vaga.liberarVaga();

                    // Calcula e retorna o valor
                    if (isFuncionario) {
                        System.out.println("Saida para funcionario (isento). Placa: " + placa);
                        return 0.0;
                    }
                    long segundos = java.time.Duration.between(entrada, LocalDateTime.now()).toSeconds();
                    double valor = segundos * 0.05; // Sua lógica de tarifa

                    System.out.println("Saida registrada. Valor: R$ " + String.format("%.2f", valor));
                    return valor;

                } catch (SQLException e) {
                    System.out.println("Erro ao registrar saida: " + e.getMessage());
                    return -1;
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
