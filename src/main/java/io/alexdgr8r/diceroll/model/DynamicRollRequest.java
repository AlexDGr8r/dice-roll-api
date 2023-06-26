package io.alexdgr8r.diceroll.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DynamicRollRequest {

    List<RollRequest> positiveRollRequests;
    List<RollRequest> negativeRollRequests;
    int mod;

}
