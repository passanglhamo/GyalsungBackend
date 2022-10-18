package com.microservice.erp.webapp.config.jdbc;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Rajib Kumer Ghosh
 *
 */

public class SimpleDataSources<Key, Value> implements DataSources<Key, Value> {
    /*private Map<Key, Value> inMemoryStorage;
    private Executor serviceExe = Executors.newSingleThreadExecutor();

    public SimpleDataSources() {
    }

    protected Map<Key, Value> getInMemoryStorage() {
        if(this.inMemoryStorage == null) {
            this.inMemoryStorage = new ConcurrentHashMap();
        }

        return this.inMemoryStorage;
    }

    public void clear() {
        this.getInMemoryStorage().clear();
    }

    public Value replace(Key key, Value value) {
        return this.getInMemoryStorage().replace(key, value);
    }

    public Value remove(Key key) {
        return this.getInMemoryStorage().remove(key);
    }

    public void put(Key key, Value value) {
        this.getInMemoryStorage().put(key, value);
    }

    public boolean containsKey(Key s) {
        return this.getInMemoryStorage().containsKey(s);
    }

    public int size() {
        return this.getInMemoryStorage().size();
    }

    public Value read(Key key) {
        return this.getInMemoryStorage().get(key);
    }

    public Value[] readSync(int offset, int pageSize) {
        int size = this.size();
        int maxItemCount = Math.abs(offset) + Math.abs(pageSize);
        if(maxItemCount <= size) {
            List items = ((List)this.getInMemoryStorage().values().stream().collect(Collectors.toList())).subList(Math.abs(offset), maxItemCount);
            return (Object[])items.toArray();
        } else {
            return (Object[])(new Object[0]);
        }
    }

    public void readAsync(int offset, int pageSize, Consumer<Value[]> consumer) {
        this.serviceExe.execute(() -> {
            if(consumer != null) {
                Object[] items = this.readSync(offset, pageSize);
                consumer.accept(items);
            }

        });
    }*/
}
