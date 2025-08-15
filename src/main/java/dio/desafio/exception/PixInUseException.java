package dio.desafio.exception;

public class PixInUseException extends RuntimeException {
    public PixInUseException(String message) {
        super(message);
    }

    public PixInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
