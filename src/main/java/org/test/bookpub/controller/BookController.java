package org.test.bookpub.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.test.bookpub.domain.Book;
import org.test.bookpub.domain.Reviewer;
import org.test.bookpub.repository.BookRepository;
import org.test.bookpub.util.Isbn;
import org.test.bookpub.util.IsbnEditor;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookRepository bookRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	// @RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
	// public Book getBook(@PathVariable String isbn) {
	// return bookRepository.findBookByIsbn(isbn);
	// }

	@RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
	public Book getBook(@PathVariable Isbn isbn) {
		return bookRepository.findBookByIsbn(isbn.getIsbn());
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Isbn.class, new IsbnEditor());
	}

	@RequestMapping(value = "/{isbn}/reviewers", method = RequestMethod.GET)
	public List<Reviewer> getReviewers(@PathVariable("isbn") Book book) {
		return book.getReviewers();
	}

	@RequestMapping(value = "/session", method = RequestMethod.GET)
	public String getSessionId(HttpServletRequest request) {
		return request.getSession().getId();
	}
}
