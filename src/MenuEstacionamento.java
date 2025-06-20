
import controller.Estacionamento;
import java.util.Scanner;
import models.*;

public class MenuEstacionamento {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estacionamento estacionamento = new Estacionamento(20); // Cria estacionamento com 20 vagas
        
        while (true) {
            System.out.println(" 🚗 Bem-vindo ao Estacionamento 🚗");
            System.out.println("==========================================");
            System.out.println("1 - Registrar Entrada");
            System.out.println("2 - Registrar Saída");
            System.out.println("3 - Ver Vagas Disponíveis");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção (1 a 4): ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    registrarEntrada(scanner, estacionamento);
                    break;
                case "2":
                    registrarSaida(scanner, estacionamento);
                    break;
                case "3":
                    estacionamento.exibirVagasDisponiveis();
                    break;
                case "4":
                    System.out.println("Encerrando o sistema. Até logo!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }
    // metodo voltado para registrar a entrada de um veículo no estacionamento

    private static void registrarEntrada(Scanner scanner, Estacionamento estacionamento) {
        System.out.println("Registrando entrada de veículo");
        estacionamento.exibirVagasDisponiveis();
        
        System.out.println("Escolha o número da vaga desejada: ");
        int numeroVaga = scanner.nextInt();
        
        System.out.println("Nome do cliente: ");
        String nome = scanner.nextLine();
        
        System.out.println("CPF do cliente: ");
        String cpf = scanner.nextLine();
        
        System.out.println("Placa do veículo: ");
        String placa = scanner.nextLine();
        
        System.out.println("Modelo do veículo: ");
        String modelo = scanner.nextLine();

        // Criando uma pessoa (funcionário ou visitante)
        Pessoa pessoa = criarPessoa(scanner);

        Veiculo veiculo = new Veiculo(placa, modelo);
        
        if (estacionamento.registrarEntrada(pessoa, veiculo, numeroVaga)) {
            System.out.println("Entrada registrada com sucesso na vaga " + numeroVaga);
        } else {
            System.out.println("Erro: Vaga " + numeroVaga + " não está disponível");
        }
    }

    private static void registrarSaida(Scanner scanner, Estacionamento estacionamento) {
        System.out.println("Registrando a saída de veículo");
        System.out.println("Informe a placa do veículo: ");

        String placa = scanner.nextLine();

        if (placa.isEmpty()) {
            System.out.println("Placa não pode ser vazia.");
            return;
        }

        estacionamento.registrarSaida(placa);
    }

    private static Pessoa criarPessoa(Scanner scanner) {
        System.out.println("Tipo de pessoa:");
        System.out.println("1 - Funcionário");
        System.out.println("2 - Visitante");
        System.out.print("Escolha: ");
        int tipo = scanner.nextInt();

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Documento (CPF): ");
        String documento = scanner.nextLine();

        if (tipo == 1) {
            System.out.println("Matrícula: ");
            int matricula = scanner.nextInt();
            return new Funcionario(nome, documento, tipo);
        } else {
            return new Visitante(nome, documento);
        }
    }
}
