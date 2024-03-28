package me.glicz.inventoryapi.factory;

import me.glicz.inventoryapi.*;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.Map;

public class GlitchInventoryFactoryImpl implements GlitchInventoryFactory {
    private final Map<Class<? extends GlitchInventory<?>>, DefaultInventoryFactory<?>> defaultFactoryMap = new HashMap<>();
    private final Map<Class<? extends GlitchInventory<?>>, RowsInventoryFactory<?>> rowsFactoryMap = new HashMap<>();
    private final Map<Class<? extends GlitchInventory<?>>, TypeInventoryFactory<?>> typeFactoryMap = new HashMap<>();

    public GlitchInventoryFactoryImpl() {
        registerFactory(
                SimpleGlitchInventory.class,
                DefaultInventoryFactory.failed(SimpleGlitchInventory.class),
                SimpleGlitchInventoryImpl::new,
                SimpleGlitchInventoryImpl::new
        );
        registerFactory(
                PaginatedGlitchInventory.class,
                DefaultInventoryFactory.failed(PaginatedGlitchInventory.class),
                PaginatedGlitchInventoryImpl::new,
                PaginatedGlitchInventoryImpl::new
        );
        registerFactory(
                MerchantGlitchInventory.class,
                MerchantGlitchInventoryImpl::new,
                RowsInventoryFactory.failed(MerchantGlitchInventory.class),
                TypeInventoryFactory.failed(MerchantGlitchInventory.class)
        );
    }

    public <T extends GlitchInventory<T>> void registerFactory(Class<T> clazz, DefaultInventoryFactory<T> defaultFactory,
                                                               RowsInventoryFactory<T> rowsFactory, TypeInventoryFactory<T> typeFactory) {
        defaultFactoryMap.put(clazz, defaultFactory);
        rowsFactoryMap.put(clazz, rowsFactory);
        typeFactoryMap.put(clazz, typeFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GlitchInventory<T>> T newInventory(Class<T> clazz) throws InvalidInventoryType {
        if (!defaultFactoryMap.containsKey(clazz)) {
            throw new IllegalArgumentException();
        }
        return (T) defaultFactoryMap.get(clazz).create();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GlitchInventory<T>> T newInventory(Class<T> clazz, @Range(from = 0, to = 6) int rows) throws InvalidInventoryType {
        if (!rowsFactoryMap.containsKey(clazz)) {
            throw new IllegalArgumentException();
        }
        return (T) rowsFactoryMap.get(clazz).create(rows);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GlitchInventory<T>> T newInventory(Class<T> clazz, InventoryType inventoryType) throws InvalidInventoryType {
        if (!typeFactoryMap.containsKey(clazz)) {
            throw new IllegalArgumentException();
        }
        return (T) typeFactoryMap.get(clazz).create(inventoryType);
    }

    @FunctionalInterface
    public interface DefaultInventoryFactory<T extends GlitchInventory<T>> {
        static <T extends GlitchInventory<T>> DefaultInventoryFactory<T> failed(Class<T> clazz) {
            return () -> {
                throw new InvalidInventoryType(clazz, 0);
            };
        }

        T create() throws InvalidInventoryType;
    }

    @FunctionalInterface
    public interface RowsInventoryFactory<T extends GlitchInventory<T>> {
        static <T extends GlitchInventory<T>> RowsInventoryFactory<T> failed(Class<T> clazz) {
            return rows -> {
                throw new InvalidInventoryType(clazz, rows);
            };
        }

        T create(int rows) throws InvalidInventoryType;
    }

    @FunctionalInterface
    public interface TypeInventoryFactory<T extends GlitchInventory<T>> {
        static <T extends GlitchInventory<T>> TypeInventoryFactory<T> failed(Class<T> clazz) {
            return inventoryType -> {
                throw new InvalidInventoryType(clazz, inventoryType);
            };
        }

        T create(InventoryType inventoryType) throws InvalidInventoryType;
    }
}
