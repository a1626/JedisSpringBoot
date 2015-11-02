package com.amex.proxy;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import com.amex.Pool.SentinelPoolConfig;

public class RedisProxy {
	Logger logger = LogManager.getFormatterLogger(RedisProxy.class);
	
	public void readFromRedis(String[] fields){
		logger.entry((Object[])fields);
		JedisSentinelPool sentinelPool;
		List<String>values = null;
		int retry=0;
		if(null !=(sentinelPool=SentinelPoolConfig.pool)){
			long startTIme = System.currentTimeMillis();
			try(Jedis jedis = sentinelPool.getResource()){
				logger.debug("Pinging Jedis %s",jedis.ping());
				values=jedis.hmget("AccumsHash", fields);
				long endTIme = System.currentTimeMillis();
				logger.info("Time Taken for Redis Read Call %,d",(endTIme-startTIme));
				logger.debug("Values from Redis %,s",values.toString());
			}catch (Exception e) {
				logger.error("Error while trying to read from Redis ", e);
				
				for(Throwable t:e.getSuppressed()){
					logger.error("Suppressed Exception is ",t);
				}
			}
		}else if(retry<1){
			logger.debug("Sentinel not initiated");
			retry++;
			SentinelPoolConfig.initPool();
			readFromRedis(fields);
		}
		
		logger.exit(values);
	}
	
	public void writeToRedis(Map<String,String> hash){
		logger.entry(hash);
		JedisSentinelPool sentinelPool;
		String replyCode= null;
		int retry=0;
		if(null !=(sentinelPool=SentinelPoolConfig.pool)){
			long startTIme = System.currentTimeMillis();
			try(Jedis jedis = sentinelPool.getResource()){
				logger.debug("Pinging Jedis %s",jedis.ping());
				replyCode=jedis.hmset("AccumsHash", hash);
				long endTIme = System.currentTimeMillis();
				logger.info("Time Taken for Redis Read Call %,d",(endTIme-startTIme));
			}catch (Exception e) {
				logger.error("Error while trying to write to Redis ", e);
				
				for(Throwable t:e.getSuppressed()){
					logger.error("Suppressed Exception is ",t);
				}
			}
		}else if(retry<1){
			logger.debug("Sentinel not initiated");
			retry++;
			SentinelPoolConfig.initPool();
			writeToRedis(hash);
		}
		
		logger.exit(replyCode);
	}
}
