package model;
import java.time.LocalDate;

public class Emprestimo extends Transacao {

    private int id;
    private Beneficiario beneficiario;
    private boolean emprestado;

    public Emprestimo(Beneficiario beneficiario, boolean emprestado, LocalDate data) {
        super(data);
        this.beneficiario = beneficiario;
        this.emprestado = emprestado;
    }

    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public boolean estaEmprestado() {
        return emprestado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return beneficiario.getNome();
    }

    public String getCpf() {
        return beneficiario.getCpf();
    }

    public String getTelefone() {
        return beneficiario.getTelefone();
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public void setEmprestado(boolean emprestado) {
        this.emprestado = emprestado;

    }

    public String getCategoria() {
        return getPecas().isEmpty() ? "-" : getPecas().get(0).getCategoria(); // Ternário
    }

    public int getQuantidade() {
        return getPecas().isEmpty() ? 0 : getPecas().get(0).getQuantidade();
    }

    public void setCategoria(String categoria) {
        garantirPeca();
        getPecas().get(0).setCategoria(categoria);
    }

    public void setQuantidade(int quantidade) {
        garantirPeca();
        getPecas().get(0).setQuantidade(quantidade);
    }

    // Testa se a lista de peças está vazia, se estiver cria uma "peça padrão" e
    // depois o set altera
    private void garantirPeca() {
        if (getPecas().isEmpty()) {
            adicionarPeca(new Peca("-", 0));
        }
    }

    @Override
    public String toString() {
        return getCpf() + "," + getQuantidade() + "," + getCategoria() + "," + emprestado + ","
                + getDataFormatada();
    }

}
