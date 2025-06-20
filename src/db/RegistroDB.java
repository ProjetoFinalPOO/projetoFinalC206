package db;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import models.Funcionario;
import models.Pessoa;
import models.Veiculo;
import models.Visitante;
import DataBase.DataBase;

public class RegistroDB {

    public static void criarTabela(DataBase db) {
        String sql = "CREATE TABLE IF NOT EXISTS registros ("
                + "id SERIAL PRIMARY KEY,"
                + "nome VARCHAR(100) NOT NULL,"
                + "documento VARCHAR(20) NOT NULL,"
                + "is_funcionario BOOLEAN NOT NULL DEFAULT FALSE,"
                + "matricula VARCHAR NOT NULL,"
                + "placa_veiculo VARCHAR(10) NOT NULL,"
                + "modelo VARCHAR(50) NOT NULL,"
                + "vaga INTEGER NOT NULL,"
                + "entrada TIMESTAMP NOT NULL,"
                + "saida TIMESTAMP,"
                + "valor NUMERIC(10,2))";

        executarSQL(db, sql, "Tabela 'registros' criada");
    }

    private static void executarSQL(DataBase db, String sql, String mensagemSucesso) {
        try (Connection conn = db.getConnection(); Statement stmt = conn.createStatement()) {

            System.out.println("Conexão estabelecida: " + !conn.isClosed());
            stmt.executeUpdate(sql);
            System.out.println(mensagemSucesso);

        } catch (SQLException e) {
            System.err.println("Erro ao executar SQL: " + e.getMessage());
        }
    }

    public static void registrarEntrada(DataBase db, Pessoa pessoa, Veiculo veiculo, int vaga) {
        String sql = "INSERT INTO registros "
                + "(nome, documento, is_funcionario, matricula, placa_veiculo, modelo, vaga, entrada) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getDocumento());
            stmt.setBoolean(3, pessoa instanceof Funcionario);

            if (pessoa instanceof Funcionario) {
                stmt.setInt(4, ((Funcionario) pessoa).getMatricula());
            } else {
                stmt.setNull(4, Types.INTEGER); // Correção aqui
            }

            stmt.setString(5, veiculo.getPlaca());
            stmt.setString(6, veiculo.getModelo());
            stmt.setInt(7, vaga);
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();
            System.out.println("Entrada registrada com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao registrar entrada: " + e.getMessage());
        }
    }

    public static void registrarSaida(DataBase db, String placa) throws SQLException {
        String sql = "UPDATE registros SET saida = ?, valor = ? "
                + "WHERE placa_veiculo = ? AND saida IS NULL";

        try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalDateTime saida = LocalDateTime.now();
            stmt.setTimestamp(1, Timestamp.valueOf(saida));

            // Cálculo simplificado do valor - pode ser aprimorado
            double valor = calcularValor(db, placa, saida);
            stmt.setDouble(2, valor);

            stmt.setString(3, placa);

            int linhas = stmt.executeUpdate();
            if (linhas == 0) {
                System.out.println("Nenhum veículo encontrado ou já saiu.");
            } else {
                System.out.println("Saída registrada. Valor: R$ " + valor);
            }
        }
    }

    private static double calcularValor(DataBase db, String placa, LocalDateTime saida) throws SQLException {
        String sql = "SELECT entrada, is_funcionario FROM registros "
                + "WHERE placa_veiculo = ? AND saida IS NULL";

        try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getBoolean("is_funcionario")) {
                    return 0.0; // Funcionários são isentos
                }
                if (saida == null) {
                    return 0; // Saída não registrada
                }
                LocalDateTime entrada = rs.getTimestamp("entrada").toLocalDateTime();

                long minutos = java.time.Duration.between(entrada, saida).toMinutes();

                if (minutos < 0) {
                    return 0; // Saída antes da entrada
                } else if (minutos == 0) {
                    return 0; // Veículo saiu no mesmo minuto
                } else if (minutos <= 15) {
                    return 0; // Primeiros 15 minutos são gratuitos
                }

                return minutos * 0.20; // R$ 5,00 por hora
            }
            throw new SQLException("Veículo não encontrado");
        }
    }

    public static void testarConexaoETabela(DataBase db) {
        criarTabela(db);

        // Criando visitante fictício para teste
        Pessoa visitante = new Visitante("Carlos Teste", "123456789");
        Veiculo veiculo = new Veiculo("ABC-1234", "Civic");

        registrarEntrada(db, visitante, veiculo, 1);
    }

}
