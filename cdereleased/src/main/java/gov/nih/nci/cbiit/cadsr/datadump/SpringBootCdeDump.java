package gov.nih.nci.cbiit.cadsr.datadump;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication(scanBasePackages = {"gov.nih.nci.cbiit.cadsr.datadump"}, exclude = JpaRepositoriesAutoConfiguration.class)
@Component
public class SpringBootCdeDump {
    public static void main(String[] args) throws Exception {
    	SpringApplication app = new SpringApplication(SpringBootCdeDump.class);
    	long started = System.currentTimeMillis();
    	//In springboot 2
    	app.setWebApplicationType(WebApplicationType.NONE);
        app.setBannerMode(Banner.Mode.OFF);
        System.out.println("Started");
		SpringApplication.run(SpringBootCdeDump.class, args);
    }
    
    @Bean
    public ApplicationStartupRunner schedulerRunner() {
        return new ApplicationStartupRunner();
    }

}
