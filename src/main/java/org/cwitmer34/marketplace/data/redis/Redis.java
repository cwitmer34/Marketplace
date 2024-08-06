package org.cwitmer34.marketplace.data.redis;

import lombok.Getter;
import lombok.SneakyThrows;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.cwitmer34.marketplace.util.ConsoleUtil;
import redis.clients.jedis.*;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@Getter
public class Redis {
	private JedisPool pool;

	public Redis() {
		this.init();
	}

	@SneakyThrows
	private void init() {
		final JedisPoolConfig jedisConfig = new JedisPoolConfig();
		final URI uri = new URI(Objects.requireNonNull(TrialMarketplace.getPlugin().getConfig().getString("redis-uri")));
		jedisConfig.setMaxIdle(64);
		jedisConfig.setMaxTotal(64);

		final ClassLoader previous = Thread.currentThread().getContextClassLoader();

		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

		try {
			this.pool = new JedisPool(jedisConfig, uri, Protocol.DEFAULT_TIMEOUT);
		} catch (Exception e) {
			ConsoleUtil.severe("Failed to connect to Redis. Please ensure your Redis URI is valid.");
			ConsoleUtil.severe("If you are running on localhost, make sure you've installed Redis.");
			ConsoleUtil.severe("Disabling plugin...");
			TrialMarketplace.getPlugin().getServer().getPluginManager().disablePlugin(TrialMarketplace.getPlugin());
		}

		Thread.currentThread().setContextClassLoader(previous);
	}

	public void flush() {
		try (final Jedis jedis = this.pool.getResource()) {
			jedis.flushAll();
		}
	}


	public Jedis getPool() {
		return this.pool.getResource();
	}
}
