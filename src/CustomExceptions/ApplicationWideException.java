package CustomExceptions;

public class ApplicationWideException extends Exception{

// Constructor for Data Access Layer Exception
public ApplicationWideException(String message, Throwable cause) {
    super(message, cause);
}   }

