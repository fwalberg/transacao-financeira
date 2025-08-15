package dio.desafio.repository;

import dio.desafio.exception.AccountNotFoundException;
import dio.desafio.exception.PixInUseException;
import dio.desafio.model.AccountWallet;

import java.util.List;

import static dio.desafio.repository.CommonsRepository.checkFundsAvailability;

public class AccountRepository {
    private List<AccountWallet> accounts;

    public AccountWallet create(final List<String> pix, final long initialAmount) {
        var pixInUse = accounts.stream()
                .flatMap(account -> account.getPix().stream())
                .toList();
        for (var p : pix) {
            if (pixInUse.contains(p)) {
                throw new PixInUseException("A chave PIX " + p + " já está em uso.");
            }
        }
        var newAccount = new AccountWallet(initialAmount, pix);
        if (accounts == null) {
            accounts = new java.util.ArrayList<>();
        }
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount) {
        var accountTarget = findBydPix(pix);
        accountTarget.addMoney(fundsAmount, "Depósito na conta");
    }

    public long withDraw(final String pix, final long fundsAmount) {
        var accountSource = findBydPix(pix);
        checkFundsAvailability(accountSource, fundsAmount);
        var moneyToRemove = accountSource.reduceMoney(fundsAmount);
        accountSource.addMoney(moneyToRemove, accountSource.getServiceType(), "Saque da conta");
        return fundsAmount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount) {
        if (sourcePix.equals(targetPix)) {
            throw new IllegalArgumentException("Cannot transfer money to the same account.");
        }
        var source = findBydPix(sourcePix);
        checkFundsAvailability(source, amount);

        var target = findBydPix(targetPix);
        var message = "Transferência de " + amount + " da conta " + sourcePix + " para a conta " + targetPix;
        target.addMoney(source.reduceMoney(amount), source.getServiceType(), message);
    }

    public AccountWallet findBydPix(final String pix) {
        return accounts.stream()
                .filter(account -> account.getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("A conta coma chave PIX " + pix + " não foi encontrada."));
    }

    public List<AccountWallet> list() {
        return accounts;
    }
}
