package io.alexdgr8r.diceroll.controllers;

import io.alexdgr8r.diceroll.model.DynamicRollRequestString;
import io.alexdgr8r.diceroll.model.DynamicRollResponse;
import io.alexdgr8r.diceroll.model.RollResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RollControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void singleRoll() {
        webTestClient.get().uri("/roll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RollResponse.class)
                .value(resp -> {
                    checkRollResponse(resp);
                    assertEquals(1, resp.getTimes());
                    assertEquals(1, resp.getRolls().size());
                    assertEquals(20, resp.getNumSides());
                });
    }

    @Test
    void multipleRollD20() {
        webTestClient.get().uri("/roll/5")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RollResponse.class)
                .value(resp -> {
                    checkRollResponse(resp);
                    assertEquals(5, resp.getTimes());
                    assertEquals(5, resp.getRolls().size());
                    assertEquals(20, resp.getNumSides());
                });
    }

    @Test
    void singleRollSidesOnly() {
        webTestClient.get().uri("/roll/d/8")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RollResponse.class)
                .value(resp -> {
                    checkRollResponse(resp);
                    assertEquals(1, resp.getTimes());
                    assertEquals(1, resp.getRolls().size());
                    assertEquals(8, resp.getNumSides());
                });
    }

    @Test
    void multipleRolls() {
        webTestClient.get().uri("/roll/4/d/8")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RollResponse.class)
                .value(resp -> {
                    checkRollResponse(resp);
                    assertEquals(4, resp.getTimes());
                    assertEquals(4, resp.getRolls().size());
                    assertEquals(8, resp.getNumSides());
                });
    }

    @Test
    void dynamicRoll_notation() {
        // 4d20+10+2d10-8-1d4
        webTestClient.get().uri("/roll/notation/4d20+10+2d10-8-1d4")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DynamicRollResponse.class)
                .value(resp -> {
                    checkDynamicRollResponse(resp);
                    assertEquals(2, resp.getPositiveRolls().size());
                    assertEquals(1, resp.getNegativeRolls().size());
                    RollResponse fourD20 = resp.getPositiveRolls().get(0);
                    assertEquals(20, fourD20.getNumSides());
                    assertEquals(4, fourD20.getTimes());
                    RollResponse twoD10 = resp.getPositiveRolls().get(1);
                    assertEquals(10, twoD10.getNumSides());
                    assertEquals(2, twoD10.getTimes());
                    RollResponse oneD4 = resp.getNegativeRolls().get(0);
                    assertEquals(4, oneD4.getNumSides());
                    assertEquals(1, oneD4.getTimes());
                    assertEquals(2, resp.getModifier());
                });
    }

    @Test
    void dynamicRoll_post() {
        DynamicRollRequestString req = new DynamicRollRequestString("4d20+10+2d10-8-1d4");
        webTestClient.post().uri("/roll")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(req), DynamicRollRequestString.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DynamicRollResponse.class)
                .value(resp -> {
                    checkDynamicRollResponse(resp);
                    assertEquals(2, resp.getPositiveRolls().size());
                    assertEquals(1, resp.getNegativeRolls().size());
                    RollResponse fourD20 = resp.getPositiveRolls().get(0);
                    assertEquals(20, fourD20.getNumSides());
                    assertEquals(4, fourD20.getTimes());
                    RollResponse twoD10 = resp.getPositiveRolls().get(1);
                    assertEquals(10, twoD10.getNumSides());
                    assertEquals(2, twoD10.getTimes());
                    RollResponse oneD4 = resp.getNegativeRolls().get(0);
                    assertEquals(4, oneD4.getNumSides());
                    assertEquals(1, oneD4.getTimes());
                    assertEquals(2, resp.getModifier());
                });
    }

    void checkRollResponse(RollResponse roll) {
        assertNotNull(roll);
        assertEquals(roll.getRolls().size(), roll.getTimes());
        assertEquals(roll.getRolls().stream().anyMatch(i -> i == roll.getNumSides()), roll.isCrit());
        assertEquals(roll.getRolls().stream().mapToInt(Integer::intValue).sum(), roll.getSum());
        assertEquals(roll.getRolls().stream().mapToInt(Integer::intValue).max().orElse(0), roll.getMax());
        assertEquals(roll.getRolls().stream().mapToInt(Integer::intValue).min().orElse(0), roll.getMin());
        assertEquals(roll.getRolls().stream().mapToInt(Integer::intValue).average().orElse(0), roll.getMean());
        assertTrue(roll.getMin() >= 1 && roll.getMin() <= roll.getNumSides());
        assertTrue(roll.getMax() >= 1 && roll.getMax() <= roll.getNumSides());
    }

    void checkDynamicRollResponse(DynamicRollResponse dynaRoll) {
        assertNotNull(dynaRoll);
        dynaRoll.getPositiveRolls().forEach(this::checkRollResponse);
        dynaRoll.getNegativeRolls().forEach(this::checkRollResponse);
        int sum = dynaRoll.getPositiveRolls().stream().mapToInt(RollResponse::getSum).sum() -
                dynaRoll.getNegativeRolls().stream().mapToInt(RollResponse::getSum).sum();
        assertEquals(dynaRoll.getSum(), sum);
        assertEquals(dynaRoll.getSum() + dynaRoll.getModifier(), dynaRoll.getSumWithModifier());
    }

}