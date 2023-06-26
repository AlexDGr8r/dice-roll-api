package io.alexdgr8r.diceroll.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@Schema(title = "NotationResponse", description = "The result of a notation-based request.")
public class DynamicRollResponse {

    @Schema(description = "Positive roll groupings (e.g. \"4d20\").")
    List<RollResponse> positiveRolls;
    @Schema(description = "Negative roll groupings (e.g. \"- 4d20\").")
    List<RollResponse> negativeRolls;
    @Schema(description = "Sum of all positive and negative rolls without the modifier.")
    int sum;
    @Schema(description = "Modifier to all rolls summed up (e.g. the end of \"4d20 + 4\").")
    int modifier;
    @Schema(description = "Sum of all postive and negative rolls with the modifier.")
    int sumWithModifier;

}
