package org.strokova.gamestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.strokova.gamestore.exception.AuthorizationRequiredException;
import org.strokova.gamestore.exception.UserNotFoundException;
import org.strokova.gamestore.model.User;
import org.strokova.gamestore.repository.UserRepository;

/**
 * @author vstrokova, 07.10.2016.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new AuthorizationRequiredException("Bad username");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("No user with username: " + username + " was found");
        }

        return user;
    }
}
