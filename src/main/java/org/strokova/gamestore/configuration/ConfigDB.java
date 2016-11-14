package org.strokova.gamestore.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.strokova.gamestore.model.Models;
import org.strokova.gamestore.repository.Repositories;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * @author vstrokova, 05.09.2016.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = Repositories.class)
@PropertySource("classpath:application.properties") // already have this in ConfigWeb class
public class ConfigDB {

    private static final Logger logger = Logger.getLogger(ConfigDB.class.getName());

    private static final String DATASOURCE_NAME_JDBC_GAMESTORE = "jdbc/gamestore";
    private static final String CREATE_DATABASE_WITH_DATA_SCRIPT_H2 = "classpath:db/gamestore_create_database_with_test_data_07nov2016_h2.sql";

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Profile("prod")
    @Bean
    public DataSource prodDataSource() {
        logger.info("Starting PROD MySQL database");
        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        return dsLookup.getDataSource(DATASOURCE_NAME_JDBC_GAMESTORE);
    }

    @Profile("dev")
    @Bean
    public DataSource devDataSource() {
        logger.info("Starting DEV H2 database");
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(CREATE_DATABASE_WITH_DATA_SCRIPT_H2)
                .build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform(hibernateDialect);
        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setJpaVendorAdapter(jpaVendorAdapter);
        emfb.setPackagesToScan(Models.class.getPackage().getName());

        return emfb;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
