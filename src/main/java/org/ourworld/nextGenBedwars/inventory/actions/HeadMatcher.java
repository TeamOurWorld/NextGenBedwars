package org.ourworld.nextGenBedwars.inventory.actions;

import java.util.function.Predicate;

public record HeadMatcher(String head) implements Predicate<String> {
    @Override
    public boolean test(String string) {
        return string.startsWith(head + ": ");
    }

    public String cropped(String string) {
        if (test(string)) return string.substring(head.length() + 2);
        throw new IllegalArgumentException("String does not match head");
    }
}
