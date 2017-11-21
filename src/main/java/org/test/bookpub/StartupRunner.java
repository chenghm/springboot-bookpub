package org.test.bookpub;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.test.bookpub.repository.BookRepository;

public class StartupRunner implements CommandLineRunner {
	protected final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

	@Autowired
	private DataSource ds;
	@Autowired
	private BookRepository bookRepository;

	@Override
	public void run(String... args) throws Exception {
		logger.info("hello");
		logger.info("DataSource:	" + ds.toString());
		logger.info("Number	of	books:	"	+	bookRepository.count());

	}

}
