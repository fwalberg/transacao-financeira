package dio.desafio.exception;

public class AccountWithInvestmentException extends RuntimeException {
    public AccountWithInvestmentException(String message) {
        super(message);
    }

    public AccountWithInvestmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
