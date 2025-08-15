package dio.desafio.model;

import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@ToString
public abstract class Wallet {

    @Getter
    private final BankService serviceType;

    protected final List<Money> money;

    protected Wallet(BankService serviceType) {
        this.serviceType = serviceType;
        this.money = new ArrayList<>();
    }

    protected  List<Money> generateMoney(final long amount, final String description) {
        var moneyAudit = new MoneyAudit(
                UUID.randomUUID(),
                serviceType,
                description,
                OffsetDateTime.now()
        );

        return Stream.generate(() -> new Money(moneyAudit))
                .limit(amount)
                .toList();
    }

    public long getFunds() {
        return money.size();
    }

    public void addMoney(final List<Money> money, final BankService service, final String description) {
        var history = new MoneyAudit(
                UUID.randomUUID(),
                service,
                description,
                OffsetDateTime.now()
        );

        money.forEach(m -> m.addHistory(history));

        this.money.addAll(money);
    }

    public List<Money> reduceMoney(final long amount) {
        List<Money> toRemove = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            toRemove.add(this.money.removeFirst());
        }

        return toRemove;
    }

    public List<MoneyAudit> getFinancialTransactions() {
        return this.money.stream()
                .flatMap(m -> m.getHistory().stream())
                .toList();
    }


}
