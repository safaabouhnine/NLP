package org.example.backend.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.model.User;
import org.example.backend.service.EmailService;
import org.example.backend.service.EmailTemplateName;
import org.example.backend.service.UserService;
import org.example.backend.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    private final JwtUtil jwtUtil;




    public UserController(UserService userService, EmailService emailService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            Map<String, Object> response = userService.registerUserAndGenerateToken(user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // Retourne un message d'erreur si le questionnaire n'est pas rempli
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?>loginUser(@RequestBody User loginUser) {
        // Extraire l'email et le mot de passe de l'utilisateur
        String email = loginUser.getEmail();
        String password = loginUser.getPassword();

        // Authentifier l'utilisateur via le service
        User authenticatedUser = userService.authenticateUser(email, password);

        if (authenticatedUser == null) {
            // Si l'authentification échoue, retourner une réponse UNAUTHORIZED
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        // Générer un token pour l'utilisateur authentifié
        String token = userService.generateTokenForUser(authenticatedUser);

        // Construire la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("user", authenticatedUser);
        response.put("token", token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            String resetToken = userService.generateResetToken(email);
            User user = userService.getUserByEmail(email);
            // TODO: Envoyer le token par email (intégrer un service d'email ici)
            System.out.println("Reset token: " + resetToken); // À remplacer par un envoi d'email
            String resetLink = "http://localhost:5173/reset-password?token=" + resetToken;
            String userFullName = user.getFirstName() + " " + user.getLastName();

            emailService.sendResetPasswordEmail(email,userFullName, EmailTemplateName.RESET_PASSWORD,resetLink,"Reset your password");




            return new ResponseEntity<>("Password reset email sent successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request){
            String token = request.get("token");
            String newPassword = request.get("newPassword");

            try {
                userService.resetPassword(token, newPassword);
                return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String token) {
        // Optionnel : Invalider le token en l'ajoutant à une liste noire
        String jwtToken = token.replace("Bearer ", ""); // Supprime le préfixe "Bearer "
        jwtUtil.addToBlacklist(jwtToken);

        // Réponse de succès
        return new ResponseEntity<>("Déconnexion réussie", HttpStatus.OK);

    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            // Extraire l'utilisateur du token
            String jwtToken = token.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(jwtToken);
            User user = userService.getUserByEmail(email);

            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }



}
