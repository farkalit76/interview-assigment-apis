package com.interview.assignment.domain.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.interview.assignment.repository",
        entityManagerFactoryRef = "companyEntityManagerFactory",
        transactionManagerRef = "companyTransactionManager"
)
public class CompanyDatasourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.company")
    public DataSourceProperties companyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.company.hikari")
    public DataSource companyDataSource() {
        return companyDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @Primary
    public EntityManagerFactoryBuilder companyEntityManagerFactoryBuilder(JpaProperties jpaProperties, ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), persistenceUnitManager.getIfAvailable());
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean companyEntityManagerFactory(@Qualifier("companyEntityManagerFactoryBuilder") EntityManagerFactoryBuilder companyEntityManagerFactoryBuilder) {
        return companyEntityManagerFactoryBuilder
                .dataSource(companyDataSource())
                .packages("com.interview.assignment.repository")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager companyTransactionManager(@Qualifier("companyEntityManagerFactory")
                                                                LocalContainerEntityManagerFactoryBean companyEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(companyEntityManagerFactory.getObject()));
    }

    @Bean
    @Primary
    public JdbcTemplate companyJdbcTemplate() {
        return new JdbcTemplate(companyDataSource());
    }
}
