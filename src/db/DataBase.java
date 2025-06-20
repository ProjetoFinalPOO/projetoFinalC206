package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public DataBase() {
        this.url = "jdbc:postgresql://localhost:5432/meubanco"; 
        this.user = "admin";
        this.password = "123123";
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    public void criarTabelas() {
        // Método para criação inicial da tabela
        RegistroDB.criarTabela(this);
    }
}