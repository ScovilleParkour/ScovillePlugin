package com.uhmily.scovilleplugin.Types;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ScovilleObject {

    private final UUID uuid;

    protected ScovilleObject() {
        this.uuid = UUID.randomUUID();
    }

    protected ScovilleObject(UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    public String getType() {
        return this.getClass().getName();
    }

}
