package controller;
    import java.time.LocalDate;
import java.util.*;

import model.Doacao;
import model.dao.DoacaoDAO;

public class CadastroDoacao {

    private static String[] CATEGORIAS = { "Masculino", "Feminino", "Infantil", "Calçados", "Diversos", "Não especificado"};

    private DoacaoDAO doacaoDAO;

    public CadastroDoacao() {
        doacaoDAO = new DoacaoDAO();

        /*erifica se o arquivo existe
        File arquivo = new File(doacaoDAO.getNomeArquivo());
        if (!arquivo.exists()) {
            System.out.println(" Sistema de registros inicializado ");
        }
            */
    }

    public String[] getCategorias() {
        return CATEGORIAS;
    }

    public void exibirEstoqueTotal() {
        int total = calcularEstoqueAtual();

        System.out.println("\n" + "=".repeat(40));
        System.out.println("📦 QUANTIDADE EM ESTOQUE TOTAL");
        System.out.println("=".repeat(40));

        System.out.printf("TOTAL == %,d unidades%n", total);
        System.out.println("=".repeat(40));
    }

    public void listarCategorias() {
        System.out.println("\n=== CATEGORIAS DISPONÍVEIS ===");
        for (int i = 0; i < CATEGORIAS.length; i++) {
            System.out.println((i + 1) + ". " + CATEGORIAS[i]);
        }
    }

    public void adicionarQuantidade(String categoria, int quantidade) {
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero!");
            return;
        }

        Doacao novoRegistro = new Doacao("ENTRADA", categoria, quantidade, LocalDate.now());

        doacaoDAO.salvar(novoRegistro);

        System.out.println(" (Registrado como ENTRADA no sistema");
        System.out.printf("✓ Adicionadas %,d unidades de %s%n ", quantidade, categoria);

    }

    public void removerQuantidade(int quantidade) {

        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero!");
            return;
        }

        int estoqueAtual = calcularEstoqueAtual();

        if (estoqueAtual < quantidade) {
            System.out.printf("✗ Estoque insuficiente! Disponível: %,d unidade%n", estoqueAtual);
            return;

        }

        Doacao novoRegistro = new Doacao("SAIDA", quantidade, LocalDate.now());

        doacaoDAO.salvar(novoRegistro);

        System.out.printf("✓ Removidas %,d unidades %n", quantidade );
        System.out.println("  (Registrado como SAÍDA no sistema)");
    }

    

    // mateus
    private int calcularEstoqueAtual() {
        int valorEstoqueTotal = doacaoDAO.calcularEstoqueTotal();
        return valorEstoqueTotal;
    }

    // Terminar de fazer ainda, parte mais complexa e complicada

    public void gerarRelatorioMensal(int mes, int ano, String obs1, String obs2) {
        List<Doacao> lista = doacaoDAO.listarTodas();

        System.out.println("\n" + "=".repeat(50));
        System.out.printf("📊 RELATÓRIO MENSAL - %02d/%d%n", mes, ano);
        System.out.println("=".repeat(50));

        if (lista.isEmpty()) {
            System.out.println("Nenhum registro encontrado!");
            return;
        }

        // Calcular entradas do mês por categoria
        Map<String, Integer> entradasPorCategoria = new HashMap<>();
        int totalEntradas = 0;

        // Calcular saídas por dia
        Map<String, Integer> saidasPorDia = new TreeMap<>();
        Map<String, Integer> atendimentosPorDia = new HashMap<>();
        int totalSaidas = 0;
        int totalAtendimentos = 0;

        for (Doacao est : lista) {
            LocalDate data = est.getData();

            if (data.getMonthValue() == mes && data.getYear() == ano) {
                String tipo = est.getTipo();
                String categoria = est.getCategoria();
                int quantidade = est.getQuantidade();
                String dataStr = est.getDataFormatada();

                if (tipo.equals("ENTRADA")) {
                    entradasPorCategoria.put(categoria, entradasPorCategoria.getOrDefault(categoria, 0) + quantidade);

                    totalEntradas += quantidade;

                } else if (tipo.equals("SAIDA")) {
                    saidasPorDia.put(dataStr, saidasPorDia.getOrDefault(dataStr, 0) + quantidade);

                    atendimentosPorDia.put(dataStr, atendimentosPorDia.getOrDefault(dataStr, 0) + 1);

                    totalSaidas += quantidade;
                    totalAtendimentos++;
                }
            }
        }

        // Entradas por categoria
        System.out.println("\n📈 DOAÇÕES (ENTRADAS) NO MÊS:");
        System.out.println("-".repeat(35));

        for (String cat : CATEGORIAS) {
            int qtd = entradasPorCategoria.getOrDefault(cat, 0);
            System.out.printf("• %-12s: %,7d unidades%n", cat, qtd);
        }

        System.out.println("-".repeat(35));
        System.out.printf("TOTAL DE ENTRADAS: %,9d unidades%n", totalEntradas);

        // Área de observação 1 (ex: "Foram descartados 474 itens...")
        if (!obs1.isEmpty()) {
            System.out.println("");
            System.out.println("-".repeat(35));
            System.out.println(" OBSERVAÇÕES / DESCARTES:");
            System.out.println(" " + obs1);
            System.out.println("-".repeat(35));
        }

        // Saídas por dia
        System.out.println("\n📅 SAÍDAS POR DIA NO MÊS:");
        System.out.println("-".repeat(35));
        System.out.printf("%-12s | %-15s | %-12s%n", "DATA", "ITENS SAÍDOS", "ATENDIMENTOS");
        System.out.println("-".repeat(35));

        if (saidasPorDia.isEmpty()) {
            System.out.println("Nenhuma saída registrada neste mês.");
        } else {
            for (Map.Entry<String, Integer> entry : saidasPorDia.entrySet()) {
                String data = entry.getKey();
                int itensDia = entry.getValue();
                int atendimentosDia = atendimentosPorDia.getOrDefault(data, 0);

                System.out.printf("%-12s | %,15d | %,12d%n", data, itensDia, atendimentosDia);
            }
        }

        System.out.println("-".repeat(35));
        System.out.printf("TOTAL DE SAÍDAS: %,11d unidades%n", totalSaidas);
        System.out.printf("TOTAL ATENDIMENTOS: %,8d%n", totalAtendimentos);

        // Estoque atual
        int estoqueAtual = calcularEstoqueAtual();
        System.out.println("\n💼 ESTOQUE ATUAL (Geral):");
        System.out.println("-".repeat(35));
        System.out.printf("Quantidade total em estoque == %,d unidades%n", estoqueAtual);
        System.out.println("\n");

        // Área de observação 2 (Ações Externas)
        if (!obs2.isEmpty()) {
            System.out.println("-".repeat(35));
            System.out.println(" AÇÕES EXTERNAS / OUTRAS OBSERVAÇÕES:");
            System.out.println(" [ ] " + obs2);
            System.out.println("-".repeat(35));
        }
        System.out.println("\n");
    }
}