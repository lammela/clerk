package org.clerk.camel.routes;

import java.util.Date;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.spring.Main;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class TimedBatchRoute extends RouteBuilder {

	/**
	 * * A main() so we can easily run these routing rules in our IDE
	 */
	public static void main(String... args) {
           
            
		try {
			Main.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void configure() throws Exception {
                System.out.println("HELLO!");
                
		// unknown exceptions
		errorHandler(deadLetterChannel("{{otherErrors}}"));

		// known exceptions
		onException(IllegalStateException.class).processRef("failureResponseProcessor")
		//.handled(true) // this moves the file to the success directory 
		.setHeader(Exchange.FILE_NAME, new SimpleExpression("${file:onlyname}-${date:now:yyyyMMdd}"))
		.to("{{knownErrorAction}}");

		// this is our trigger
		from("{{quartzConfig}}")			
			.beanRef("filePollingConsumer","collectFiles");
		
		from("{{forwardTo}}").processRef("fileProcessor");
	}

}
