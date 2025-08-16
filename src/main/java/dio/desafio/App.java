package dio.desafio;

import dio.desafio.exception.AccountNotFoundException;
import dio.desafio.exception.NoFundsEnoughException;
import dio.desafio.exception.WalletNotFoundException;
import dio.desafio.model.AccountWallet;
import dio.desafio.repository.AccountRepository;
import dio.desafio.repository.InvestmentRepository;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public class App {

    public static Scanner scanner = new Scanner(System.in);
    public final static AccountRepository accountRepository = new AccountRepository();
    public final static InvestmentRepository investmentRepository = new InvestmentRepository();

    public static void main(String[] args) {
        System.out.println("Welcome to the DIO Investment Challenge!");

        while (true) {
            System.out.println("Selecione uma opção:");
            System.out.println("1 - Criar uma conta");
            System.out.println("2 - Criar um investimento");
            System.out.println("3 - Criar Carteria de investimento");
            System.out.println("4 - Depositar na conta");
            System.out.println("5 - Saca da conta");
            System.out.println("6 - Tranferência entre contas");
            System.out.println("7 - Investir");
            System.out.println("8 - Sacar investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimento");
            System.out.println("12 - Atualizar investimentos");
            System.out.println("13 - Historico de conta");
            System.out.println("14 - Sair");

            var option = scanner.nextInt();

            switch (option) {
                case 1 -> createAccount();
                case 2 -> createInvestment();
                case 3 -> createWalletInvestment();
                case 4 -> deposit();
                case 5 -> withdrawFromAccount();
                case 6 -> transferToAccount();
                case 7 -> incInvestment();
                case 8 -> withdrawFromInvestment();
                case 9 -> accountRepository.list().forEach(System.out::println);
                case 10 -> investmentRepository.listInvestments().forEach(System.out::println);
                case 11 -> investmentRepository.listWallets().forEach(System.out::println);
                case 12 -> {
                    investmentRepository.updateAmount();
                    System.out.println("Carteiras de investimento atualizadas.");
                }
                case 13 -> checkHistory();
                case 14 -> System.exit(0);
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void checkHistory() {
        System.out.println("Informe a chave pix da conta para verificar o extrato: ");
        var pix = scanner.next();
        try {
            var sortedHistory = accountRepository.getHistory(pix);
            sortedHistory.forEach((k, v) -> {
                System.out.println(k.format(ISO_DATE_TIME));
                System.out.println(v.getFirst().transactionId());
                System.out.println(v.getFirst().description());
                System.out.println("R$" + (v.size() / 100) + "," + (v.size() % 100));
            });
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void withdrawFromInvestment() {
        System.out.println("Informe a chave pix da conta para resgate do investimento: ");
        var pix = scanner.next();

        System.out.println("Informe o valor que será sacado :");
        var amount = scanner.nextLong();

        try {
            investmentRepository.withDraw(pix, amount);
        } catch (NoFundsEnoughException | AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void incInvestment() {
        System.out.println("Informe a chave pix da conta para investimento: ");
        var pix = scanner.next();

        System.out.println("Informe o valor que será investido: ");
        var amout = scanner.nextLong();

        try {
            investmentRepository.deposit(pix, amout);
        } catch (WalletNotFoundException | AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createWalletInvestment() {
        System.out.println("Informe a chave Pix da conta: ");
        var pix =  scanner.next();
        var account = accountRepository.findByPix(pix);

        System.out.println("Informe o identificador do investimento: ");
        var investmentId = scanner.nextLong();
        var investmentWallet = investmentRepository.initInvestment(account, investmentId);

        System.out.println("Investmento criado com sucesso: " + investmentWallet);
    }

    private static void createAccount() {
        System.out.println("Crie suas chaves PIX (separadas por ';'): ");
        var pix = Arrays.stream(scanner.next().split(";")).toList();

        System.out.println("Digite o valor inicial de depósito:");
        var initialAmount = scanner.nextLong();

        var wallet = accountRepository.create(pix, initialAmount);
        System.out.println("Conta criada com sucesso: " + wallet);
    }

    private static void createInvestment() {
        System.out.println("Informe a taxa de rendimento (em %):");
        var tax = scanner.nextInt();

        System.out.println("Informe o valor incial do investimento:");
        var initialFunds = scanner.nextLong();

        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println("Investimento criado com sucesso: " + investment);
    }

    private static void deposit() {
        System.out.println("Digite a chave PIX da conta para depósito:");
        var pix = scanner.next();

        System.out.println("Digite o valor a ser depositado:");
        var amount = scanner.nextLong();

        accountRepository.deposit(pix, amount);
        System.out.println("Depósito realizado com sucesso.");
    }

    private static void withdrawFromAccount() {
        System.out.println("Digite a chave PIX da conta para saque:");
        var pix = scanner.next();

        System.out.println("Digite o valor a ser sacado:");
        var fundsAmount = scanner.nextLong();

        try {
            var withdrawnAmount = accountRepository.withDraw(pix, fundsAmount);
            System.out.println("Saque realizado com sucesso. Valor sacado: " + withdrawnAmount / 100);
        } catch (NoFundsEnoughException | AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void transferToAccount() {
        System.out.println("Digite a chave PIX da conta de origem: ");
        var source = scanner.next();

        System.out.println("Digite a chave PIX da conta de destino: ");
        var target = scanner.next();

        System.out.println("Digite o valor a ser transferido:");
        var amount = scanner.nextLong();

        try {
            accountRepository.transferMoney(source, target, amount);
        } catch (NoFundsEnoughException | AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
