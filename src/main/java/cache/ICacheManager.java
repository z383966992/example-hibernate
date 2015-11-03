package cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICacheManager {
	public void set(String key, String value);
	
	public void set(byte[] key, byte[] value);

	public void lpush(String key, List<String> list);

	public void lpush(String key, String value);

	public String get(String key);
	
	public byte[] get(byte[] key);
	
	public List<byte[]> getMulti(List<byte[]> keys);

	public void del(byte[] key);
	
	public void del(String key);
	
	void expire(String key, int seconds);
	
	void expire(byte[] key, int seconds);
	
	public String lpop(String key);

	public String ltrim(String key);
	
	public List<String> lrange(String key, long start, long end);
	
	public List<String> lrange(String key);
	
	public long llen(String key);
	
	public String hget(String key, String field);
	
	public Map<String, String> hgetAll(String key);
	
	public void hset(String key, String field, String value);
	
	public Long hdel(String key, String field) ;
	
	public Long hincrBy(String key, String field, Long increase);
	
	public Set<String> hkeys(String key);
	
	public Boolean hexists(String key, String field);
	
	public boolean lock(String key, int seconds);
	
	public boolean lock(String key);
	
	public void unlock(String key);
}
