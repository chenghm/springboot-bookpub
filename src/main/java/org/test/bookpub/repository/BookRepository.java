package org.test.bookpub.repository;

import org.springframework.data.repository.CrudRepository;
import org.test.bookpub.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

	public	Book	findBookByIsbn(String	isbn); 
}
