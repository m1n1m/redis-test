package ru.funbox.links.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@TestConfiguration
@Import({
        RedisConfiguration.class
})
public class RedisTestConfiguration {

    @Value("${spring.redis.testing.host:localhost}") String host;
    @Value("${spring.redis.testing.port:6379}") Integer port;
    @Value("${spring.redis.testing.password:}") String password;
    @Value("${spring.redis.testing.database:10}") Integer database;

    @Bean
    @Profile("test")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setPassword(RedisPassword.of(password));
        configuration.setDatabase(database);
        return new LettuceConnectionFactory(configuration);
    }
}