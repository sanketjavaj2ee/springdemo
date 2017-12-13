package com.boot2;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DatabaseConfig {

	@Autowired
	ConfigurableApplicationContext appDbContext;

	@Value("${jdbc.fetchsize: 100}")
	private Integer fetchSize;

	@Value("${jdbc.bigFetchsize: 10000}")
	private Integer bigFetchSize;

	@Primary
	@Bean(name = "primaryTemplate")
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		jt.setFetchSize(fetchSize);
		return jt;
	}

	@Primary
	@Bean(name = "primaryNamedJdbcTemplate")
	public NamedParameterJdbcTemplate namedJdbcTemplate(DataSource dataSource) {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		jt.setFetchSize(fetchSize);
		return new NamedParameterJdbcTemplate(jt);
	}

	@Bean(destroyMethod = "")
	@ConfigurationProperties(prefix = "spring.flyway.datasource")
	@FlywayDataSource
	@ConditionalOnProperty(prefix = "flyway", name = "enabled", matchIfMissing = true)
	public DataSource flywayDataSource() {
		return DataSourceBuilder.create().type(BasicDataSource.class).build();
	}

	@Bean(initMethod = "migrate")
	@ConfigurationProperties(prefix = "flyway")
	@ConditionalOnProperty(prefix = "flyway", name = "enabled", matchIfMissing = true)
	public Flyway flyway() {
		Flyway flyway = new Flyway();
		flyway.setDataSource(flywayDataSource());
		return flyway;
	}

}
