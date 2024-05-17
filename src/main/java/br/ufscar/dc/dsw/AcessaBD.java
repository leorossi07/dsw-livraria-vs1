package br.ufscar.dc.dsw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AcessaBD {

    public static void main(String[] args) {
        Connection con = null;
        try {
            // Setup para uso do banco de dados Apache Derby
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/Livraria";
            con = DriverManager.getConnection(url, "root", "root");

            // Adicionar nova editora "Seguinte"
            String sqlInsertEditora = "INSERT INTO Editora (cnpj, nome) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sqlInsertEditora);
            pstmt.setString(1, "87.557.922/0001-82");
            pstmt.setString(2, "Seguinte");
            pstmt.executeUpdate();

            // Adicionar novo livro "O Dia do Curinga"
            String sqlInsertLivro1 = "INSERT INTO Livro (titulo, autor, ano, preco, editora_id) VALUES (?, ?, ?, ?, (SELECT id FROM Editora WHERE nome = ?))";
            pstmt = con.prepareStatement(sqlInsertLivro1);
            pstmt.setString(1, "O Dia do Curinga");
            pstmt.setString(2, "Jostein Gaarder");
            pstmt.setInt(3, 1996);
            pstmt.setFloat(4, 29.99f);
            pstmt.setString(5, "Seguinte");
            pstmt.executeUpdate();

            // Adicionar novo livro "A Revolução dos Bichos"
            String sqlInsertLivro2 = "INSERT INTO Livro (titulo, autor, ano, preco, editora_id) VALUES (?, ?, ?, ?, (SELECT id FROM Editora WHERE nome = ?))";
            pstmt = con.prepareStatement(sqlInsertLivro2);
            pstmt.setString(1, "A Revolução dos Bichos");
            pstmt.setString(2, "George Orwell");
            pstmt.setInt(3, 2007);
            pstmt.setFloat(4, 23.90f);
            pstmt.setString(5, "Companhia das Letras");
            pstmt.executeUpdate();

            // Imprimir livros ordenados pelo preço
            String sqlSelectLivros = "SELECT l.titulo, l.autor, e.nome AS editora, l.ano, l.preco FROM Livro l JOIN Editora e ON l.editora_id = e.id ORDER BY l.preco";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlSelectLivros);

            while (rs.next()) {
                System.out.print(rs.getString("titulo"));
                System.out.print(", " + rs.getString("autor"));
                System.out.print(", " + rs.getString("editora"));
                System.out.print(", " + rs.getInt("ano"));
                System.out.println(". R$ " + rs.getFloat("preco"));
            }

            rs.close();
            stmt.close();
            pstmt.close();
            con.close();
        } catch (ClassNotFoundException e) {
            System.out.println("A classe do driver de conexão não foi encontrada!");
        } catch (SQLException e) {
            System.out.println("O comando SQL não pode ser executado!");
            e.printStackTrace();
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
