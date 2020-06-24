package com.dxc.lib;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.dxc.lib.entity.Books;
import com.dxc.lib.repository.BookRepository;

@DataJpaTest // configuring H2, an in-memory database,setting Hibernate, Spring Data, and the
				// DataSource,performing an @EntityScan turning on SQL logging
public class BookRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookRepository bookRepository;

	private Books[] testData;

	@BeforeEach
	public void fillTestData() {
		testData = new Books[] { 
				new Books(101, "VoteforLaughter", "RKN", LocalDate.now()),
				new Books(102, "WingsofFire", "APJK", LocalDate.now()) 
				};

		// inserting test data into H2 database
		for (Books book : testData) {
			entityManager.persist(book);
		}
		entityManager.flush();

	}

	@AfterEach
	public void clearDatabase() {
		// removing test data into H2 database
		for (Books book : testData) {
			entityManager.remove(book);
		}
		entityManager.flush();
	}

	@Test
	public void findByTitleTest() {
		for (Books book : testData) {
			Assertions.assertEquals(book, bookRepository.findByTitle(book.getTitle()));
		}
	}

	@Test
	public void findByTitleTestWitnNonExistingTitle() {
		Assertions.assertNull(bookRepository.findByTitle("123!@#"));
	}

	@Test
	public void findAllByPublishDateTest() {
		Books[] actualData = bookRepository.findAllByPublishDate(LocalDate.now()).toArray(new Books[] {});
		for (int i = 0; i < actualData.length; i++) {
			Assertions.assertEquals(testData[i], actualData[i]);
		}
	}

	@Test
	public void findAllByPublishDateTestWithNonExisitngDate() {
		List<Books> actualData = bookRepository.findAllByPublishDate(LocalDate.now().plusDays(2));
		Assertions.assertEquals(0, actualData.size());
	}

}
