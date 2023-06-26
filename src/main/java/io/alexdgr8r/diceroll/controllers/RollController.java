package io.alexdgr8r.diceroll.controllers;

import io.alexdgr8r.diceroll.model.DynamicRollRequestString;
import io.alexdgr8r.diceroll.model.DynamicRollResponse;
import io.alexdgr8r.diceroll.model.RollRequest;
import io.alexdgr8r.diceroll.model.RollResponse;
import io.alexdgr8r.diceroll.services.Roller;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/roll")
@Tag(name = "Dice Roll")
public class RollController {

    private final Roller roller;

    public RollController(Roller roller) {
        this.roller = roller;
    }

    /**
     * Rolls a single 20-sided dice.
     * @return The result of the roll.
     */
    @GetMapping
    public Mono<RollResponse> singleRoll() {
        return Mono.fromSupplier(() -> RollRequest.of(1, 20))
                .map(roller::roll);
    }

    /**
     * Rolls a 20-sided dice the specified number of times.
     * @param numTimes The number of times to roll the 20-sided dice.
     * @return The result of the rolls.
     */
    @GetMapping("/{numTimes}")
    public Mono<RollResponse> multipleRollD20(@PathVariable @Min(1) int numTimes) {
        return Mono.fromSupplier(() -> RollRequest.of(numTimes, 20))
                .map(roller::roll);
    }

    /**
     * Rolls a single dice with the specified number of sides.
     * @param numSides The number of sides of the dice to roll.
     * @return The result of the roll.
     */
    @GetMapping("/d/{numSides}")
    public Mono<RollResponse> singleRollSidesOnly(@PathVariable @Min(1) int numSides) {
        return Mono.fromSupplier(() -> RollRequest.of(1, numSides))
                .map(roller::roll);
    }

    /**
     * Rolls die with a specified number of times and sides.
     * @param numTimes The number of times the dice will be rolled.
     * @param numSides The number of sides of the dice to be rolled.
     * @return The result of the rolls.
     */
    @GetMapping("/{numTimes}/d/{numSides}")
    public Mono<RollResponse> multipleRolls(@PathVariable @Min(1) int numTimes, @PathVariable @Min(1) int numSides) {
        return Mono.fromSupplier(() -> RollRequest.of(numTimes, numSides))
                .map(roller::roll);
    }

    /**
     * Use typical dice roll notation (e.g. {@code 3d4 + 3}) to roll a set of dice a specified
     * number of times, to be summed with specified modifiers.
     * @param notation Typical dice roll notation (e.g. {@code 3d4 + 3}).
     * @return The result of all positive and negative rolls, combined with modifiers.
     */
    @GetMapping("/notation/{notation}")
    public Mono<DynamicRollResponse> dynamicRoll(@PathVariable @NotNull @NotBlank String notation) {
        return Mono.fromSupplier(() -> roller.parse(notation))
                .map(roller::roll);
    }

    /**
     * Use typical dice roll notation (e.g. {@code 3d4 + 3}) to roll a set of dice a specified
     * number of times, to be summed with specified modifiers.
     * @param request Request containing typical dice roll notation (e.g. {@code 3d4 + 3}).
     * @return The result of all positive and negative rolls, combined with modifiers.
     */
    @PostMapping
    public Mono<DynamicRollResponse> dynamicRoll(@Valid @RequestBody DynamicRollRequestString request) {
        return Mono.fromSupplier(() -> roller.parse(request.request()))
                .map(roller::roll);
    }

}
