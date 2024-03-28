package me.glicz.inventoryapi.util.margin;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public sealed interface Margin permits MarginImpl {
    @Contract(value = "-> new", pure = true)
    static @NotNull Margin zero() {
        return from(0, 0, 0, 0);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Margin fromTop(int top) {
        return from(top, 0, 0, 0);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Margin fromLeft(int left) {
        return from(0, left, 0, 0);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Margin fromBottom(int bottom) {
        return from(0, 0, bottom, 0);
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Margin fromRight(int right) {
        return from(0, 0, 0, right);
    }

    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull Margin from(int vertical, int horizontal) {
        return from(vertical, horizontal, vertical, horizontal);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull Margin from(int top, int left, int bottom, int right) {
        return new MarginImpl(top, left, bottom, right);
    }

    int top();

    @Contract(value = "_ -> new", pure = true)
    default Margin top(int top) {
        return from(top, left(), bottom(), right());
    }

    int left();

    @Contract(value = "_ -> new", pure = true)
    default Margin left(int left) {
        return from(top(), left, bottom(), right());
    }

    int bottom();

    @Contract(value = "_ -> new", pure = true)
    default Margin bottom(int bottom) {
        return from(top(), left(), bottom, right());
    }

    int right();

    @Contract(value = "_ -> new", pure = true)
    default Margin right(int right) {
        return from(top(), left(), bottom(), right);
    }

    default int sumSlots(int rows) {
        return (left() + right()) * rows + (9 - (left() + right())) * (top() + bottom());
    }
}
