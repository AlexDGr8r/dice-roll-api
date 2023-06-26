package io.alexdgr8r.diceroll.model;

import jakarta.validation.constraints.Min;
import lombok.Value;

@Value(staticConstructor = "of")
public class RollRequest {

    @Min(1)
    int numTimes;
    @Min(1)
    int numSides;

}
