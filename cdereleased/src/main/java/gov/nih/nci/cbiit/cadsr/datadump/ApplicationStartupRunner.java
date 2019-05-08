package gov.nih.nci.cbiit.cadsr.datadump;
/*
 * Copyright (C) 2019 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class ApplicationStartupRunner implements CommandLineRunner {
    protected static final Log logger = LogFactory.getLog(ApplicationStartupRunner.class);
	@Autowired
	XmlCdeDumper xmlCdeDumper;
	@Autowired
	ConnectionHelper connectionHelper;
    @Override
    //this was a test
//    public void run(String... args) throws Exception {
//        logger.info("Application Started !!");
//        System.exit(0);
//    }
	public void run(String... args) throws Exception {
		if (args.length > 0) {
			if ("xml".equals(args[0])) {
				System.out.println("XML dump");
				xmlCdeDumper.setConnection(connectionHelper.createConnection());
				System.out.println("...XML dump started");
				xmlCdeDumper.doDump();
				System.out.println("XML dump done");
			}
			else if ("excel".equals(args[0])) {
				System.out.println("Excel dump");
			}
			else {
				System.out.println("Unknown dump format:" + args[0]);
			}
		} else {
			System.out.println("Usage: \nSpringBootCdeDump xml \nor \nSpringBootCdeDump excel");
			exit(-1);
		}
		exit(0);
	}
	private void exit(int i) {
		System.out.println("Exiting with code: " + i);
		System.exit(i);
	}
}
