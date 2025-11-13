package pe.iotteam.plantcare.analytics.domain.model.commands;

/**
 * Command to trigger sensor data ingestion from external API
 */
public record IngestSensorDataCommand() {
    // This is a trigger command, no parameters needed
}
