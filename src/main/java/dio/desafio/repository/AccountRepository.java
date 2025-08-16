package dio.desafio.repository;

import dio.desafio.exception.AccountNotFoundException;
import dio.desafio.exception.PixInUseException;
import dio.desafio.model.AccountWallet;
import dio.desafio.model.MoneyAudit;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dio.desafio.repository.CommonsRepository.checkFundsAvailability;

public class AccountRepository {
    private List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long initialAmount) {
        if (!accounts.isEmpty()) {
            var pixInUse = accounts.stream()
                    .flatMap(account -> account.getPix().stream()).toList();
            for (var p : pix) {
                if (pixInUse.contains(p)) {
                    throw new PixInUseException("A chave PIX " + p + " já está em uso.");
                }
            }
        }

        var newAccount = new AccountWallet(initialAmount, pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount) {
        var accountTarget = findByPix(pix);
        accountTarget.addMoney(fundsAmount, "Depósito a vista");
    }

    public long withDraw(final String pix, final long amount) {
        var accountSource = findByPix(pix);
        checkFundsAvailability(accountSource, amount);
        accountSource.reduceMoney(amount);
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount) {
        if (sourcePix.equals(targetPix)) {
            throw new IllegalArgumentException("Cannot transfer money to the same account.");
        }
        var source = findByPix(sourcePix);
        checkFundsAvailability(source, amount);

        var target = findByPix(targetPix);
        var message = "Transferência de " + amount + " da conta " + sourcePix + " para a conta " + targetPix;
        target.addMoney(source.reduceMoney(amount), source.getServiceType(), message);
    }

    public AccountWallet findByPix(final String pix) {
        return accounts.stream()
                .filter(account -> account.getPix().contains(pix))
                .findFirst()
                .orElseThrow(() ->
                        new AccountNotFoundException("A conta com a chave PIX " + pix + " não foi encontrada."));
    }

    public List<AccountWallet> list() {
        return this.accounts;
    }

    public Map<OffsetDateTime, List<MoneyAudit>> getHistory(String pix) {
        var wallet = findByPix(pix);
        var audit = wallet.getFinancialTransactions();

        return audit.stream()
                .collect(Collectors.groupingBy( t-> t.createdAt().truncatedTo(ChronoUnit.SECONDS)));


//        return account.getFinancialTransactions().stream()
//                .collect(Collectors.groupingBy(MoneyAudit::createdAt, TreeMap::new, Collectors.toList()));
    }
}
