package bo.edu.ucb.todolist.service;

import bo.edu.ucb.todolist.dto.AuthDto;
import bo.edu.ucb.todolist.dto.UserDto;
import bo.edu.ucb.todolist.entity.Users;
import bo.edu.ucb.todolist.repository.UserRepository;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bo.edu.ucb.todolist.security.JwtUtil;
import bo.edu.ucb.todolist.security.SHA256PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);

    public AuthDto login(AuthDto authDto) {
        log.info("Login attempt for user: {}", authDto.getEmail());
        Users user = userRepository.findByEmail(authDto.getEmail());
        if (user == null) {
            log.error("User not found: {}", authDto.getEmail());
            throw new RuntimeException("Usuario no encontrado");
        }
        SHA256PasswordEncoder passwordEncoder = new SHA256PasswordEncoder();
        if (!passwordEncoder.hash(authDto.getPassword()).equals(user.getPassword())) {
            log.error("Incorrect password for user: {}", authDto.getEmail());
            throw new RuntimeException("Contraseña incorrecta");
        }
        String token = JwtUtil.generateToken(user.getId(), user.getEmail());
        authDto.setToken(token);
        log.info("Login successful for user: {}", authDto.getEmail());
        return authDto;
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Creating user: {}", userDto.toString());
        SHA256PasswordEncoder passwordEncoder = new SHA256PasswordEncoder();
        String hashedPassword = passwordEncoder.hash(userDto.getPassword());
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setPassword(hashedPassword);
        user.setUsername(userDto.getUserName());
        Users savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getEmail());
        return new UserDto(savedUser.getUsername(), savedUser.getEmail(), null);
    }

    public Users getUserByEmail(String email) {
        log.info("Retrieving user by email: {}", email);
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found: {}", email);
            throw new RuntimeException("Usuario no encontrado");
        }
        return user;
    }

    public Users validateToken(String authHeader) {
        JwtUtil jwtUtil = new JwtUtil();
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Token inválido");
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            Users user = getUserByEmail(email);
            if (user == null) {
                throw new RuntimeException("Usuario no encontrado");
            }

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el token", e);
        }
    }

}
