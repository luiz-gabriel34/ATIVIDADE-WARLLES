 java.io.*;
import java.util.Scanner;

public class Main {

    // Nome fixo do arquivo que sera manipulado
    private static final String NOME_ARQUIVO = "arquivo.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        // Cabecalho do programa
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       EDITOR DE ARQUIVO              ║");
        System.out.println("║    Insera os dados dos alunos        ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Laco principal do menu - executa ate o usuario escolher sair
        do {
            System.out.println("\n----------- MENU -----------");
            System.out.println("  1. Criar arquivo.txt");
            System.out.println("  2. Modificar arquivo.txt");
            System.out.println("  3. Excluir arquivo.txt");
            System.out.println("  4. Ler arquivo.txt");
            System.out.println("  0. Sair");
            System.out.println("----------------------------");
            System.out.print("Escolha uma opcao: ");

            // Valida se a entrada e um numero inteiro
            while (!scanner.hasNextInt()) {
                System.out.print("Entrada invalida. Digite um numero: ");
                scanner.next();
            }

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer apos leitura do inteiro

            // Direciona para o metodo correto conforme a opcao escolhida
            switch (opcao) {
                case 1 -> criarArquivo();
                case 2 -> modificarArquivo(scanner);
                case 3 -> excluirArquivo();
                case 4 -> lerArquivo();
                case 0 -> System.out.println("\nEncerrando o editor. Ate mais!");
                default -> System.out.println("\n[!] Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0); // Repete enquanto o usuario nao digitar 0

        scanner.close(); // Fecha o scanner liberando o recurso
    }

    // -------------------------------------------------------
    // METODO: Criar o arquivo.txt vazio no diretorio atual
    // -------------------------------------------------------
    private static void criarArquivo() {
        // Representa o arquivo na memoria
        File arquivo = new File(NOME_ARQUIVO);

        System.out.println("\n--- CRIAR ARQUIVO ---");

        // Teste logico: verifica se o arquivo ja existe antes de criar
        if (arquivo.exists()) {
            System.out.println("[!] O arquivo '" + NOME_ARQUIVO + "' JA EXISTE.");
            System.out.println("    Use a opcao 2 para modificar o conteudo.");
            return; // Encerra o metodo sem criar
        }

        // Tenta criar o arquivo e captura possiveis erros de I/O
        try {
            if (arquivo.createNewFile()) {
                System.out.println("[✓] Arquivo criado com sucesso: " + NOME_ARQUIVO);
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao criar o arquivo: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // METODO: Adicionar uma linha de conteudo ao arquivo.txt
    // Permite somente uma edicao por vez
    // -------------------------------------------------------
    private static void modificarArquivo(Scanner scanner) {
        // Representa o arquivo na memoria
        File arquivo = new File(NOME_ARQUIVO);

        System.out.println("\n--- MODIFICAR ARQUIVO ---");

        // Teste logico: verifica se o arquivo existe antes de modificar
        if (!arquivo.exists()) {
            System.out.println("[!] O arquivo '" + NOME_ARQUIVO + "' NAO EXISTE.");
            System.out.println("    Use a opcao 1 para criar o arquivo primeiro.");
            return; // Encerra o metodo sem modificar
        }

        // Le o conteudo que o usuario deseja adicionar
        System.out.print("Digite o conteudo a adicionar sobre o aluno: ");
        String novoConteudo = scanner.nextLine();

        // Abre o arquivo em modo append (true = nao apaga o conteudo existente)
        // try-with-resources fecha o writer automaticamente ao terminar
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.newLine();        // Salta para a proxima linha antes de escrever
            writer.write(novoConteudo); // Escreve o conteudo digitado
            System.out.println("[✓] Conteudo adicionado com sucesso!");
            // Uma edicao concluida - retorna ao menu automaticamente
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao modificar o arquivo: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // METODO: Excluir o arquivo.txt do disco
    // -------------------------------------------------------
    private static void excluirArquivo() {
        // Representa o arquivo na memoria
        File arquivo = new File(NOME_ARQUIVO);

        System.out.println("\n--- EXCLUIR ARQUIVO ---");

        // Teste logico: verifica se o arquivo existe antes de excluir
        if (!arquivo.exists()) {
            System.out.println("[!] O arquivo '" + NOME_ARQUIVO + "' NAO EXISTE.");
            System.out.println("    Nada a excluir.");
            return; // Encerra o metodo sem excluir
        }

        // Pede confirmacao do usuario antes de excluir permanentemente
        System.out.print("[!] Tem certeza que deseja excluir '" + NOME_ARQUIVO + "'? (s/n): ");
        Scanner confirmacao = new Scanner(System.in);
        String resposta = confirmacao.nextLine().trim().toLowerCase();

        // Verifica se o usuario confirmou com "s"
        if (resposta.equals("s")) {
            if (arquivo.delete()) { // Exclui o arquivo do disco
                System.out.println("[✓] Arquivo excluido com sucesso!");
            } else {
                System.out.println("[ERRO] Nao foi possivel excluir o arquivo.");
            }
        } else {
            System.out.println("[!] Exclusao cancelada."); // Qualquer resposta diferente de "s" cancela
        }
    }

    // -------------------------------------------------------
    // METODO: Ler e exibir o conteudo do arquivo.txt
    // -------------------------------------------------------
    private static void lerArquivo() {
        // Representa o arquivo na memoria
        File arquivo = new File(NOME_ARQUIVO);

        System.out.println("\n--- LER ARQUIVO ---");

        // Teste logico: verifica se o arquivo existe antes de tentar ler
        if (!arquivo.exists()) {
            System.out.println("[!] O arquivo '" + NOME_ARQUIVO + "' NAO EXISTE.");
            System.out.println("    Use a opcao 1 para criar o arquivo primeiro.");
            return; // Encerra o metodo sem ler
        }

        System.out.println("Conteudo de " + NOME_ARQUIVO + ":");
        System.out.println("-----------------------------");

        // Abre o arquivo para leitura linha por linha
        // try-with-resources fecha o reader automaticamente ao terminar
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean arquivoVazio = true;

            // Le cada linha ate o fim do arquivo
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha); // Exibe cada linha no console
                arquivoVazio = false;
            }

            // Informa caso o arquivo esteja vazio
            if (arquivoVazio) {
                System.out.println("[!] O arquivo esta vazio.");
            }

        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao ler o arquivo: " + e.getMessage());
        }

        System.out.println("-----------------------------");
    }
}
