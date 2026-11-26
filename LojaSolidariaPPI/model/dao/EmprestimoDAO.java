package model.dao;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import ConexaoBanco;
import model.Beneficiario;
import model.Emprestimo;

public class EmprestimoDAO {

    // Antes salvava o CPF solto no CSV e o converterLinha descartava linhas órfãs
    //  agora usa JOIN com beneficiarios, UPDATE para devolução e DATEDIFF(CURDATE(), ...) pra empréstimos atrasados.



    // ------ ESTOQUE DE EMPRÉSTIMOS ------

    public Map<String, Integer> lerEstoqueEmprestimos() {
        String sql = "SELECT categoria, quantidade FROM estoque_emprestimos";
        Map<String, Integer> mapa = new HashMap<>();

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                mapa.put(rs.getString("categoria"), rs.getInt("quantidade"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao ler estoque de empréstimos: " + e.getMessage());
        }
        return mapa;
    }

    public void atualizarEstoqueEmprestimo(String categoria, int quantidade) {
        String sql = "UPDATE estoque_emprestimos SET quantidade = ? WHERE categoria = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantidade);
            ps.setString(2, categoria);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar estoque de empréstimos: " + e.getMessage());
        }
    }

    // ------ EMPRÉSTIMOS ------

    public void registrarNovoEmprestimo(Emprestimo e) {
        String sql = "INSERT INTO emprestimos " +
                     "(beneficiario_id, categoria, quantidade, emprestado, data_evento) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, e.getBeneficiario().getId());
            ps.setString(2, e.getCategoria());
            ps.setInt(3, e.getQuantidade());
            ps.setBoolean(4, e.estaEmprestado());
            ps.setDate(5, Date.valueOf(e.getData()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }

        } catch (SQLException ex) {
            System.out.println("Erro ao registrar empréstimo: " + ex.getMessage());
        }
    }

    public List<Emprestimo> listarEmprestimos() {
        String sql = "SELECT e.*, b.nome, b.cpf, b.telefone " +
                     "FROM emprestimos e " +
                     "JOIN beneficiarios b ON b.id = e.beneficiario_id " +
                     "ORDER BY e.data_evento";

        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(montarEmprestimo(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar empréstimos: " + e.getMessage());
        }
        return lista;
    }

    public boolean possuiEmprestimoAtivo(String cpf) {
        String sql = "SELECT COUNT(*) FROM emprestimos e " +
                     "JOIN beneficiarios b ON b.id = e.beneficiario_id " +
                     "WHERE b.cpf = ? AND e.emprestado = TRUE";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar empréstimo ativo: " + e.getMessage());
        }
        return false;
    }

    public void registrarDevolucao(String cpf) {
        String sql = "UPDATE emprestimos e " +
                     "JOIN beneficiarios b ON b.id = e.beneficiario_id " +
                     "SET e.emprestado = FALSE " +
                     "WHERE b.cpf = ? AND e.emprestado = TRUE";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);
            int linhas = ps.executeUpdate();

            if (linhas > 0) {
                System.out.println("✓ Devolução registrada!");
            } else {
                System.out.println("CPF não encontrado ou empréstimo já devolvido.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao registrar devolução: " + e.getMessage());
        }
    }

    // Empréstimos ativos há mais de 10 dias
    public List<Emprestimo> emprestimosAcimaDe10Dias() {
        String sql = "SELECT e.*, b.nome, b.cpf, b.telefone " +
                     "FROM emprestimos e " +
                     "JOIN beneficiarios b ON b.id = e.beneficiario_id " +
                     "WHERE e.emprestado = TRUE " +
                     "AND DATEDIFF(CURDATE(), e.data_evento) > 10";

        List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(montarEmprestimo(rs));

        } catch (SQLException e) {
            System.out.println("Erro ao buscar empréstimos antigos: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Emprestimo> devolucoesProximas(){

        String sql = "SELECT COUNT(*)" +
                     "FROM emprestimos e" +
                     "WHERE DATEDIFF(CURDATE(), e.data_evento) <= 3";

            List<Emprestimo> lista = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(montarEmprestimo(rs));

        } catch (SQLException e) {
            System.out.println("Erro ao buscar devoluções próximas: " + e.getMessage());
        }
        return lista;
            
    }

    private Emprestimo montarEmprestimo(ResultSet rs) throws SQLException {
        Beneficiario b = new Beneficiario();
        b.setId(rs.getInt("beneficiario_id"));
        b.setNome(rs.getString("nome"));
        b.setCpf(rs.getString("cpf"));
        b.setTelefone(rs.getString("telefone"));

        Emprestimo e = new Emprestimo(b, rs.getBoolean("emprestado"),
                                       rs.getDate("data_evento").toLocalDate());
        e.setId(rs.getInt("id"));
        e.setCategoria(rs.getString("categoria"));
        e.setQuantidade(rs.getInt("quantidade"));
        return e;
    }
}