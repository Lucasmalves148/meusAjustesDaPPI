package controller;
import java.time.LocalDate;
import java.util.*;

import model.Beneficiario;
import model.Emprestimo;
import model.dao.BeneficiarioDAO;
import model.dao.EmprestimoDAO;

public class CadastroEmprestimo {

    private BeneficiarioDAO beneficiarioDAO;
    private EmprestimoDAO emprestimoDAO;

    public CadastroEmprestimo() {
        beneficiarioDAO = new BeneficiarioDAO();
        emprestimoDAO = new EmprestimoDAO();
    }

    public void exibirEmprestimos() {
        Map<String, Integer> emprestimos = emprestimoDAO.lerEstoqueEmprestimos();

        System.out.println("\n=== ESTOQUE DE EMPRÉSTIMOS ===");
        System.out.println("Masculino: " + emprestimos.getOrDefault("Masculino", 0));
        System.out.println("Feminino: " + emprestimos.getOrDefault("Feminino", 0));
        System.out.println("Não especificado: " + emprestimos.getOrDefault("Não especificado", 0));
    }

    public void adicionarQuantidadeEmprestimos(String categoria, int quantidade) {
        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero!");
            return;
        }

        Map<String, Integer> emprestimos = emprestimoDAO.lerEstoqueEmprestimos();

        emprestimos.put(categoria, emprestimos.getOrDefault(categoria, 0) + quantidade);

        emprestimoDAO.salvarArquivoEstoqueEmprestimos(emprestimos);

