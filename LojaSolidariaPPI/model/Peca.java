package model;
public class Peca {

    private int quantidade;
    private String categoria;

    public Peca(String categoria, int quantidade) {
        this.categoria = categoria;
        this.quantidade = quantidade;
    }
   
    public int getQuantidade() {
        return quantidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
