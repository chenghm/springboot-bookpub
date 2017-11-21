package org.test.bookpub.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.test.bookpub.domain.Book;
import org.test.bookpub.repository.BookRepository;

public class BookFormatter implements Formatter<Book> {

	private BookRepository repository;

	public BookFormatter(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	public String print(Book book, Locale locale) {
		return book.getIsbn();
	}

	@Override
	public Book parse(String bookIdentifier, Locale locale) throws ParseException {
		Book book =  repository.findBookByIsbn(bookIdentifier);
		return book != null ? book : new Book();
	}

}
