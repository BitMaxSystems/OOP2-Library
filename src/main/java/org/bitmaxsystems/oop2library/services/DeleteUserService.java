package org.bitmaxsystems.oop2library.services;

import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class DeleteUserService {
    private GenericRepository<User> userGenericRepository = new GenericRepository<>(User.class);

    public void deleteUser(User user)
    {
        userGenericRepository.delete(user);
    }
}
