package org.test.bookpub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.test.bookpub.repository.BookRepository;

@SpringBootApplication
@EnableScheduling 
public class BookPubApplication {
	protected final Logger logger = LoggerFactory.getLogger(BookPubApplication.class);
	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(BookPubApplication.class, args);
	}
	
	@Bean
	public StartupRunner schedulerRunner(){
		return new StartupRunner();
	}
	
	@Scheduled(initialDelay=10000,fixedRate=10000)
	public void run(){
		logger.info("Number	of	books:	"	+	bookRepository.count()); 
	}
}


