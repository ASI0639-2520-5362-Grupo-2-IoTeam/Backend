package pe.iotteam.plantcare.subscription.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.subscription.application.internal.commandservices.SubscriptionCommandService;
import pe.iotteam.plantcare.subscription.application.internal.queryservices.SubscriptionQueryService;
import pe.iotteam.plantcare.subscription.domain.model.aggregates.Subscription;
import pe.iotteam.plantcare.subscription.interfaces.rest.resources.CreateSubscriptionResource;
import pe.iotteam.plantcare.subscription.interfaces.rest.resources.SubscriptionResource;
import pe.iotteam.plantcare.subscription.interfaces.rest.transform.SubscriptionResourceAssembler;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionCommandService commandService;
    private final SubscriptionQueryService queryService;

    public SubscriptionController(SubscriptionCommandService commandService,
                                  SubscriptionQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /**
     * Registrar o actualizar una suscripción
     */
    @PostMapping
    public ResponseEntity<SubscriptionResource> subscribe(@RequestBody CreateSubscriptionResource resource) {
        Subscription subscription = commandService.subscribeOrChangePlan(resource.userId(), resource.planType());
        return ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription));
    }

    /**
     * Cancelar una suscripción activa
     */
    @PostMapping("/{userId}/cancel")
    public ResponseEntity<SubscriptionResource> cancel(@PathVariable UUID userId) {
        Subscription subscription = commandService.cancelSubscription(userId);
        return ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription));
    }

    /**
     * Reactivar una suscripción cancelada
     */
    @PostMapping("/{userId}/reactivate")
    public ResponseEntity<SubscriptionResource> reactivate(@PathVariable UUID userId) {
        Subscription subscription = commandService.reactivateSubscription(userId);
        return ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription));
    }

    /**
     * Obtener la suscripción de un usuario
     */
    @GetMapping("/{userId}")
    public ResponseEntity<SubscriptionResource> getByUser(@PathVariable UUID userId) {
        return queryService.getByUserId(userId)
                .map(sub -> ResponseEntity.ok(SubscriptionResourceAssembler.toResource(sub)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Listar todas las suscripciones (solo admins)
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionResource>> getAll() {
        var list = queryService.getAllSubscriptions().stream()
                .map(SubscriptionResourceAssembler::toResource)
                .toList();
        return ResponseEntity.ok(list);
    }
}
