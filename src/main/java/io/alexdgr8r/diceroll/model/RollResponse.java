package io.alexdgr8r.diceroll.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RollResponse {

    List<Integer> rolls;
    int sum;
    double mean;
    int max;
    int min;
    int times;
    int numSides;
    boolean crit;

}
