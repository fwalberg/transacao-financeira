package dio.desafio.model;

import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static dio.desafio.model.BankService.INVESTMENT;

@ToString
@Getter
public class InvestmentWallet extends Wallet{
    private final Investment investment;
    private final AccountWallet account;

    public InvestmentWallet(final Investment investment, final AccountWallet account, final long amount) {
        super(INVESTMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount), getServiceType(), "Investmento");
    }

    public void updateAmount(final long percentage) {
        var amount = (long) (getFunds() * (percentage / 100.0));
        var history = new MoneyAudit(
                UUID.randomUUID(),
                getServiceType(),
                "rendimentos",
                OffsetDateTime.now()
        );
        var money = Stream
                .generate(() -> new Money(history))
                .limit(amount)
                .toList();

        this.money.addAll(money);
    }
}
