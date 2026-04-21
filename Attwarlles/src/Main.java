import java.io.*;
import java.util.Scanner;

public class Main {

    private static final String NOME_ARQUIVO = "arquivo.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       EDITOR DE ARQUIVO              ║");
        System.out.println("║    Insera os dados dos alunos        ║");
        System.out.println("╚══════════════════════════════════════╝");

        do {
            System.out.println("\n----------- MENU -----------");
            System.out.println("  1. Criar arquivo.txt");
            System.out.println("  2. Modificar arquivo.txt");
            System.out.println("  3. Excluir arquivo.txt");
            System.out.println("  0. Sair");
            System.out.println("----------------------------");
            System.out.print("Escolha uma opcao: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Entrada invalida. Digite um numero: ");
                scanner.next();
            }
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> criarArquivo();
                case 2 -> modificarArquivo(scanner);
                case 3 -> excluirArquivo();
                case 0 -> System.out.println("\nEncerrando o editor. Ate mais!");
                default -> System.out.println("\n[!] Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void criarArquivo() {
        File arquivo = new File(NOME_ARQUIVO);

        System.out.println("\n--- CRIAR ARQUIVO ---");

        if (arquivo.exists()) {
            System.out.println("[!] O arquivo '" + NOME_ARQUIVO + "' JA EXISTE.");
            System.out.println("    Use a opcao 2 para modificar o conteudo.");
            return;
        }

        try {
            if (arquivo.createNewFile()) {
                System.out.println("[✓] Arquivo criado com sucesso em: " + NOME_ARQUIVO);
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao criar o arquivo: " + e.getMessage());
        }
    }

    private static void modificarArquivo(Scanner scanner) {
        File arquivo = new File(NOME_ARQUIVO);

        System.out.println("\n--- MODIFICAR ARQUIVO ---");

        if (!arquivo.exists()) {
            System.out.println("[!] O arquivo '" + NOME_ARQUIVO + "' NAO EXISTE.");
            System.out.println("    Use a opcao 1 para criar o arquivo primeiro.");
            return;
        }

        System.out.print("Digite o conteudo a adicionar sobre o aluno: ");
        String novoConteudo = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.newLine();
            writer.write(novoConteudo);
            System.out.println("[✓] Conteudo adicionado com sucesso!");
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao modificar o arquivo: " + e.getMessage());
        }
    }

    private static void excluirArquivo() {
        File arquivo = new File(NOME_ARQUIVO);

        System.out.println("\n--- EXCLUIR ARQUIVO ---");

        if (!arquivo.exists()) {
            System.out.println("[!] O arquivo '" + NOME_ARQUIVO + "' NAO EXISTE.");
            System.out.println("    Nada a excluir.");
            return;
        }

        System.out.print("[!] Tem certeza que deseja excluir '" + NOME_ARQUIVO + "'? (s/n): ");
        Scanner confirmacao = new Scanner(System.in);
        String resposta = confirmacao.nextLine().trim().toLowerCase();

        if (resposta.equals("s")) {
            if (arquivo.delete()) {
                System.out.println("[✓] Arquivo excluido com sucesso!");
            } else {
                System.out.println("[ERRO] Nao foi possivel excluir o arquivo.");
            }
        } else {
            System.out.println("[!] Exclusao cancelada.");
        }
    }
}
