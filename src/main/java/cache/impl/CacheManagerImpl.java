package cache.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import cache.ICacheManager;

@Service
public class CacheManagerImpl implements ICacheManager {
	@Resource
	private ShardedJedisPool shardedJedisPool;
	private static int DEFAULT_TIME_OUT = 120;
	
	public void set(String key, String value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.set(key, value);			

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void set(byte[] key, byte[] value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.set(key, value);			

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	public String get(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.get(key);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public byte[] get(byte[] key){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.get(key);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
    
	public List<byte[]> getMulti(List<byte[]> keys){
		ShardedJedis jedis = shardedJedisPool.getResource();
		List<byte[]> values = new ArrayList<byte[]>();
		try {
			for(byte[] key : keys){
				if(get(key) != null)
				values.add(get(key));
			}
			return values;

		} finally {
			shardedJedisPool.returnResource(jedis);
		}		
	}
	
	public void del(byte[] key){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {		
			jedis.expire(key, 1);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void expire(String key, int seconds){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {		
			jedis.expire(key, seconds);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void expire(byte[] key, int seconds){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {		
			jedis.expire(key, seconds);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void del(String key){
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {		
			jedis.del(key);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	public void lpush(String key, List<String> list) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			for (String str : list) {
				jedis.lpush(key, str);
			}
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void lpush(String key, String value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {			
				jedis.lpush(key, value);
			
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	
	public String lpop(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.lpop(key);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}	
	
	public String ltrim(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			return jedis.ltrim(key, 0, 0);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}	

	public List<String> lrange(String key, long start, long end) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.lrange(key, start, end);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public List<String> lrange(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.lrange(key, 0, jedis.llen(key)-1);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public long llen(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.llen(key);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public void hset(String key, String field, String value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			jedis.hset(key, field, value);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public String hget(String key, String field) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.hget(key, field);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public Long hdel(String key, String field) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.hdel(key, field);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}	
	
	public Long hincrBy(String key, String field, Long increase) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.hincrBy(key, field, increase);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public Set<String> hkeys(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.hkeys(key);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public Boolean hexists(String key, String field) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.hexists(key, field);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public Map<String, String> hgetAll(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {

			return jedis.hgetAll(key);

		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public boolean lock(String key, int seconds){
		ShardedJedis jedis = null;
		try{
			jedis = shardedJedisPool.getResource();
		}catch(Exception e){			
			return true;
		}
		
		try {
			long r = jedis.setnx(key, "1");
			if(r == 1){
				jedis.expire(key, seconds);
				return true;
			}
			return false;
		} catch(Exception e){			
			return true;
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	
	public boolean lock(String key){
		return lock(key, DEFAULT_TIME_OUT);
	}
	
	public void unlock(String key){
		del(key);
	}

//	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
//		this.shardedJedisPool = shardedJedisPool;
//	}


}
