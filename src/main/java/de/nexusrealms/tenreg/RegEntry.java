package de.nexusrealms.tenreg;

import de.nexusrealms.tenreg.item.ItemEntry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
public abstract class RegEntry<T> implements Supplier<T> {
    @Getter
    private final SubReg<T> owner;
    @Getter
    private final RegistryKey<T> key;
    @Getter
    private final T value;

    protected RegEntry(SubReg<T> owner, RegistryKey<T> key, T value){
        this.owner = owner;
        this.key = key;
        this.value = value;
        owner.addEntry(this);

    }
    @Getter @Nullable
    private RegistryEntry.Reference<T> reference;
    protected void register(){
        reference = Registry.registerReference(owner.getRegistry(), key, get());
    }
    protected void postRegister(){
    }

    @Override
    public T get() {
        return getValue();
    }
    public Identifier getId(){
        return getKey().getValue();
    }
}
