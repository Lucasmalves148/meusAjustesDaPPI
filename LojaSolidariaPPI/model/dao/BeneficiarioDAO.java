package model.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Beneficiario;

public class BeneficiarioDAO {


    //Antestava lendo e escrevendo no Beneficiarios.csv via GerenciadorArquivos; 
    // agora usa a query INSERT, SELECT ... WHERE cpf = ? e SELECT * direto no banco, e ganhou um listarTodos().


    public void salvarBeneficiario(Beneficiario b) {
        String sql = "INSERT INTO beneficiarios (nome, cpf, telefone) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, b.getNome());
            ps.setString(2, b.getCpf());
            ps.setString(3, b.getTelefone());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    b.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao salvar beneficiário: " + e.getMessage());
        }
    }

    public Beneficiario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM beneficiarios WHERE cpf = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montarBeneficiario(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar beneficiário: " + e.getMessage());
        }
        return null;
    }

    public List<Beneficiario> listarTodos() {
        String sql = "SELECT * FROM beneficiarios ORDER BY nome";
        List<Beneficiario> lista = new ArrayList<>();

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(montarBeneficiario(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar beneficiários: " + e.getMessage());
        }
        return lista;
    }

    private Beneficiario montarBeneficiario(ResultSet rs) throws SQLException {
        Beneficiario b = new Beneficiario();
        b.setId(rs.getInt("id"));
        b.setNome(rs.getString("nome"));
        b.setCpf(rs.getString("cpf"));
        b.setTelefone(rs.getString("telefone"));
        return b;
    }
}