        System.out.println("✓ Estoque de empréstimos atualizado!");
    }

   
    public void cadastrarPessoa(String nome, String cpf, String telefone) {
        if (verificarCadastro(cpf)) {
            System.out.println("Pessoa já possui cadastro!");
            return;
        }

        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setNome(nome);
        beneficiario.setCpf(cpf);
        beneficiario.setTelefone(telefone);

        beneficiarioDAO.salvarBeneficiario(beneficiario);
    }

    public boolean verificarCadastro(String cpf) {
        return beneficiarioDAO.buscarPorCpf(cpf) != null;
    }


    public void registrarEmprestimo(String cpf, String categoria, int quantidade) {

        if (quantidade <= 0) {
            System.out.println("Quantidade deve ser maior que zero!");
            return;
        }

        Beneficiario beneficiario = beneficiarioDAO.buscarPorCpf(cpf);

        if (beneficiario == null) {
            System.out.println("Cadastro não encontrado! ");
            return;
        }

        if (possuiEmprestimoAtivo(cpf)) {
            System.out.println("Pessoa já possui empréstimo ativo.");
            return;
        }

        Map<String, Integer> estoqueEmprestimos = emprestimoDAO.lerArquivoEstoqueEmprestimos();
        int estoqueAtual = estoqueEmprestimos.getOrDefault(categoria, 0);

        if (estoqueAtual < quantidade) {
            System.out.println("Estoque insuficiente para a categoria: " + categoria);
            return;
        }

        estoqueEmprestimos.put(categoria, estoqueAtual - quantidade);

        Emprestimo novoEmprestimo = new Emprestimo(beneficiario, true, LocalDate.now());
        novoEmprestimo.setCategoria(categoria);
        novoEmprestimo.setQuantidade(quantidade);

        emprestimoDAO.registrarNovoEmprestimo(novoEmprestimo);
        emprestimoDAO.salvarArquivoEstoqueEmprestimos(estoqueEmprestimos);

        System.out.println("✓ Empréstimo realizado para " + beneficiario.getNome());
    }

    private boolean possuiEmprestimoAtivo(String cpf) {
        for (Emprestimo emp : emprestimoDAO.lerListaEmprestimos()) {
            if (emp.getCpf().equals(cpf) && emp.estaEmprestado()) {
                return true;
            }
        }
        return false;
    }

    // Soma a quantidade de peças de todos os empréstimos ATIVOS de um
    // beneficiário. Hoje só pode existir um empréstimo ativo por vez (regra
    // mantida em possuiEmprestimoAtivo), mas somar deixa o cálculo correto
    // mesmo se essa regra for flexibilizada no futuro.
    public int calcularQuantidadeEmprestimosAtivos(String cpf) {
        int total = 0;

        for (Emprestimo emp : emprestimoDAO.lerListaEmprestimos()) {
            if (emp.getCpf().equals(cpf) && emp.estaEmprestado()) {
                total += emp.getQuantidade();
            }
        }

        return total;
    }

    public void listarEmprestimos() {
        List<Emprestimo> lista = emprestimoDAO.lerListaEmprestimos();

        System.out.println("\n === EMPRÉSTIMOS ATIVOS === ");

        boolean vazio = true;

        for (Emprestimo emp : lista) {
            if (emp.estaEmprestado()) {
                System.out.printf("Nome: %s | CPF: %s | Telefone: %s | Qtd: %d | Categoria: %s | Data: %s%n",
                        emp.getNome(), emp.getCpf(), emp.getTelefone(), emp.getQuantidade(), emp.getCategoria(),
                        emp.getDataFormatada());
                vazio = false;
            }
        }
        if (vazio) {
            System.out.println("Nenhum empréstimo ativo!");
        }
    }

    public void registrarDevolucao(String cpf) {
        List<Emprestimo> lista = emprestimoDAO.lerListaEmprestimos();
        Map<String, Integer> estoqueEmprestimos = emprestimoDAO.lerArquivoEstoqueEmprestimos();

        boolean encontrado = false;

        for (Emprestimo emp : lista) {
            if (emp.getCpf().equals(cpf) && emp.estaEmprestado()) {
                emp.setEmprestado(false);

                estoqueEmprestimos.put(emp.getCategoria(),
                        estoqueEmprestimos.getOrDefault(emp.getCategoria(), 0) + emp.getQuantidade());

                System.out.println("✓ Devolução de " + emp.getNome());

                encontrado = true;
                break;
            }
        }

        emprestimoDAO.salvarListaEmprestimos(lista);
        emprestimoDAO.salvarArquivoEstoqueEmprestimos(estoqueEmprestimos);

        if (!encontrado) {
            System.out.println("CPF não encontrado ou empréstimo já devolvido!");
        }
    }

    public void pesquisarCadastro(String cpf) {
        Beneficiario beneficiario = beneficiarioDAO.buscarPorCpf(cpf);

        if (beneficiario == null) {
            System.out.println("Cadastro não encontrado!");
            return;
        }

        System.out.println("\n === CADASTRO ENCONTRADO === ");
        System.out.println("Nome:  " + beneficiario.getNome());
        System.out.println("CPF:  " + beneficiario.getCpf());
        System.out.println("Telefone:  " + beneficiario.getTelefone());

        boolean temAtivo = false;

        for (Emprestimo emp : emprestimoDAO.lerListaEmprestimos()) {
            if (emp.getCpf().equals(cpf) && emp.estaEmprestado()) {
                System.out.println("Categoria emprestada:  " + emp.getCategoria());
                System.out.println("Quantidade emprestada:  " + emp.getQuantidade());
                System.out.println("Data do empréstimo:  " + emp.getDataFormatada());
                temAtivo = true;
            }
        }

        if (!temAtivo) {
            System.out.println("Empréstimo ativo:  nenhum no momento");
        } else {
            System.out.println("Total de peças emprestadas (ativo): " + calcularQuantidadeEmprestimosAtivos(cpf));
        }
    }
    /*
     * 
     * public void excluirCadasgro (String cpf){
     * List <Emprestimos> lista = lerListaEmprestimos();
     * 
     * boolean encontrado = false;
     * 
     * Iterator<Emprestimos> iterator = lista.iterator();
     * 
     * while (iterator.hasNext()) {
     * 
     * Emprestimos emp = iterator.next();
     * 
     * if (emp.getCpf().equals(cpf)){
     * 
     * encontrado = true;
     * 
     * if (emp.isEmprestado()) {
     * System.out.println("Não é possível excluir!");
     * System.out.println("A pessoa possui empréstimo ativo.");
     * return;
     * 
     * 
     * }
     * 
     * iterator.remove();
     * 
     * salvarListaEmprestimos(lista);
     * 
     * System.out.println("✓ Cadastro excluído com sucesso!");
     * 
     * }
     * 
     * }
     * 
     * 
     * 
     * 
     * 
     * }
     * 
     * 
     * 
     */

}