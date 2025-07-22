package com.Polimeras.Service;

import com.Polimeras.Entity.Users;
import com.Polimeras.Repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EmailService emailService;

    public Optional<Users> getUserDetails(Principal principal) {
        return usersRepository.findByUsername(principal.getName());
    }

    public Users setNewUSer(Users user) {
        user.setRole(user.getRole());
        user.setCreatedAt(LocalDateTime.now());
        Users users = usersRepository.save(user);
        if (users.isActive()) {
            emailService.newRegister(users.getFirstname(), users.getId(), users.getEmail());
        }
        return users;
    }

    public boolean existsByUsername(String username) {
        return usersRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }

    public List<Users> findByRole(String role) {
        return usersRepository.findByRole(role);
    }

    public Optional<Users> findById(Long id) {
        return usersRepository.findById(id);
    }

    public Users updateUserStatus(Long id, boolean enabled) {
        Optional<Users> user = findById(id);
        if (user.isPresent()) {
            Users u = user.get();
            u.setEnabled(enabled);
            return usersRepository.save(u);
        }
        return null;
    }

    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

    @Transactional
    public Users toggleStatus(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName().equals(user.getUsername())) {
            throw new IllegalStateException("You cannot disable your own account");
        }

        user.setEnabled(!user.isEnabled());
        return user;                // JPA will flush automatically at tx‑commit
    }


    public void blockUser(Users user) {
        user.setRole("BLOCKED");
        user.setEnabled(false);
        user.setActive(false);
        Users users = usersRepository.save(user);
        if (users.getRole().equals("BLOCKED")){
            emailService.sendBlockNotification(users.getEmail(), users.getUsername());
            System.out.println(users.getEmail());
        }
    }

    public void approveUser(Users user) {
        user.setRole("ROLE_USER");   // or any default role you want
        user.setEnabled(true);
        user.setActive(true);
        Users users = usersRepository.save(user);
        if (users.getRole().equals("ROEL_USER")){
            System.out.println(users);
            emailService.sendApprovalNotification(users.getEmail(), users.getUsername());
        }
    }
}
