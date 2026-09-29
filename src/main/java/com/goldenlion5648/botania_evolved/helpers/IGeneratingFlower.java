package com.goldenlion5648.botania_evolved.helpers;

public interface IGeneratingFlower {
    default boolean shouldGenerateMana() {
        return true;
    }
}
