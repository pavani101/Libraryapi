package com.dxc.lib;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dxc.lib.entity.Books;
import com.dxc.lib.exception.BookException;
import com.dxc.lib.repository.BookRepository;
import com.dxc.lib.service.BooksService;
import com.dxc.lib.service.BooksServiceImpl;

@SpringJUnitConfig
public class BookServiceImplUnitTest {

	@TestConfiguration
	static class ItemServiceImplTestContextConfiguration {

		@Bean
		public BooksService bookService() {
			return new BooksServiceImpl();
		}
	}

	@MockBean
	private BookRepository bookRepository;

	@Autowired
	private BooksService bookService;

	private Books[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Books[] { new Books(101, "Valorant", "Vishal", LocalDate.now()),
				new Books(102, "Harry_Potter", "Prahant", LocalDate.now()) };
	}

	@AfterEach
	public void clearDatabase() {
		testData = null;
	}

	@Test
	public void addTest() {

		Mockito.when(bookRepository.save(Mockito.any(Books.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(false);

		try {
			Books actual = bookService.add(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (BookException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void addExistingBookTest() {
		Mockito.when(bookRepository.save(Mockito.any(Books.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(true);

		Assertions.assertThrows(BookException.class, () -> {
			bookService.add(testData[0]);
		});

	}

	@Test
	public void updateExistingBookTest() {

		Mockito.when(bookRepository.save(Mockito.any(Books.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(true);

		try {
			Books actual = bookService.update(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (BookException e) {
			Assertions.fail(e.getMessage());
		}
	}

	@Test
	public void updateNonExistingItemTest() {
		Mockito.when(bookRepository.save(Mockito.any(Books.class))).thenReturn(null);

		Mockito.when(bookRepository.existsById(testData[0].getBcode())).thenReturn(false);

		Assertions.assertThrows(BookException.class, () -> {
			bookService.update(testData[0]);
		});

	}

	@Test
	public void deleteByIdExistingRecordTest() {
		Mockito.when(bookRepository.existsById(Mockito.isA(Integer.class))).thenReturn(true);

		Mockito.doNothing().when(bookRepository).deleteById(Mockito.isA(Integer.class));

		try {
			Assertions.assertTrue(bookService.deleteById(testData[0].getBcode()));
		} catch (BookException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void deleteByIdNonExistingRecordTest() {
		Mockito.when(bookRepository.existsById(Mockito.isA(Integer.class))).thenReturn(false);

		Mockito.doNothing().when(bookRepository).deleteById(Mockito.isA(Integer.class));

		Assertions.assertThrows(BookException.class, () -> {
			bookService.deleteById(testData[0].getBcode());
		});

	}

	@Test
	public void getByIdExisitngRecordTest() {
		Mockito.when(bookRepository.findById(testData[0].getBcode())).thenReturn(Optional.of(testData[0]));
		Assertions.assertEquals(testData[0], bookService.getById(testData[0].getBcode()));
	}

	@Test
	public void getByIdNonExisitngRecordTest() {
		Mockito.when(bookRepository.findById(testData[0].getBcode())).thenReturn(Optional.empty());
		Assertions.assertNull(bookService.getById(testData[0].getBcode()));
	}

	@Test
	public void getAllItemsWhenDataExists() {
		List<Books> expected = Arrays.asList(testData);

		Mockito.when(bookRepository.findAll()).thenReturn(expected);

		Assertions.assertEquals(expected, bookService.getAllBooks());
	}

	@Test
	public void getAllItemsWhenNoDataExists() {
		List<Books> expected = new ArrayList<>();

		Mockito.when(bookRepository.findAll()).thenReturn(expected);

		Assertions.assertEquals(expected, bookService.getAllBooks());
	}
}
