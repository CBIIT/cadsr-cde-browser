package gov.nih.nci.cbiit.cadsr.datadump;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = {"gov.nih.nci.cbiit.cadsr.datadump"})
@EnableAutoConfiguration
public class SpringBootCdeDump implements CommandLineRunner {
	@Autowired
	XmlCdeDumper xmlCdeDumper;
	@Autowired
	ConnectionHelper connectionHelper;
	
    public static void main(String[] args) throws Exception {
    	SpringApplication app = new SpringApplication(SpringBootCdeDump.class);
    	long started = System.currentTimeMillis();
    	app.setWebEnvironment(false);
    	//In springboot 2
    	//setWebApplicationType(WebApplicationType.NONE);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        System.out.println("Time in minutes: " + ((System.currentTimeMillis() - started)/60000));
    }

	@Override
	/**
	 * if arg0 = xml - xml dump if excel Excel dump and error otherwise
	 * 
	 */
	public void run(String... args) throws Exception {
		if (args.length > 0) {
			if ("xml".equals(args[0])) {
				System.out.println("XML dump");
				xmlCdeDumper.setConnection(connectionHelper.createConnection());
				//xmlCdeDumper.setDbURL(dbURL);
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
