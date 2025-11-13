package pe.iotteam.plantcare.analytics.domain.exceptions;

/**
 * Exception thrown when data ingestion fails
 */
public class DataIngestionException extends RuntimeException {
    public DataIngestionException(String message) {
        super(message);
    }

    public DataIngestionException(String message, Throwable cause) {
        super(message, cause);
    }
}
