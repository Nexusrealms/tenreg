package de.nexusrealms.tenreg;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class SubReg<T> {
    @Getter
    private final Reg root;
    public abstract Registry<T> getRegistry();
    private final Map<RegistryKey<T>, RegEntry<T>> entries = new LinkedHashMap<>();
    public RegistryKey<? extends Registry<T>> getKey(){
        return getRegistry().getKey();
    }
    protected boolean addEntry(RegEntry<T> entry){
        if(entries.containsKey(entry.getKey())){
            return false;
        }
        entries.put(entry.getKey(), entry);
        return true;
    }
    public void register(){
        entries.values().forEach(RegEntry::register);
    }
    public void postRegister(){
        entries.values().forEach(RegEntry::postRegister);
    }
    public Identifier id(String path){
        return root.id(path);
    }
    public Function<String, Identifier> id(){
        return root.id();
    }
    public Stream<RegEntry<T>> streamEntries(){
        return entries.values().stream();
    }
    public <A> Stream<A> streamMatchingEntriesAs(Class<A> clazz){
        return streamEntries().filter(entry -> clazz.isAssignableFrom(entry.getClass())).map(clazz::cast);
    }
}
