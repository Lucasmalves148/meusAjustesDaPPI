package model.dao;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Doacao;

public class DoacaoDAO {

    //Antes percorria a lista em Java para calcular estoque e relatório;
    //  agora usa SUM(CASE WHEN...) e GROUP BY MONTH() pra nao ter qusar maps nem loops .



    public void salvar(Doacao d) {
        String sql = "INSERT INTO doacoes (tipo, categoria, quantidade, data_evento) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getTipo());
            ps.setString(2, d.getCategoria());
            ps.setInt(3, d.getQuantidade());
            ps.setDate(4, Date.valueOf(d.getData()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    d.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar doação: " + e.getMessage());
        }
    }

    public List<Doacao> listarTodas() {
        String sql = "SELECT * FROM doacoes ORDER BY data_evento";
        List<Doacao> lista = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(montarDoacao(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar doações: " + e.getMessage());
        }
        return lista;
    }

    public int calcularEstoqueTotal() {
        String sql = "SELECT " +
                "SUM(CASE WHEN tipo = 'ENTRADA' THEN quantidade ELSE -quantidade END) AS total " +
                "FROM doacoes";

        try (Connection conn = ConexaoBanco.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao calcular estoque: " + e.getMessage());
        }
        return 0;
    }

    public List<Object[]> relatorioMensal(int mes, int ano) {
        String sql = "SELECT DAY(data_evento), categoria, SUM(quantidade) AS total " +
                "FROM doacoes " +
                "WHERE MONTH(data_evento) = ? AND YEAR(data_evento) = ? " +
                "GROUP BY DAY(data_evento), categoria";

                
        List<Object[]> resultado = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mes);
            ps.setInt(2, ano);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Object[] {
                            rs.getString("tipo"),
                            rs.getString("categoria"),
                            rs.getInt("total")
                    });
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro no relatório: " + e.getMessage());
        }
        return resultado;
    }

    private Doacao montarDoacao(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        String categoria = rs.getString("categoria");
        int quantidade = rs.getInt("quantidade");
        LocalDate data = rs.getDate("data_evento").toLocalDate();

        Doacao d = new Doacao(tipo, categoria, quantidade, data);
        d.setId(rs.getInt("id"));
        return d;
    }
}