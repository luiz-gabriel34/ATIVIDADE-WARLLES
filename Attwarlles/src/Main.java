import java.io.*;
import java.util.Scanner;

public class Main {

    private static final String ARQUIVO_ALUNOS      = "alunos.txt";
    private static final String ARQUIVO_PROFESSORES = "professores.txt";
    private static final String ARQUIVO_DISCIPLINAS = "disciplinas.txt";
    private static final String SEP = "|";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        inicializarArquivos();

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║        SISTEMA ESCOLAR - TXT         ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean continuar = true;
        while (continuar) {

            System.out.println("\n======= SELECIONE O ARQUIVO =======");
            System.out.println("  1. Alunos");
            System.out.println("  2. Professores");
            System.out.println("  3. Disciplinas");
            System.out.println("  0. Sair");
            System.out.println("===================================");
            System.out.print("Escolha: ");

            int arquivo = lerInteiro(scanner);

            if (arquivo == 0) {
                System.out.println("\nEncerrando o sistema. Ate mais!");
                break;
            }

            if (arquivo < 1 || arquivo > 3) {
                System.out.println("[!] Opcao invalida.");
                continue;
            }

            System.out.println("\n------- O QUE DESEJA FAZER? -------");
            System.out.println("  1. Cadastrar");
            System.out.println("  2. Buscar por ID");
            System.out.println("  0. Voltar");
            System.out.println("-----------------------------------");
            System.out.print("Escolha: ");

            int operacao = lerInteiro(scanner);

            if (operacao == 0) continue;

            switch (arquivo) {
                case 1 -> {
                    if (operacao == 1)      cadastrarAluno(scanner);
                    else if (operacao == 2) buscarAluno(scanner);
                    else System.out.println("[!] Opcao invalida.");
                }
                case 2 -> {
                    if (operacao == 1)      cadastrarProfessor(scanner);
                    else if (operacao == 2) buscarProfessor(scanner);
                    else System.out.println("[!] Opcao invalida.");
                }
                case 3 -> {
                    if (operacao == 1)      cadastrarDisciplina(scanner);
                    else if (operacao == 2) buscarDisciplina(scanner);
                    else System.out.println("[!] Opcao invalida.");
                }
            }

            continuar = perguntarContinuar(scanner, "Deseja acessar outro arquivo");
        }

