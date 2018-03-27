package config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import models.ModelsPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import repositories.RepositoriesPackage;

/**
 * @author localEvg
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = RepositoriesPackage.class)
public class TestH2Database {

    @Bean
    public DataSource dataSource() {
        String driver = "org.h2.Driver";
        String url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"; // (!) DB_CLOSE_DELAY - is very important
        String user = "sa";
        String password = "";
        String schema = null;

        DriverManagerDataSource dmDataSource = new DriverManagerDataSource(url, user, password);
        dmDataSource.setDriverClassName(driver);
        if (schema != null) {
            dmDataSource.setSchema(schema);
        }
        return dmDataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        return jpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter vendAdapter) {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();

        emfb.setDataSource(dataSource);
        emfb.setJpaVendorAdapter(vendAdapter);
        emfb.setPackagesToScan(ModelsPackage.class.getPackage().getName());

        {
            Properties jpaProperties = new Properties();
            jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            jpaProperties.put("hibernate.show_sql", "true");
            jpaProperties.put("hibernate.hbm2ddl.auto", "create"); // validate | update | create | create-drop

            emfb.setJpaProperties(jpaProperties);
        }

        return emfb;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transMgr = new JpaTransactionManager(entityManagerFactory);
        return transMgr;
    }

}
