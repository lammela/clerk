package org.clerk.camel.routes.fileread;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class FileProcessor implements Processor {

	public void process(Exchange ex) throws Exception {
		String filename = ex.getIn().getHeader(Exchange.FILE_NAME,String.class);
		if (filename.startsWith("FAIL")) {
			// this will be thrown to the DefaultErrorHandler
			ex.setException(new IllegalStateException("i cannot process this " + filename));
		}
		if (ex.getIn().getHeader(Exchange.FILE_NAME,String.class).startsWith("UNKNOWN")) {
			// fault is recoverable
			ex.getOut().setFault(true);				
			throw new IllegalArgumentException("another problem");
		}
	}

}
