package com.dxc.lib.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dxc.lib.entity.Books;
import com.dxc.lib.exception.BookException;
import com.dxc.lib.repository.BookRepository;

@Service
public class BooksServiceImpl implements BooksService {

	@Autowired
	private BookRepository booksRepo;

	@Transactional
	@Override
	public boolean deleteById(int bcode) throws BookException {
		boolean deleted = false;
		if (!booksRepo.existsById(bcode)) {
			throw new BookException("No Such Book Found To Delete!");
		}
		booksRepo.deleteById(bcode);
		deleted = true;
		return deleted;
	}

	@Transactional
	@Override
	public Books add(Books book) throws BookException {
		if (book != null) {
			if (booksRepo.existsById(book.getBcode())) {
				throw new BookException("An Book with the bcode " + book.getBcode() + " already exists!");
			}

			booksRepo.save(book);
		}
		return book;
	}

	@Transactional
	@Override
	public Books update(Books book) throws BookException {
		if (book != null) {
			if (!booksRepo.existsById(book.getBcode())) {
				throw new BookException("No Such Book Found To Update!");
			}
			booksRepo.save(book);
		}
		return book;
	}

	@Transactional
	@Override
	public Books getById(int bcode) {
		return booksRepo.findById(bcode).orElse(null);
	}

	@Override
	public List<Books> getAllBooks() {

		return booksRepo.findAll();
	}

	@Override
	public Books findByTitle(String title) {
		return booksRepo.findByTitle(title);
	}

	@Override
	public List<Books> findAllByPublishDate(LocalDate publishDate) {
		return booksRepo.findAllByPublishDate(publishDate);
	}



}
