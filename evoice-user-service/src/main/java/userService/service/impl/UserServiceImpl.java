package userService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import userService.entity.User;
import userService.repository.UserRepository;
import userService.service.UserService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> saveUsers(List<User> users) {
        return userRepository.saveAllAndFlush(users);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Revisions<Long, User> getRevisions(UUID userId) {
        return userRepository.findRevisions(userId);
    }

    @Override
    public Revision<Long, User> getLastRevision(UUID userId) {
        return userRepository.findLastChangeRevision(userId).orElse(null);
    }
}
