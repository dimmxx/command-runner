package com.dm.model;

public class Pair<K, V> {

    private K key;
    private V value;
    private String metaData;

    Pair(K key, V value, String metaData) {
        this.key = key;
        this.value = value;
        this.metaData = metaData;
    }

    public static <K, V> Pair<K, V> of(K key, V value, String metaData) {
        return new Pair<>(key, value, metaData);
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    @Override
    public String toString() {
        return "Pair{" +
            "key=" + key +
            ", value=" + value +
            ", metaData='" + metaData + '\'' +
            '}';
    }
}
