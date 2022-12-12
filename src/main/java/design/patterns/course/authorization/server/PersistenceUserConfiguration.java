package design.patterns.course.authorization.server;

import design.patterns.course.authorization.server.domain.entity.user.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "userEntityManager",
    transactionManagerRef = "userTransactionManager",
    basePackages = "design.patterns.course.authorization.server.domain.entity.user.repository"
)
@ComponentScan(basePackages = { "design.patterns.course.authorization.server.*" })
@EntityScan("design.patterns.course.authorization.server.*")
public class PersistenceUserConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "spring.identity-provider.datasource")
  public DataSource userDataSource() {
    return DataSourceBuilder
        .create()
        .build();
  }

  @Bean(name = "userEntityManager")
  public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(userDataSource())
        .properties(hibernateProperties())
        .packages(User.class)
        .persistenceUnit("userPU")
        .build();
  }

  @Primary
  @Bean(name = "userTransactionManager")
  public PlatformTransactionManager userTransactionManager(@Qualifier("userEntityManager") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  private Map hibernateProperties() {
    Resource resource = new ClassPathResource("hibernate.properties");

    try {
      Properties properties = PropertiesLoaderUtils.loadProperties(resource);

      return properties.entrySet().stream()
          .collect(Collectors.toMap(
              e -> e.getKey().toString(),
              Map.Entry::getValue)
          );
    } catch (IOException e) {
      return new HashMap();
    }
  }

}
