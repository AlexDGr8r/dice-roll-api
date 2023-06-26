package io.alexdgr8r.diceroll.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@Schema(
    title = "DiceRoll",
    description = "The result of a single set of rolls for a dice with specified number of sides. (e.g. \"4d20\")"
)
public class RollResponse {

    @Schema(description = "The numeric value of each roll of the dice.")
    List<Integer> rolls;
    @Schema(description = "The sum of all dice rolls.")
    int sum;
    @Schema(description = "The average/mean of all the dice rolls.")
    double mean;
    @Schema(description = "Maximum roll from all dice rolls.")
    int max;
    @Schema(description = "Minimum roll from all dice rolls.")
    int min;
    @Schema(description = "The number of times the dice was rolled.")
    int times;
    @Schema(description = "The number of sides the dice has.")
    int numSides;
    @Schema(description = "If any roll of the dice was the maximum value of the dice.")
    boolean crit;

}