        scanner.close();
    }

    // ================================================================
    // VALIDACOES DE TIPOS PRIMITIVOS
    // ================================================================

    // Le e valida um numero inteiro positivo - repete ate receber valor correto
    private static int lerInteiro(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("[!] Valor invalido. Digite apenas numeros inteiros: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    // Le e valida um inteiro positivo maior que zero (ex: idade, carga horaria)
    private static int lerInteiroPositivo(Scanner scanner, String campo) {
        int valor;
        while (true) {
            while (!scanner.hasNextInt()) {
                System.out.print("[!] " + campo + " deve ser um numero inteiro. Digite novamente: ");
                scanner.next();
            }
            valor = scanner.nextInt();
            scanner.nextLine();
            if (valor > 0) break;
            System.out.print("[!] " + campo + " deve ser maior que zero. Digite novamente: ");
        }
        return valor;
    }

    // Le e valida um texto que contenha apenas letras e espacos (ex: nome, area)
    // Rejeita se for vazio ou contiver numeros
    private static String lerTexto(Scanner scanner, String campo) {
        String valor;
        while (true) {
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.print("[!] " + campo + " nao pode ser vazio. Digite novamente: ");
                continue;
            }
            // Verifica se o texto contem algum digito - rejeita se tiver
            if (valor.matches(".*\\d.*")) {
                System.out.print("[!] " + campo + " nao pode conter numeros. Digite novamente: ");
                continue;
            }
            break;
        }
        return valor;
    }

    // Le e valida a turma - deve conter letras E pode ter numeros (ex: 3A, 2B)
    // mas nao pode ser vazio nem conter apenas numeros
    private static String lerTurma(Scanner scanner) {
        String valor;
        while (true) {
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.print("[!] Turma nao pode ser vazia. Digite novamente: ");
                continue;
            }
            // Turma deve conter pelo menos uma letra (ex: 3A, 2B, A1, TB2)
            if (!valor.matches(".*[a-zA-Z].*")) {
                System.out.print("[!] Turma deve conter pelo menos uma letra (ex: 3A, 2B). Digite novamente: ");
                continue;
            }
            // Turma nao pode ter espacos nem caracteres especiais
            if (!valor.matches("[a-zA-Z0-9]+")) {
                System.out.print("[!] Turma so pode conter letras e numeros, sem espacos. Digite novamente: ");
                continue;
            }
            break;
        }
        return valor.toUpperCase(); // Padroniza para maiusculo
    }

    // ================================================================
    // UTILITARIOS GERAIS
    // ================================================================

    private static void inicializarArquivos() {
        criarSeNaoExistir(ARQUIVO_ALUNOS);
        criarSeNaoExistir(ARQUIVO_PROFESSORES);
        criarSeNaoExistir(ARQUIVO_DISCIPLINAS);
    }

    private static void criarSeNaoExistir(String nomeArquivo) {
        File f = new File(nomeArquivo);
        if (!f.exists()) {
            try { f.createNewFile(); }
            catch (IOException e) {
                System.out.println("[ERRO] Nao foi possivel criar " + nomeArquivo + ": " + e.getMessage());
            }
        }
    }

    private static boolean perguntarContinuar(Scanner scanner, String mensagem) {
        System.out.print("\n" + mensagem + "? (s/n): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        return resposta.equals("s");
    }

    private static int gerarProximoId(String nomeArquivo) {
        int maiorId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split("\\" + SEP);
                if (partes.length > 0) {
                    try {
                        int id = Integer.parseInt(partes[0].trim());
                        if (id > maiorId) maiorId = id;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao gerar ID: " + e.getMessage());
        }
        return maiorId + 1;
    }

    private static void escreverNoArquivo(String nomeArquivo, String linha) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true))) {
            writer.newLine();
            writer.write(linha);
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao salvar no arquivo: " + e.getMessage());
        }
    }

    private static void separador() {
        System.out.println("  +----------------------------------+");
    }

    // ================================================================
    // ALUNOS
    // ================================================================

    private static void cadastrarAluno(Scanner scanner) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- CADASTRAR ALUNO ---");

            // Nome: apenas letras e espacos
            System.out.print("Nome  : ");
            String nome = lerTexto(scanner, "Nome");

            // Turma: letras e numeros, obrigatoriamente com ao menos uma letra
            System.out.print("Turma : ");
            String turma = lerTurma(scanner);

            // Idade: inteiro positivo obrigatorio
            System.out.print("Idade : ");
            int idade = lerInteiroPositivo(scanner, "Idade");

            int id = gerarProximoId(ARQUIVO_ALUNOS);
            String registro = id + SEP + nome + SEP + turma + SEP + idade;
            escreverNoArquivo(ARQUIVO_ALUNOS, registro);

            System.out.println("\n  ╔══════════════════════════════════╗");
            System.out.println("  ║       ALUNO CADASTRADO           ║");
            separador();
            System.out.printf("  | %-10s : %-20s|\n", "ID",    id);
            System.out.printf("  | %-10s : %-20s|\n", "Nome",  nome);
            System.out.printf("  | %-10s : %-20s|\n", "Turma", turma);
            System.out.printf("  | %-10s : %-20s|\n", "Idade", idade);
            separador();
            System.out.println("  ╚══════════════════════════════════╝");

            continuar = perguntarContinuar(scanner, "Deseja cadastrar outro aluno");
        }
    }

    private static void buscarAluno(Scanner scanner) {
        System.out.print("\nDigite o ID do aluno: ");
        int idBusca = lerInteiro(scanner);

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_ALUNOS))) {
            String linha;
            boolean encontrado = false;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split("\\" + SEP);
                if (partes.length >= 4 && Integer.parseInt(partes[0].trim()) == idBusca) {
                    System.out.println("\n  ╔══════════════════════════════════╗");
                    System.out.println("  ║        ALUNO ENCONTRADO          ║");
                    separador();
                    System.out.printf("  | %-10s : %-20s|\n", "ID",    partes[0].trim());
                    System.out.printf("  | %-10s : %-20s|\n", "Nome",  partes[1].trim());
                    System.out.printf("  | %-10s : %-20s|\n", "Turma", partes[2].trim());
                    System.out.printf("  | %-10s : %-20s|\n", "Idade", partes[3].trim());
                    separador();
                    System.out.println("  ╚══════════════════════════════════╝");
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) System.out.println("[!] Nenhum aluno encontrado com ID " + idBusca + ".");
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao buscar aluno: " + e.getMessage());
        }
    }

    // ================================================================
    // PROFESSORES
    // ================================================================

    private static void cadastrarProfessor(Scanner scanner) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- CADASTRAR PROFESSOR ---");

            // Nome e Area: apenas letras e espacos
            System.out.print("Nome       : ");
            String nome = lerTexto(scanner, "Nome");

            System.out.print("Area       : ");
            String area = lerTexto(scanner, "Area");

            // Disciplina: apenas letras e espacos
            System.out.print("Disciplina : ");
            String disciplina = lerTexto(scanner, "Disciplina");

            int id = gerarProximoId(ARQUIVO_PROFESSORES);
            String registro = id + SEP + nome + SEP + area + SEP + disciplina;
            escreverNoArquivo(ARQUIVO_PROFESSORES, registro);

            System.out.println("\n  ╔══════════════════════════════════╗");
            System.out.println("  ║      PROFESSOR CADASTRADO        ║");
            separador();
            System.out.printf("  | %-10s : %-20s|\n", "ID",         id);
            System.out.printf("  | %-10s : %-20s|\n", "Nome",       nome);
            System.out.printf("  | %-10s : %-20s|\n", "Area",       area);
            System.out.printf("  | %-10s : %-20s|\n", "Disciplina", disciplina);
            separador();
            System.out.println("  ╚══════════════════════════════════╝");

            continuar = perguntarContinuar(scanner, "Deseja cadastrar outro professor");
        }
    }

    private static void buscarProfessor(Scanner scanner) {
        System.out.print("\nDigite o ID do professor: ");
        int idBusca = lerInteiro(scanner);

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PROFESSORES))) {
            String linha;
            boolean encontrado = false;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split("\\" + SEP);
                if (partes.length >= 4 && Integer.parseInt(partes[0].trim()) == idBusca) {
                    System.out.println("\n  ╔══════════════════════════════════╗");
                    System.out.println("  ║       PROFESSOR ENCONTRADO       ║");
                    separador();
                    System.out.printf("  | %-10s : %-20s|\n", "ID",         partes[0].trim());
                    System.out.printf("  | %-10s : %-20s|\n", "Nome",       partes[1].trim());
                    System.out.printf("  | %-10s : %-20s|\n", "Area",       partes[2].trim());
                    System.out.printf("  | %-10s : %-20s|\n", "Disciplina", partes[3].trim());
                    separador();
                    System.out.println("  ╚══════════════════════════════════╝");
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) System.out.println("[!] Nenhum professor encontrado com ID " + idBusca + ".");
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao buscar professor: " + e.getMessage());
        }
    }

    // ================================================================
    // DISCIPLINAS
    // ================================================================

    private static void cadastrarDisciplina(Scanner scanner) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- CADASTRAR DISCIPLINA ---");

            // Nome da disciplina: apenas letras e espacos
            System.out.print("Nome da disciplina    : ");
            String nome = lerTexto(scanner, "Nome da disciplina");

            // Carga horaria: inteiro positivo obrigatorio
            System.out.print("Carga horaria (h)     : ");
            int cargaHoraria = lerInteiroPositivo(scanner, "Carga horaria");

            // ID do professor: inteiro positivo que deve existir no arquivo
            int idProfessor;
            while (true) {
                System.out.print("ID do professor resp. : ");
                idProfessor = lerInteiroPositivo(scanner, "ID do professor");
                if (professorExisteNoArquivo(idProfessor)) break;
                System.out.println("[!] Professor com ID " + idProfessor + " nao encontrado.");
                System.out.println("    Verifique o ID ou cadastre o professor antes.");
                if (!perguntarContinuar(scanner, "Deseja tentar outro ID de professor")) {
                    return;
                }
            }

            int id = gerarProximoId(ARQUIVO_DISCIPLINAS);
            String registro = id + SEP + nome + SEP + cargaHoraria + SEP + idProfessor;
            escreverNoArquivo(ARQUIVO_DISCIPLINAS, registro);

            System.out.println("\n  ╔══════════════════════════════════╗");
            System.out.println("  ║      DISCIPLINA CADASTRADA       ║");
            separador();
            System.out.printf("  | %-14s : %-16s|\n", "ID",            id);
            System.out.printf("  | %-14s : %-16s|\n", "Nome",          nome);
            System.out.printf("  | %-14s : %-16s|\n", "Carga Horaria", cargaHoraria + "h");
            System.out.printf("  | %-14s : %-16s|\n", "ID Professor",  idProfessor);
            separador();
            System.out.println("  ╚══════════════════════════════════╝");

            continuar = perguntarContinuar(scanner, "Deseja cadastrar outra disciplina");
        }
    }

    private static void buscarDisciplina(Scanner scanner) {
        System.out.print("\nDigite o ID da disciplina: ");
        int idBusca = lerInteiro(scanner);

        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_DISCIPLINAS))) {
            String linha;
            boolean encontrado = false;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split("\\" + SEP);
                if (partes.length >= 4 && Integer.parseInt(partes[0].trim()) == idBusca) {
                    System.out.println("\n  ╔══════════════════════════════════╗");
                    System.out.println("  ║       DISCIPLINA ENCONTRADA      ║");
                    separador();
                    System.out.printf("  | %-14s : %-16s|\n", "ID",            partes[0].trim());
                    System.out.printf("  | %-14s : %-16s|\n", "Nome",          partes[1].trim());
                    System.out.printf("  | %-14s : %-16s|\n", "Carga Horaria", partes[2].trim() + "h");
                    System.out.printf("  | %-14s : %-16s|\n", "ID Professor",  partes[3].trim());
                    separador();
                    System.out.println("  ╚══════════════════════════════════╝");
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) System.out.println("[!] Nenhuma disciplina encontrada com ID " + idBusca + ".");
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao buscar disciplina: " + e.getMessage());
        }
    }

    private static boolean professorExisteNoArquivo(int idProf) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PROFESSORES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split("\\" + SEP);
                if (partes.length > 0 && Integer.parseInt(partes[0].trim()) == idProf) return true;
            }
        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao verificar professor: " + e.getMessage());
        }
        return false;
    }
}