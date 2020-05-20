package edu.dfci.cccb.mev.web.test.presets.controller.flat.large;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import edu.dfci.cccb.mev.test.presets.rest.configuration.PresetsRestConfigurationTest;

@Configuration
@Import(PresetsRestConfigurationTest.class)
public class TestJooqCursorGBMLevel2Configuration {

//  @Inject Environment environment;
//  @Bean(name="presets-datasource", destroyMethod = "close")
//  public DataSource dataSource () {
//    BasicDataSource dataSource = new BasicDataSource ();
//    dataSource.setDriverClassName ("org.h2.Driver");
//    dataSource.setUrl ("jdbc:h2:file:"
//                                                        + environment.getProperty ("user.home") 
//                                                        + "/mev/data/tcga/"
//                                                        + "mev-presets-FLAT-GBM.AgilentG4502A_07_2.Level_2.tsv"
//                                                        + ";AUTO_SERVER=TRUE"
//                                                        + ";QUERY_CACHE_SIZE=100000"
//                                                        + ";CACHE_SIZE=1048576");
//    dataSource.setUsername ("sa");
//    dataSource.setPassword ("");
//    
//    log.info ("*** presets-datasource config: " + dataSource.getUrl ());    
//
//    return dataSource;
//  }
  
  @Inject @Bean
  public DataSourceInitializer dataSourceInitializer( @Named("presets-datasource") DataSource dataSource) {
      DataSourceInitializer initializer = new DataSourceInitializer();
      initializer.setDataSource(dataSource);
      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();            
      initializer.setDatabasePopulator(populator);
      return initializer;
  }
  
}
