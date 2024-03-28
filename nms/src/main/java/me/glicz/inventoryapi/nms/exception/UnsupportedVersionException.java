package me.glicz.inventoryapi.nms.exception;

public class UnsupportedVersionException extends RuntimeException {
    public UnsupportedVersionException(String version) {
        super("Unsupported version: %s".formatted(version));
    }
}