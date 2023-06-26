package io.alexdgr8r.diceroll.controllers;

import io.alexdgr8r.diceroll.model.DynamicRollRequestString;
import io.alexdgr8r.diceroll.model.DynamicRollResponse;
import io.alexdgr8r.diceroll.model.RollRequest;
import io.alexdgr8r.diceroll.model.RollResponse;
import io.alexdgr8r.diceroll.services.Roller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/roll")
public class RollController {

    private final Roller roller;

    public RollController(Roller roller) {
        this.roller = roller;
    }

    @GetMapping
    public Mono<RollResponse> singleRoll() {
        return Mono.fromSupplier(() -> RollRequest.of(1, 20))
                .map(roller::roll);
    }

    @GetMapping("/{numTimes}")
    public Mono<RollResponse> multipleRollD20(@PathVariable @Min(1) int numTimes) {
        return Mono.fromSupplier(() -> RollRequest.of(numTimes, 20))
                .map(roller::roll);
    }

    @GetMapping("/d/{numSides}")
    public Mono<RollResponse> singleRollSidesOnly(@PathVariable @Min(1) int numSides) {
        return Mono.fromSupplier(() -> RollRequest.of(1, numSides))
                .map(roller::roll);
    }

    @GetMapping("/{numTimes}/d/{numSides}")
    public Mono<RollResponse> multipleRolls(@PathVariable @Min(1) int numTimes, @PathVariable @Min(1) int numSides) {
        return Mono.fromSupplier(() -> RollRequest.of(numTimes, numSides))
                .map(roller::roll);
    }

    @GetMapping("/notation/{rollRequest}")
    public Mono<DynamicRollResponse> dynamicRoll(@PathVariable @NotNull @NotBlank String rollRequest) {
        return Mono.fromSupplier(() -> roller.parse(rollRequest))
                .map(roller::roll);
    }

    @PostMapping
    public Mono<DynamicRollResponse> dynamicRoll(@Valid @RequestBody DynamicRollRequestString request) {
        return Mono.fromSupplier(() -> roller.parse(request.request()))
                .map(roller::roll);
    }

}
