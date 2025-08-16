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

    private long nextId = 0;

    public Investment create(final long tax, final long initialFunds) {
        this.nextId++;
        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet findWalletByAccountPix(final String pix) {
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

    public void updateAmount() {
        wallets.forEach(wallet -> {
            var investment = wallet.getInvestment();
            if (investment != null) {
                wallet.updateAmount(wallet.getInvestment().tax());
            }
        });
    }

    public void withDraw(final String pix, final long funds) {
        var wallet = findWalletByAccountPix(pix);
        var investment = wallet.getInvestment();

        if (investment == null) {
            throw new InvestmentNotFoundException("No investment found for the account: " + pix);
        }
        checkFundsAvailability(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getServiceType(), "Resgate de investimento");

        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id) {
        if (!this.wallets.isEmpty()) {
            var accountsInUse = wallets.stream()
                    .map(InvestmentWallet::getAccount)
                    .toList();

            if (accountsInUse.contains(account)) {
                throw new AccountWithInvestmentException("Account " + account + "already has an investment.");
            }
        }

        var investment = findInvestmentById(id);
        checkFundsAvailability(account, investment.initialFunds());

        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        return wallet;
    }

    public void deposit(final String pix, final long funds) {
        var wallet = findWalletByAccountPix(pix);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getServiceType(), "Depósito de investimento");
    }

    public List<InvestmentWallet> listWallets() {
        return this.wallets;
    }

    public List<Investment> listInvestments() {
        return this.investments;
    }
}

