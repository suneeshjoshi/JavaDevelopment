package com.mkyong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartApplication implements CommandLineRunner {

    private static final Logger log = LogManager.getLogger();

    @Autowired
    private BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }

    private void oldExampleCode(){
        Book java = new Book("Java");
        bookRepository.save(java);
        bookRepository.save(new Book("Node"));
        bookRepository.save(new Book("Python"));

        System.out.println("\nfindAll()");
        bookRepository.findAll().forEach(x -> System.out.println(x));

        System.out.println("\nfindById(1L)");
        bookRepository.findById(1l).ifPresent(x -> System.out.println(x));

        System.out.println("\nfindByName('Node')");
        bookRepository.findByName("Node").forEach(x -> System.out.println(x));
    }

    @Override
    public void run(String... args) {

        log.info("StartApplication...");

        oldExampleCode();
        MainApplication mainApplication = new MainApplication();
        mainApplication.init(bookRepository);

        mainApplication.threadCreation();

        bookRepository.save(new Book("Python 2"));

    }

}
