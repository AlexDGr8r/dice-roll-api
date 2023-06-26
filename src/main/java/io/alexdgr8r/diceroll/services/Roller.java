package io.alexdgr8r.diceroll.services;

import io.alexdgr8r.diceroll.model.DynamicRollRequest;
import io.alexdgr8r.diceroll.model.DynamicRollResponse;
import io.alexdgr8r.diceroll.model.RollRequest;
import io.alexdgr8r.diceroll.model.RollResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public final class Roller {

    private static final Pattern rollPattern = Pattern.compile("[+-]?\\s*(\\d+)(d(\\d+))?");
    private final RandomGenerator rand = RandomGenerator.getDefault();

    public RollResponse roll(RollRequest request) {
        List<Integer> rolls = new ArrayList<>();
        int max = 0;
        int min = request.getNumSides();
        int sum = 0;
        for (int i = 0; i < request.getNumTimes(); i++) {
            int roll = rand.nextInt(request.getNumSides()) + 1;
            if (roll > max) {
                max = roll;
            }
            if (roll < min) {
                min = roll;
            }
            sum += roll;
            rolls.add(roll);
        }
        double mean = rolls.stream().mapToInt(Integer::intValue).average().orElse(0);
        return RollResponse.builder()
                .rolls(rolls)
                .max(max)
                .min(min)
                .sum(sum)
                .mean(mean)
                .times(request.getNumTimes())
                .numSides(request.getNumSides())
                .crit(max == request.getNumSides())
                .build();
    }

    public DynamicRollResponse roll(DynamicRollRequest request) {
        List<RollResponse> posRolls = request.getPositiveRollRequests().stream().map(this::roll).toList();
        List<RollResponse> negRolls = request.getNegativeRollRequests().stream().map(this::roll).toList();
        int sum = posRolls.stream().mapToInt(RollResponse::getSum).sum() -
                negRolls.stream().mapToInt(RollResponse::getSum).sum();
        return DynamicRollResponse.builder()
                .positiveRolls(posRolls)
                .negativeRolls(negRolls)
                .sum(sum)
                .modifier(request.getMod())
                .sumWithModifier(sum + request.getMod())
                .build();
    }

    public DynamicRollRequest parse(String request) {
        List<RollRequest> posRequests = new ArrayList<>();
        List<RollRequest> negRequests = new ArrayList<>();
        int mod = 0;
        Matcher matcher = rollPattern.matcher(request.trim());
        while (matcher.find()) {
            String group = matcher.group();
            boolean negative = group.startsWith("-");
            int numTimes = Integer.parseInt(matcher.group(1));
            int numSides = Optional.ofNullable(matcher.group(3))
                    .map(Integer::parseInt)
                    .orElse(0);
            if (numSides == 0) {
                mod += negative ? -numTimes : numTimes;
            } else {
                RollRequest rollRequest = RollRequest.of(numTimes, numSides);
                if (negative) {
                    negRequests.add(rollRequest);
                } else {
                    posRequests.add(rollRequest);
                }
            }
        }
        return DynamicRollRequest.builder()
                .positiveRollRequests(posRequests)
                .negativeRollRequests(negRequests)
                .mod(mod)
                .build();
    }

}
