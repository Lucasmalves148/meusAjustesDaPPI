import java.io.*;
import java.util.*;

import javax.sound.sampled.SourceDataLine;

public class Estoque {

    private String masculinos;
    private String femininos;
    private String infantil;
    private String calcados;
    private String diversos;
    private int quantidade;

    public Estoque(String masculinos, String femininos, String infantil, String calçados, String diversos,
            int quantidade) {

        this.masculinos = masculinos;
        this.femininos = femininos;
        this.infantil = infantil;
        this.calcados = calçados;
        this.diversos = diversos;
        this.quantidade = quantidade;

    }

    public Estoque() {

    }

    public void exibirEstoque() {
        System.out.println("Aqui está o estoque TOTAL:");
        System.out.println(getQuantidade());
    }

    public void listarCategorias() {
        System.out.println("Abaixo estão todas as informações do estoque:");
        System.out.println(masculinos);
        System.out.println(femininos);
        System.out.println(infantil);
        System.out.println(calcados);
        System.out.println(diversos);
    }

    public void adicionarQuantidade(String categoria, int quantidade) {
        this.quantidade += quantidade;
    }

    // estoque.adicionarQuantidade(categoria, quantidade);

    public String categoriaExiste(String categoria) {
        return categoria;
    }

    public void removerQuantidade(String categoria, int quantidade) {
        if (quantidade != 0) {
            this.quantidade -= quantidade;
        }else{
            System.out.println("Não é possível diminuir a quantidade");
            return;
        }
    }

    public String getMasculinos() {
        return masculinos;
    }

    public String geFemininos() {
        return femininos;
    }

    public String getInfantil() {
        return infantil;
    }

    public String getCacados() {
        return calcados;
    }

    public String getDiversos() {
        return diversos;
    }

    public int getQuantidade() {
        return quantidade;
    }

}