package pe.iotteam.plantcare.analytics.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Resource DTO for data ingestion responses
 */
public record IngestionResultResource(
        @JsonProperty("message")
        String message,
        
        @JsonProperty("records_ingested")
        int recordsIngested,
        
        @JsonProperty("success")
        boolean success
) {
    public static IngestionResultResource success(int recordsIngested) {
        return new IngestionResultResource(
                "Data ingestion completed successfully",
                recordsIngested,
                true
        );
    }
    
    public static IngestionResultResource failure(String errorMessage) {
        return new IngestionResultResource(
                errorMessage,
                0,
                false
        );
    }
}
