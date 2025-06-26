package org.example.backend.repository;

import org.example.backend.dto.response.BorrowedBooksStatisticResponse;
import org.example.backend.dto.response.BorrowedTitlesResponse;
import org.example.backend.model.Book;
import org.example.backend.model.Borrowing;
import org.example.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BorrowingRepositoryTest {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Book book;
    private Borrowing borrowing;

    @BeforeEach
    void setUp() {
        user = createAndSaveUser("TestUser");
        book = createAndSaveBook("TestTitle", "Test Author");
        borrowing = createAndSaveBorrowing(user, book);
    }

    private User createAndSaveUser(String name) {
        User user = new User();
        user.setName(name);
        user.setNumberOfBorrowedBooks(0);
        return userRepository.save(user);
    }

    private Book createAndSaveBook(String title, String author) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setAmount(1);
        book.setAmountOfBorrowedBooks(0);
        return bookRepository.save(book);
    }

    private Borrowing createAndSaveBorrowing(User user, Book book) {
        Borrowing borrowing = new Borrowing();
        borrowing.setUser(user);
        borrowing.setBook(book);
        return borrowingRepository.save(borrowing);
    }

    @Test
    void findBorrowedBooksByUserName_returnsBooks() {
        List<Book> foundBooks = borrowingRepository.findBorrowedBooksByUserName("TestUser");
        assertThat(foundBooks).hasSize(1);
        assertThat(foundBooks.get(0).getTitle()).isEqualTo("TestTitle");
    }

    @Test
    void findBorrowedBooksByUserName_returnsEmpty_whenNoBorrowings() {
        List<Book> foundBooks = borrowingRepository.findBorrowedBooksByUserName("UnknownUser");
        assertThat(foundBooks).isEmpty();
    }

    @Test
    void findBorrowedBooksByUserName_returnsEmpty_whenUserHasNoBorrowings() {
        User newUser = createAndSaveUser("NoBorrowingsUser");
        List<Book> foundBooks = borrowingRepository.findBorrowedBooksByUserName("NoBorrowingsUser");
        assertThat(foundBooks).isEmpty();
    }

    @Test
    void findDistinctBorrowedBooksTitles_returnsTitles() {
        List<BorrowedTitlesResponse> titles = borrowingRepository.findDistinctBorrowedBooksTitles();
        assertThat(titles).extracting(BorrowedTitlesResponse::getTitle).contains("TestTitle");
    }

    @Test
    void findDistinctBorrowedBooksTitles_returnsEmpty_whenNoBorrowings() {
        borrowingRepository.deleteAll();
        List<BorrowedTitlesResponse> titles = borrowingRepository.findDistinctBorrowedBooksTitles();
        assertThat(titles).isEmpty();
    }

    @Test
    void findDistinctBorrowedBooksTitlesAndCountsBorrowedCopies_returnsStats() {
        List<BorrowedBooksStatisticResponse> stats = borrowingRepository.findDistinctBorrowedBooksTitlesAndCountsBorrowedCopies();
        assertThat(stats).extracting(BorrowedBooksStatisticResponse::getTitle).contains("TestTitle");
        assertThat(stats.get(0).getAmountOfBorrowedBooks()).isEqualTo(1L);
    }

    @Test
    void findDistinctBorrowedBooksTitlesAndCountsBorrowedCopies_returnsEmpty_whenNoBorrowings() {
        borrowingRepository.deleteAll();
        List<BorrowedBooksStatisticResponse> stats = borrowingRepository.findDistinctBorrowedBooksTitlesAndCountsBorrowedCopies();
        assertThat(stats).isEmpty();
    }

    @Test
    void findBorrowingByUserIdAndBookId_returnsBorrowing() {
        List<Borrowing> found = borrowingRepository.findBorrowingByUserIdAndBookId(user.getId(), book.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getUser().getId()).isEqualTo(user.getId());
        assertThat(found.get(0).getBook().getId()).isEqualTo(book.getId());
    }

    @Test
    void findBorrowingByUserIdAndBookId_returnsEmpty_whenNoSuchBorrowing() {
        List<Borrowing> found = borrowingRepository.findBorrowingByUserIdAndBookId(999L, 999L);
        assertThat(found).isEmpty();
    }
} 