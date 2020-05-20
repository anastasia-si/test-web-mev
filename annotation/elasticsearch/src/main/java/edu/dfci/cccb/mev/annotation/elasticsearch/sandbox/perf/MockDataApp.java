package edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.perf;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.configuration.ElasticSearchConfiguration;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.admin.DynamicTemplateBuilder_AllTypes;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.bulk.BulkProcessorFactory;
import edu.dfci.cccb.mev.annotation.elasticsearch.sandbox.index.mock.MockDataGenerator;

@Log4j
@Component
public class MockDataApp {

  @Inject Client client;
  @Inject BulkProcessorFactory bulkProcessorFactory;
 
  public static void main(String[] args){
      try(AnnotationConfigApplicationContext  springContext = new AnnotationConfigApplicationContext ()){
        springContext.register (ElasticSearchConfiguration.class);
        springContext.refresh ();
        
        final MockDataApp app = springContext.getBean (MockDataApp.class);
        log.debug("***** MockDataApp RUNING ****");
        app.run(args);
      }
  }
  
  public void run(String[] args){    
    MockDataGenerator mockgen = new MockDataGenerator (client, new DynamicTemplateBuilder_AllTypes () );
    
    if(args.length==2)
      mockgen.overwriteDataset (Integer.parseInt (args[0]), Integer.parseInt (args[1]), bulkProcessorFactory.create (10, 1));
    else{     
//      mockgen.overwriteDataset (100, 3000, bulkProcessorFactory.create (10, 8));
//      mockgen.overwriteDataset (150000, 10, bulkProcessorFactory.create (100, 8));
      mockgen.overwriteDataset (6001, 1000, bulkProcessorFactory.create (100, 8));
   }
  }
}
