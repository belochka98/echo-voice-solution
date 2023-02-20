package userService.service;

import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import userService.entity.User;

import java.util.Collection;
import java.util.UUID;

public interface UserService {
    User getUser(UUID userId);

    Collection<User> getAllUsers();

    User saveUser(User user);

    Collection<User> saveUsers(Collection<User> users);

    void deleteUser(UUID userId);

    Revisions<Long, User> getRevisions(UUID userId);

    Revision<Long, User> getLastRevision(UUID userId);
}
