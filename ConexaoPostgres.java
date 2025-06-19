import java.sql.*;

public class ConexaoPostgres {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/meubanco";
        String usuario = "admin";
        String senha = "123123";

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conectado com sucesso ao PostgreSQL!");

            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT version()");

            if (rs.next()) {
                System.out.println("Versão do PostgreSQL: " + rs.getString(1));
            }

            conexao.close();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }
}
