package io.alexdgr8r.diceroll.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DynamicRollRequestString(@NotNull @NotBlank String request) { }
