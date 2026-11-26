import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GerenciadorArquivos {

    // Cada DAO informa qual arquivo usa
    protected abstract String getNomeArquivo();

    // Cada DAO converte a linha para seu objeto
    protected abstract Object converterLinha(String linha);


    public void adicionarLinha(String arquivo, String conteudo) {

        try (PrintWriter pw = new PrintWriter(
                new FileWriter(arquivo, true))) {

            pw.println(conteudo);
            System.out.println("✓ Registro realizado com sucesso!");

        } catch (IOException e) {
            
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    // Método herdado por todas as filhas
    public List<String> lerLinhasArquivo(String arquivo) {

        List<String> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {

            String linha;

            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

        return linhas;
    }

}