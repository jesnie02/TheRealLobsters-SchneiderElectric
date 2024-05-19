package CustomExceptions;

public class ApplicationWideException extends Exception{

// Constructor for Data Access Layer Exception
public ApplicationWideException(String message, Throwable cause) {
    super(message, cause);
}
    public ApplicationWideException(String message) {
        super(message);
    }

    public ApplicationWideException(Throwable exception) {
        super(exception);
    }
}

