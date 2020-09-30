package com.r3235.collection;

import java.io.Serializable;

public enum MeleeWeapon implements Serializable {
    POWER_SWORD(1),
    CHAIN_AXE(2),
    LIGHTING_CLAW(3),
    POWER_BLADE(4);

    int value;

    MeleeWeapon(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}