package org.example.backend.repository;

import org.example.backend.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findByTitleAndAuthor_returnsBook_whenExists() {
        Book book = new Book();
        book.setTitle("TestTitle");
        book.setAuthor("Test Author");
        book.setAmount(5);
        book.setAmountOfBorrowedBooks(0);
        bookRepository.save(book);

        Book found = bookRepository.findByTitleAndAuthor("TestTitle", "Test Author");
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("TestTitle");
        assertThat(found.getAuthor()).isEqualTo("Test Author");
    }

    @Test
    void findByTitleAndAuthor_returnsNull_whenNotExists() {
        Book found = bookRepository.findByTitleAndAuthor("Nonexistent", "Nobody");
        assertThat(found).isNull();
    }
} 