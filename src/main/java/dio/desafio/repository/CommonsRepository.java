package dio.desafio.repository;

import dio.desafio.exception.NoFundsEnoughException;
import dio.desafio.model.AccountWallet;
import dio.desafio.model.BankService;
import dio.desafio.model.Money;
import dio.desafio.model.MoneyAudit;
import dio.desafio.model.Wallet;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class CommonsRepository {
    public static void checkFundsAvailability(final Wallet source, final long amount) {
        if (source.getFunds() < amount) {
            throw new NoFundsEnoughException("Insufficient funds for the transaction.");
        }
    }

    public static List<Money> generateMoney(final UUID transactionId, final long funds, final String description) {
        var history = new MoneyAudit(
                transactionId,
                BankService.ACCOUNT,
                description,
                OffsetDateTime.now()
        );

        return Stream.generate(() -> new Money(history)).limit(funds).toList();
    }

}
