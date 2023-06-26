package io.alexdgr8r.diceroll.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DynamicRollRequest {

    @Schema(description = "Positive roll groupings (e.g. \"4d20\").")
    List<RollRequest> positiveRollRequests;
    @Schema(description = "Negative roll groupings (e.g. \"- 4d20\").")
    List<RollRequest> negativeRollRequests;
    @Schema(description = "Modifier to all rolls summed up (e.g. the end of \"4d20 + 4\").")
    int mod;

}
