package pe.iotteam.plantcare.subscription.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "User Subscriptions",
        description = "Endpoints para gestionar el ciclo de vida de las suscripciones de usuarios, "
                + "incluyendo creación, consulta, cancelación, reactivación y actualización del plan."
)
public class SubscriptionController {

    private final SubscriptionCommandService commandService;
    private final SubscriptionQueryService queryService;

    public SubscriptionController(SubscriptionCommandService commandService,
                                  SubscriptionQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /**
     * Crear o actualizar una suscripción
     */
    @Operation(
            summary = "Crear o actualizar una suscripción",
            description = "Permite registrar una nueva suscripción o cambiar el plan actual de un usuario existente. "
                    + "Si el usuario ya posee una suscripción, se actualiza con el nuevo tipo de plan enviado en la solicitud."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción creada o actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping
    public ResponseEntity<SubscriptionResource> subscribe(@RequestBody CreateSubscriptionResource resource) {
        Subscription subscription = commandService.subscribeOrChangePlan(resource.userId(), resource.planType());
        return ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription));
    }

    /**
     * Cancelar una suscripción activa
     */
    @Operation(
            summary = "Cancelar una suscripción activa",
            description = "Cambia el estado de la suscripción del usuario a 'CANCELLED'. "
                    + "Esta operación se utiliza cuando el usuario decide interrumpir su suscripción actual. "
                    + "No elimina el registro, solo actualiza su estado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Suscripción o usuario no encontrado")
    })
    @PostMapping("/{userId}/cancelled")
    public ResponseEntity<SubscriptionResource> cancel(@PathVariable UUID userId) {
        Subscription subscription = commandService.cancelSubscription(userId);
        return ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription));
    }

    /**
     * Reactivar una suscripción cancelada
     */
    @Operation(
            summary = "Reactivar una suscripción cancelada",
            description = "Cambia el estado de la suscripción del usuario a 'ACTIVE'. "
                    + "Permite al usuario reanudar una suscripción que previamente había sido cancelada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción reactivada correctamente"),
            @ApiResponse(responseCode = "404", description = "Suscripción o usuario no encontrado")
    })
    @PostMapping("/{userId}/active")
    public ResponseEntity<SubscriptionResource> reactivate(@PathVariable UUID userId) {
        Subscription subscription = commandService.reactivateSubscription(userId);
        return ResponseEntity.ok(SubscriptionResourceAssembler.toResource(subscription));
    }

    /**
     * Obtener la suscripción de un usuario
     */
    @Operation(
            summary = "Obtener la suscripción de un usuario",
            description = "Devuelve la información completa de la suscripción asociada a un usuario específico, "
                    + "incluyendo su estado actual y tipo de plan."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción encontrada"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<SubscriptionResource> getByUser(@PathVariable UUID userId) {
        return queryService.getByUserId(userId)
                .map(sub -> ResponseEntity.ok(SubscriptionResourceAssembler.toResource(sub)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Listar todas las suscripciones (solo administradores)
     */
    @Operation(
            summary = "Listar todas las suscripciones",
            description = "Devuelve una lista con todas las suscripciones registradas en el sistema. "
                    + "Este endpoint está pensado para ser utilizado por administradores o servicios internos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de suscripciones obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<SubscriptionResource>> getAll() {
        var list = queryService.getAllSubscriptions().stream()
                .map(SubscriptionResourceAssembler::toResource)
                .toList();
        return ResponseEntity.ok(list);
    }
}