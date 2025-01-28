package com.ww.WagerWave.Services;


import com.ww.WagerWave.Model.MyUser;
import com.ww.WagerWave.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordServices {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    //sprawdzenie czy hasło podane jest takie samo jak do konta
    //i potem zmiana hasła na nowe podane przez użytkownika
    public boolean verifyPassword(MyUser user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    public void updatePassword(MyUser user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        System.out.println("Updated password: " + encodedPassword); // Dla debuggowania
    }





}
