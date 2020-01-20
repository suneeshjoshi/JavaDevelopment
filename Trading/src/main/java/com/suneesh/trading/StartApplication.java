package com.suneesh.trading;

import com.suneesh.trading.engine.Framework;
import com.suneesh.trading.models.responses.Tick;
import com.suneesh.trading.repository.TickRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.suneesh.trading"})
@EnableJpaRepositories("com.suneesh.trading.repository")
@Configuration
public class StartApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger();

    @Value("${Application.id}")
    private String applicationID;

    @Value("${Application.authorizeCode}")
    private String applicationAuthorizeCode;

    @Autowired
    protected TickRepository tickRepository;


    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    @Override
    public void run(String... args) {

        logger.info("Starting Automated Trading Application...");
        logger.info("{} - {} ",applicationID, applicationAuthorizeCode);
        Framework mainFramework = new Framework();
        mainFramework.init(tickRepository);

        mainFramework.threadCreation();

//        bookRepository.save(new Book("Python 2"));

    }

}
