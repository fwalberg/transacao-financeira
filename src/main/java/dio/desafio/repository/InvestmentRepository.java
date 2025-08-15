package dio.desafio.repository;

import dio.desafio.exception.AccountWithInvestmentException;
import dio.desafio.exception.InvestmentNotFoundException;
import dio.desafio.exception.WalletNotFoundException;
import dio.desafio.model.AccountWallet;
import dio.desafio.model.Investment;
import dio.desafio.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static dio.desafio.repository.CommonsRepository.checkFundsAvailability;

public class InvestmentRepository {
    private final List<Investment> investments = new ArrayList<>();
    private final List<InvestmentWallet> wallets = new ArrayList<>();

    private long nextId;

    public Investment create(final long tax, final long initialFunds) {
        this.nextId++;
        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet findWalletByAccount(final String pix) {
        return wallets.stream()
                .filter(wallet -> wallet.getAccount().getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for account: " + pix));
    }

    public Investment findInvestmentById(final long id) {
        return investments.stream()
                .filter(investment -> investment.id() == id)
                .findFirst()
                .orElseThrow(() -> new InvestmentNotFoundException("Investment not found with ID: " + id));
    }

    public void updateAmount(final long percentage) {
        wallets.forEach(wallet -> {
            var investment = wallet.getInvestment();
            if (investment != null) {
                wallet.updateAmount(percentage);
            }
        });
    }

    public InvestmentWallet withDrawFromInvestment(final String pix, final long funds) {
        var wallet = findWalletByAccount(pix);
        var investment = wallet.getInvestment();

        if (investment == null) {
            throw new InvestmentNotFoundException("No investment found for the account: " + pix);
        }
        checkFundsAvailability(wallet, funds);
        wallet.reduceMoney(funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getServiceType(), "Saque de investimento");

        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }
        return wallet;
    }

    public InvestmentWallet initializeInvestment(final AccountWallet accountWallet, final long id) {
        var accountsInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();

        if (accountsInUse.contains(accountWallet)) {
            throw new AccountWithInvestmentException("Account " + accountWallet + "already has an investment.");
        }

        var investment = findInvestmentById(id);
        if (investment == null) {
            throw new InvestmentNotFoundException("Investment not found with ID: " + id);
        }

        var wallet = new InvestmentWallet(investment, accountWallet, investment.initialFunds());
        wallets.add(wallet);
        return wallet;
    }

    public InvestmentWallet depositToInvestment(final String pix, final long funds) {
        var wallet = findWalletByAccount(pix);
        var investment = wallet.getInvestment();

        if (investment == null) {
            throw new InvestmentNotFoundException("No investment found for the account: " + pix);
        }
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getServiceType(), "Depósito de investimento");

        return wallet;
    }
}

