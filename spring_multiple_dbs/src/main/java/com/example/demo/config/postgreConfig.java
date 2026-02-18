package com.example.demo.config;

import java.util.HashMap;

import javax.sql.DataSource;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "psqlEntityManagerFactory", 
		transactionManagerRef = "psqlTransactionManager", 
		basePackages = {
				"com.example.demo.repos.psql"
		}
)
public class postgreConfig {

	@Bean(name = "psqlDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.secondary")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "psqlEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean bookEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("psqlDataSource") DataSource dataSource) {
		
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "update");
		
		return builder.dataSource(dataSource)
					  .properties(properties)
					  .packages("com.example.demo.entities")
					  .persistenceUnit("psql")
					  .build();
	}

	@Bean(name = "psqlTransactionManager")
	public PlatformTransactionManager bookTransactionManager(@Qualifier("psqlEntityManagerFactory") EntityManagerFactory bookEntityManagerFactory) {
		return new JpaTransactionManager(bookEntityManagerFactory);
	}
}