package userService.service;

import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import userService.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getUser(UUID userId);

    List<User> getAllUsers();

    User saveUser(User user);

    List<User> saveUsers(List<User> users);

    void deleteUser(UUID userId);

    Revisions<Long, User> getRevisions(UUID userId);

    Revision<Long, User> getLastRevision(UUID userId);
}
