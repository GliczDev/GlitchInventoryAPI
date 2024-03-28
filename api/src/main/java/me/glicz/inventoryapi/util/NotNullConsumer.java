package me.glicz.inventoryapi.util;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * NotNull version of {@link Consumer}. Accepting using {@link NotNullConsumer#acceptIfNotNull(Object)} ignores null values.
 *
 * @param <T> the type of the input to the operation
 * @since 2.0
 */
@FunctionalInterface
public interface NotNullConsumer<T> extends Consumer<T> {
    /**
     * Performs this operation on the given argument if not null.
     *
     * @param t the input argument
     */
    default void acceptIfNotNull(@Nullable T t) {
        if (t == null) return;
        accept(t);
    }
}
