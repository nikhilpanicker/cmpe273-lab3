package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;

import com.google.common.hash.Hashing;

public class Client {
	static CacheServiceInterface nodeB;
	static CacheServiceInterface nodeC;
	static CacheServiceInterface nodeA;
	public static void main(String[] args) throws Exception {
		System.out.println("Starting Cache Client...");
		// CacheServiceInterface cache = new DistributedCacheService(
		// "http://localhost:3000");
		ArrayList<String> serverNode = new ArrayList<String>();
		serverNode.add("http://localhost:3000");
		serverNode.add("http://localhost:3002");
		serverNode.add("http://localhost:3001");
		ConsistentHash<String> ch = new ConsistentHash<String>(Hashing.md5(),
				1, serverNode);
		for (int count = 1, letterCount = 97; count <= 10 && letterCount <= 106; count++, letterCount++) {
			String serverURL = ch.get(count);
                        CacheServiceInterface cache = new DistributedCacheService(serverURL);
                        System.out.println("PUT ==> node "+serverURL);
	                System.out.println("put ==>("+count+","+String.valueOf((char) letterCount)+")");
	                cache.put(count, String.valueOf((char) letterCount));
	                System.out.println("GET ==> node "+serverURL);
	                System.out.println("get(" + count + ") => " + cache.get(count));
		}
		System.out.println("Existing Cache Client...");
		
	}
	
}
