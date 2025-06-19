    package db;

    import java.sql.*;
    import java.time.LocalDateTime;

    public class ManipulacaoDB {

        private final String url = "jdbc:postgresql://localhost:5432/meubanco";
        private final String user = "admin";
        private final String password = "123123";

        public Connection conectar() throws SQLException {
            return DriverManager.getConnection(url, user, password);
        }

        // Criar tabela se não existir
        public void criarTabelaSeNaoExistir() {
            String sql = "CREATE TABLE IF NOT EXISTS registro_acesso (" +
                        "id SERIAL PRIMARY KEY," +
                        "nome VARCHAR(100) NOT NULL," +
                        "cpf VARCHAR(11) NOT NULL," +
                        "funcionario BOOLEAN DEFAULT FALSE," +
                        "placa VARCHAR(10) NOT NULL," +
                        "entrada TIMESTAMP NOT NULL," +
                        "saida TIMESTAMP)";
            
            try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabela verificada/criada com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao criar tabela: " + e.getMessage());
            }
        }

        // CREATE
        public void inserir(String nome, String cpf, String placa, boolean funcionario) {
            String sql = "INSERT INTO registro_acesso (nome, cpf, funcionario, placa, entrada) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setString(2, cpf);
                stmt.setBoolean(3, funcionario); // Definindo como não funcionário
                stmt.setString(4, placa);
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                stmt.executeUpdate();
                System.out.println("Registro inserido com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao inserir: " + e.getMessage());
            }
        }

        // READ
        public void listarTodos() {
            String sql = "SELECT * FROM registro_acesso";
            try (Connection conn = conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                                    ", Nome: " + rs.getString("nome") +
                                    ", CPF: " + rs.getString("cpf") +
                                    ", Funcionário: " + rs.getBoolean("funcionario") +
                                    ", Placa: " + rs.getString("placa") +
                                    ", Entrada: " + rs.getTimestamp("entrada") +
                                    ", Saída: " + rs.getTimestamp("saida"));
                }
            } catch (SQLException e) {
                System.out.println("Erro ao listar: " + e.getMessage());
            }
        }

        // UPDATE (registrar saída)
        public void registrarSaida(int id) {
            String sql = "UPDATE registro_acesso SET saida = ? WHERE id = ?";
            try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setInt(2, id);
                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Saída registrada com sucesso.");
                } else {
                    System.out.println("Registro não encontrado.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao atualizar: " + e.getMessage());
            }
        }

        // DELETE
        public void excluir(int id) {
            String sql = "DELETE FROM registro_acesso WHERE id = ?";
            try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhas = stmt.executeUpdate();
                if (linhas > 0) {
                    System.out.println("Registro excluído com sucesso.");
                } else {
                    System.out.println("Registro não encontrado.");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao excluir: " + e.getMessage());
            }
        }

        // MAIN PARA TESTE
        public static void main(String[] args) {
            ManipulacaoDB db = new ManipulacaoDB();
            db.criarTabelaSeNaoExistir();  // Adicione esta linha

            // Exemplo de uso:
            db.inserir("Maria Oliveira", "98765432100", "XYZ-0001",false);
            db.listarTodos();
            db.registrarSaida(1); // Atualiza saída do ID 1
            db.excluir(1);        // Exclui o registro ID 1
        }
    }
