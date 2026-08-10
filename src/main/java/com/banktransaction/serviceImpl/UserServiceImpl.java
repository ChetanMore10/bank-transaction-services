package com.banktransaction.serviceImpl;

import com.banktransaction.entity.User;
import com.banktransaction.repository.UserRepo;
import com.banktransaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        if (user.getRole() == null || user.getRole().isBlank()){
            user.setRole("USER");
        }

        if (user.getStatus() == null || user.getStatus().isBlank()){
            user.setStatus("ACTIVE");
        }

        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("User Not found with id :" + id));
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found with id :" + id));

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setRole(user.getRole());
        existingUser.setStatus(user.getStatus());
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepo.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                        .orElseThrow(()-> new RuntimeException("User not found with id :"+ id));
        userRepo.delete(user);
    }
}
