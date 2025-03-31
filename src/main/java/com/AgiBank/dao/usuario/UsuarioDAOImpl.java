package com.AgiBank.dao.usuario;

import com.AgiBank.model.Usuario;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class UsuarioDAOImpl implements UsuarioDAO {
    private final String url;
    private final String username;
    private final String password;

    public UsuarioDAOImpl() {
        Dotenv dotenv = Dotenv.load();
        String port = dotenv.get("DATABASE_PORT");
        String baseUrl = dotenv.get("DATABASE_URL");
        this.username = dotenv.get("DATABASE_USERNAME");
        this.password = dotenv.get("DATABASE_PASSWORD");

        this.url = "jdbc:mysql://" + baseUrl + ":" + port + "/aposentarBem";
    }

    @Override
    public boolean criarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nome, dataNascimento, genero, profissao, idadeAposentadoriaDesejada) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setDate(2, java.sql.Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(3, usuario.getGenero().toString());
            stmt.setString(4, usuario.getProfissao().toString());
            stmt.setInt(5, usuario.getIdadeAposentadoriaDesejada());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
            return false;
        }
    }
}
