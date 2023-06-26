package io.alexdgr8r.diceroll.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DynamicRollResponse {

    List<RollResponse> positiveRolls;
    List<RollResponse> negativeRolls;
    int sum;
    int modifier;
    int sumWithModifier;

}
