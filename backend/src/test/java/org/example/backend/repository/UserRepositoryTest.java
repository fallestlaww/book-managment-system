package org.example.backend.repository;

import org.example.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByName_returnsUser() {
        User user = new User();
        user.setName("testuser");
        user.setNumberOfBorrowedBooks(0);
        userRepository.save(user);

        User found = userRepository.findByName("testuser");
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("testuser");
    }

    @Test
    void findByName_returnsNull() {
        User found = userRepository.findByName("nonexistent");
        assertThat(found).isNull();
    }
} 