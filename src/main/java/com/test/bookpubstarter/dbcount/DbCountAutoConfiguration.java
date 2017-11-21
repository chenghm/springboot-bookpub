package com.test.bookpubstarter.dbcount;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;

@Configurable
public class DbCountAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DbCountRunner dbCountRunner(Collection<CrudRepository> repositories) {
		return new DbCountRunner(repositories);
	}

}
