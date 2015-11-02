package com.amex.Pool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class SentinelPoolConfig {

	private static final Logger logger = LogManager.getFormatterLogger(SentinelPoolConfig.class);
	public static JedisSentinelPool pool;
	private static final ReentrantLock LOCK = new ReentrantLock();

	public static void initPool(){

		logger.entry();
		try {
			LOCK.tryLock(1000,TimeUnit.MILLISECONDS);
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxTotal(10);
			poolConfig.setBlockWhenExhausted(true);
			poolConfig.setMinIdle(1);

			logger.debug("Maximum pool size is %,d",poolConfig.getMaxTotal());
			logger.debug("Minimum idle pool size is %,d", poolConfig.getMinIdle());
			logger.debug("Will thread be blocked when pool is exhausted? %s",poolConfig.getBlockWhenExhausted());
			logger.debug("maximum idle pool size is %,d",poolConfig.getMaxIdle());

			Set<String> sentinels=new HashSet<String>();
			logger.debug("Adding... 10.115.18.23:26379");
			sentinels.add("10.115.18.23:26379");
			logger.debug("Adding... 10.115.18.24:26379");
			sentinels.add("10.115.18.24:26379");
			logger.debug("Number of Sentinels added %,d",sentinels.size());


			pool= new JedisSentinelPool("mymaster", sentinels, poolConfig, 1500, "dds");
		}catch (Exception e) {
			logger.error("Error while initializing Pool ", e);
		}finally{
			logger.debug("Number of threads in hold %,d",LOCK.getHoldCount());
			LOCK.unlock();
		}

		logger.exit(pool);
	} 
}
