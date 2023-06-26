package io.alexdgr8r.diceroll.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
    title = "NotationRequest",
    description = "Request for rolling die based on dice notation (e.g. \"4d20 + 4\""
)
public record DynamicRollRequestString(
    @Schema(description = "Request in the form of dice notation.", example = "4d20 + 2d4 - 4")
    @NotNull @NotBlank String request
) { }
