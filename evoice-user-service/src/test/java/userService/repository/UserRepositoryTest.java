package userService.repository;

import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import userService.entity.User;
import userService.utils.templates.DataJPATestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest extends DataJPATestTemplate<User> {
    @Autowired
    @Getter
    private UserRepository repository;

    @Test
    @DisplayName("Test for storing invalid values in the database (NEGATIVE)")
    void testUserRepository() {
        final var user = easyRandom.nextObject(User.class);
        user.setPhone("999 999 999 999 999 999");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(user));
    }
}