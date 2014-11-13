package edu.sjsu.cmpe.cache.client;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.HashFunction;

public class ConsistentHash<T> {

	private final HashFunction hashFunction;
    private final int numberOfReplicas;
    private final SortedMap<Long, T> circle = new TreeMap<Long, T>();

    public ConsistentHash(HashFunction hashFunction, int numberOfReplicas,
            Collection<T> nodes) {
        this.hashFunction = hashFunction;
        this.numberOfReplicas = numberOfReplicas;

        for (T node : nodes) {
            add(node);
        }
    }

    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
        	Long l=hashFunction.hashString(node.toString() + i).asLong();
        	//System.out.println("Hash value of node "+l);
            circle.put(l, node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(hashFunction.hashString(node.toString() + i).asLong());
        }
    }

    public T get(Object key) {
    	//System.out.println("In ch get Method");
        if (circle.isEmpty()) {
            return null;
        }
        long hash = hashFunction.hashString(key.toString()).asLong();
       // System.out.println("Hash value of key "+hash);
        if (!circle.containsKey(hash)) {
            SortedMap<Long, T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
           // System.out.println("Circle First Key "+circle.firstKey()+" Tail Map First Key"+tailMap.firstKey());
        }
        return circle.get(hash);
    }
}