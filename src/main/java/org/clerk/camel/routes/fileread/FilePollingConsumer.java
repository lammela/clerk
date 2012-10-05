package org.clerk.camel.routes.fileread;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class FilePollingConsumer {
	
	@Autowired
	ConsumerTemplate consumer;
	
	@Autowired
	ProducerTemplate producer;
	
	@Value("#{camelProperties['pollFrom']}")
	String pollFrom;
	@Value("#{camelProperties['forwardTo']}")
	String forwardTo;
	
	public void collectFiles() {
            System.out.println("Consumer");
		while (true) {
			// a smaller timeout does not poll the files
                    System.out.println(pollFrom);
			Exchange exch = consumer.receive(pollFrom,2000);
			if (exch==null) {
                                System.out.println("Break;");
				break;
			}
                        System.out.println("Läpi");
			Exchange result = producer.send(forwardTo, exch);
			if (result.isFailed()) {
				System.out.println("my processor raised an error!");
			}
		}
	}
}
