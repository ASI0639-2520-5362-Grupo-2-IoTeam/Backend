package pe.iotteam.plantcare.subscription.interfaces.rest.transform;

import pe.iotteam.plantcare.subscription.domain.model.aggregates.Subscription;
import pe.iotteam.plantcare.subscription.domain.model.valueobjects.SubscriptionStatus;
import pe.iotteam.plantcare.subscription.interfaces.rest.resources.SubscriptionResource;

public class SubscriptionResourceAssembler {

    public static SubscriptionResource toResource(Subscription subscription) {
        if (subscription == null) {
            return new SubscriptionResource(
                    null,
                    null,
                    "NONE",
                    SubscriptionStatus.PENDING,
                    null,
                    null,
                    null
            );
        }

        return new SubscriptionResource(
                subscription.getId(),
                subscription.getUserId().value(),
                subscription.getPlan() != null ? subscription.getPlan().getType().name() : "NONE",
                subscription.getStatus() != null ? subscription.getStatus() : SubscriptionStatus.PENDING,
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getNextBillingDate()
        );
    }
}

