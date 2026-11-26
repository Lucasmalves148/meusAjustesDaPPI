package model;
import java.time.LocalDate;

public class Doacao extends Transacao {

    private int id;
    private String tipo; // "ENTRADA" ou "SAIDA"
    private static String SEM_CATEGORIA = "-";

    // Entradas continuam sendo registradas por categoria.
    public Doacao(String tipo, String categoria, int quantidade, LocalDate dataEvento) {
        super(dataEvento);
        this.tipo = tipo;
        adicionarPeca(new Peca(categoria, quantidade));
    }

    // Saídas agora são registradas apenas pela quantidade total, sem categoria.
    public Doacao(String tipo, int quantidade, LocalDate dataEvento) {
        this(tipo, SEM_CATEGORIA, quantidade, dataEvento);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    // acessa uma lista de peças, pega apartir do 1º elemento e extrai categoria
    public String getCategoria() {
        return getPecas().get(0).getCategoria();
    }

    public int getQuantidade() {
        return getPecas().get(0).getQuantidade();
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // acessa uma lista de peças, pega o 1° elemento e atualiza categoria com o
    // parâmetro recebido
    public void setCategoria(String categoria) {
        getPecas().get(0).setCategoria(categoria);
    }

    public void setQuantidade(int quantidade) {
        getPecas().get(0).setQuantidade(quantidade);
    }

    @Override
    public String toString() {
        return tipo + "," + getCategoria() + "," + getQuantidade() + "," + getDataFormatada();
    }
}