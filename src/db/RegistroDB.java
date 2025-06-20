package db;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import models.Funcionario;
import models.Pessoa;
import models.Veiculo;
import models.Visitante;
import DataBase.DataBase;
import models.RegistroAcesso;

public class RegistroDB {

    public static void criarTabela(DataBase db) {
        String sql = "CREATE TABLE IF NOT EXISTS registros ("
                + "id SERIAL PRIMARY KEY,"
                + "nome VARCHAR(100) NOT NULL,"
                + "documento VARCHAR(20) NOT NULL,"
                + "is_funcionario BOOLEAN NOT NULL DEFAULT FALSE,"
                + "matricula VARCHAR,"
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

    public static void registrarEntrada(DataBase db, RegistroAcesso registro) {
        String sql = "INSERT INTO registros "
                + "(nome, documento, is_funcionario, matricula, placa_veiculo, modelo, vaga, entrada) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, registro.getPessoa().getNome());
            stmt.setString(2, registro.getPessoa().getDocumento());
            stmt.setBoolean(3, registro.getPessoa() instanceof Funcionario);

            if (registro.getPessoa() instanceof Funcionario) {
                stmt.setString(4, ((Funcionario) registro.getPessoa()).getMatricula());
          
            } else {
                stmt.setNull(4, Types.INTEGER); // Correção aqui
            }

            stmt.setString(5, registro.getVeiculo().getPlaca());
            stmt.setString(6, registro.getVeiculo().getModelo());
            stmt.setInt(7, registro.getId());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

            stmt.executeUpdate();
            System.out.println("Entrada registrada com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao registrar entrada: " + e.getMessage());
        }
    }

    public static double registrarSaida(DataBase db, String placa) throws SQLException {
        //   
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
                throw new SQLException("Nenhum veículo encontrado ou já saiu.");
            } 
            return valor;
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

                long segundos = java.time.Duration.between(entrada, saida).toSeconds();

           
                return segundos * 0.05; 
            }
            throw new SQLException("Veículo não encontrado");
        }
    }
    public static boolean removerRegistro(DataBase db, String placa) throws SQLException {
        String sql = "DELETE FROM registros WHERE placa_veiculo = ?";

        try (Connection conn = db.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);
            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;
        }
    }

    public static void testarConexaoETabela(DataBase db) {
        criarTabela(db);

        // Criando visitante fictício para teste
        Pessoa visitante = new Visitante("Carlos Teste", "123456789");
        Veiculo veiculo = new Veiculo("ABC-1234", "Civic");
        RegistroAcesso registro = new RegistroAcesso(visitante, veiculo, 1);

        registrarEntrada(db, registro);
    }

}
