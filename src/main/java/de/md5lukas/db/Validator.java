package de.md5lukas.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class Validator {

    private Validator() {
    }

    public static void checkNotNull(@Nullable Object o, @NotNull String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }

    public static void checkArgument(boolean expression, @NotNull String message, @NotNull Object... parameters) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, parameters));
        }
    }
}
