package pe.iotteam.plantcare.analytics.infrastructure.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Custom deserializer for RFC 1123 date format (Sun, 09 Nov 2025 16:11:46 GMT)
 * Converts it to LocalDateTime
 */
public class Rfc1123DateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter RFC_1123_FORMATTER = 
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        String dateString = parser.getText();
        
        try {
            // Parse as ZonedDateTime first to handle the timezone
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, RFC_1123_FORMATTER);
            // Convert to LocalDateTime
            return zonedDateTime.toLocalDateTime();
        } catch (Exception e) {
            // If it fails, try parsing as ISO format as fallback
            try {
                return LocalDateTime.parse(dateString);
            } catch (Exception ex) {
                throw new IOException("Unable to parse date: " + dateString, e);
            }
        }
    }
}
