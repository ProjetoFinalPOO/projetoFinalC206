import java.util.Scanner;

public class MenuEstacionamento {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Exibe o menu principal do estacionamento
        System.out.println("🚗 Bem-vindo ao Estacionamento Grupo 2MNT 🚗");
        System.out.println("==========================================");
        System.out.println("1 - Registrar Entrada");
        System.out.println("2 - Registrar Saída");
        System.out.println("3 - Sair");
        System.out.print("Escolha uma opção (1 a 3): ");

        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                System.out.println("Você escolheu: Registrar Entrada");
                break;
            case "2":
                System.out.println("Você escolheu: Registrar Saída");
                break;
            case "3":
                System.out.println("Encerrando o sistema. Até logo!");
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }

        scanner.close();
    }
}