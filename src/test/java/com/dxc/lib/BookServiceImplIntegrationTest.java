package com.dxc.lib;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dxc.lib.entity.Books;
import com.dxc.lib.exception.BookException;
import com.dxc.lib.repository.BookRepository;
import com.dxc.lib.service.BooksService;

@SpringJUnitConfig
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class BookServiceImplIntegrationTest {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BooksService bookService;

	private Books[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Books[] { new Books(101, "Valorant", "Vishal", LocalDate.now()),
				new Books(102, "Harry_Potter", "Prahant", LocalDate.now()) };

		for (Books book : testData) {
			bookRepository.saveAndFlush(book);
		}
	}

	@AfterEach
	public void clearDatabase() {
		bookRepository.deleteAll();
		testData = null;
	}

	@Test
	public void addTest() {
		try {
			Books expected = new Books(106, "CocaCola", "sef", LocalDate.now().minusYears(1));
			Books actual = bookService.add(expected);
			Assertions.assertEquals(expected, actual);
		} catch (BookException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void addExistingItemTest() {
		Assertions.assertThrows(BookException.class, () -> {
			bookService.add(testData[0]);
		});
	}

	@Test
	public void updateExistingItemTest() {
		try {
			Books actual = bookService.update(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (BookException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void updateNonExistingItemTest() {
		Books nonExistingItem = new Books(106, "CocaCola","sefes", LocalDate.now().minusYears(1));
		Assertions.assertThrows(BookException.class, () -> {
			bookService.update(nonExistingItem);
		});
	}

	@Test
	public void deleteByIdExistingRecordTest() {
		try {
			Assertions.assertTrue(bookService.deleteById(testData[0].getBcode()));
		} catch (BookException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void deleteByIdNonExistingRecordTest() {
		Assertions.assertThrows(BookException.class, () -> {
			bookService.deleteById(333);
		});
	}

	@Test
	public void getByIdExisitngRecordTest() {
		Assertions.assertEquals(testData[0].getBcode(), bookService.getById(testData[0].getBcode()).getBcode());
	}

	@Test
	public void getByIdNonExisitngRecordTest() {
		Assertions.assertNull(bookService.getById(333));
	}

	@Test
	public void getAllItemsWhenDataExists() {
		List<Books> expected = Arrays.asList(testData);
		// Assertions.assertSame(expected, itemService.getAllItems());
		Assertions.assertIterableEquals(expected, bookService.getAllBooks());
	}

	@Test
	public void getAllItemsWhenNoDataExists() {
		List<Books> expected = new ArrayList<>();
		bookRepository.deleteAll();
		Assertions.assertEquals(expected, bookService.getAllBooks());
	}
}
