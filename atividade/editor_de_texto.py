import os

PASTA   = "atividade"
ARQUIVO = os.path.join(PASTA, "arquivo.txt")

def limpar_tela():
    os.system('cls' if os.name == 'nt' else 'clear')

def exibir_menu():
    print("=" * 40)
    print("         EDITOR DE TEXTO")
    print("=" * 40)
    print("  [1] Criar arquivo")
    print("  [2] Modificar arquivo")
    print("  [3] Excluir arquivo")
    print("  [0] Sair")
    print("=" * 40)

def criar_arquivo():
    print("\n--- CRIAR ARQUIVO ---")

    if os.path.exists(ARQUIVO):
        print(f"O arquivo '{ARQUIVO}' já existe.")
        return

    os.makedirs(PASTA, exist_ok=True)

    with open(ARQUIVO, 'w', encoding='utf-8') as f:
        pass

    print(f" Arquivo '{ARQUIVO}' criado com sucesso!")

def modificar_arquivo():
    print("\n--- MODIFICAR ARQUIVO ---")

    if not os.path.exists(ARQUIVO):
        print(f"  O arquivo '{ARQUIVO}' não foi encontrado.")
        print("   Crie o arquivo primeiro (opção 1).")
        return

    linha = input("Digite a linha que deseja adicionar: ")

    with open(ARQUIVO, 'a', encoding='utf-8') as f:
        f.write(linha + '\n')

    print(f" Linha adicionada e arquivo '{ARQUIVO}' fechado com sucesso!")

def excluir_arquivo():
    print("\n--- EXCLUIR ARQUIVO ---")

    if not os.path.exists(ARQUIVO):
        print(f"  O arquivo '{ARQUIVO}' não foi encontrado.")
        return

    confirmacao = input(f"Tem certeza que deseja excluir '{ARQUIVO}'? (s/n): ").strip().lower()

    if confirmacao == 's':
        os.remove(ARQUIVO)
        print(f"  Arquivo '{ARQUIVO}' excluído com sucesso!")
    else:
        print("  Exclusão cancelada.")

def main():
    while True:
        limpar_tela()
        exibir_menu()

        opcao = input("\n  Escolha uma opção: ").strip()

        if opcao == '1':
            criar_arquivo()
        elif opcao == '2':
            modificar_arquivo()
        elif opcao == '3':
            excluir_arquivo()
        elif opcao == '0':
            print("\n  Encerrando o Editor de Texto. Até logo!")
            break
        else:
            print("\n⚠  Opção inválida. Tente novamente.")

        input("\n  Pressione ENTER para voltar ao menu...")

if __name__ == "__main__":
    main()
