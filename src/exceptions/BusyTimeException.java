package exceptions;

public class BusyTimeException extends RuntimeException {
    public BusyTimeException(String message) {
        super(message);
    }
}
