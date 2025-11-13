package pe.iotteam.plantcare.analytics.domain.model.queries;

import java.time.LocalDateTime;

/**
 * Query to retrieve sensor data within a date range
 */
public record GetSensorDataByDateRangeQuery(LocalDateTime startDate, LocalDateTime endDate) {
    public GetSensorDataByDateRangeQuery {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
}
