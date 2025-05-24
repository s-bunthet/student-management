package com.project.spring.controller;

import com.project.spring.model.RecaptchaResponse;
import com.project.spring.model.User;
import com.project.spring.service.EmailService;
import com.project.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final UserService userService;
    private final EmailService emailService;

    @Value("${spring.mail.fromResetPassword}")
    private String fromResetPassword;

    @Value("${recaptcha.secret-key}")
    private String recaptchaSecretKey;

    @Value("${recaptcha.verify-url}")
    private String recaptchaVerifyUrl;

    @Value("${recaptcha.site-key}")
    private String recaptchaSiteKey;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        // Add the reCAPTCHA site key to the model for rendering in the view
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        System.out.println(recaptchaSiteKey);
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email,
                                        @RequestParam("g-recaptcha-response") String recaptchaResponse,
                                        Model model) {

        RestTemplate restTemplate = new RestTemplate();
        String params = "?secret=" + recaptchaSecretKey + "&response=" + recaptchaResponse;

        RecaptchaResponse recaptchaResult = restTemplate.postForObject(recaptchaVerifyUrl + params, null, RecaptchaResponse.class);

        if (recaptchaResult == null || !recaptchaResult.isSuccess()) {
            model.addAttribute("message", "reCAPTCHA verification failed.");
            return "auth/forgot-password";
        }

        Optional<User> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
            userService.save(user);

            String resetLink = "http://localhost:8280/reset-password?token=" + token;
            String htmlBody = """
                                <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                                    Dear\s""" + user.getUsername() + """
                                    ,
                                    <p>We received a request to reset your password. Click the link below to reset your password:</p>
                                    <p>
                                        <a href='""" + resetLink + """
                                                  ' style="display: inline-block; padding: 10px 20px; color: #fff; background-color: #4CAF50; 
                                                  text-decoration: none; border-radius: 5px;">
                                            Reset Password
                                        </a>
                                    </p>
                                    <p style="margin-top: 20px;">This link will expire on: 
                                        <strong>""" + user.getResetTokenExpiry().toString() + """
                                    </strong>
                                    </p>
                                    <p>If you did not request this, please ignore this email.</p>
                                    <p style="margin-top: 20px; font-size: 0.9em; color: #777;">
                                        &copy; 2025 StudentManagement. All rights reserved.
                                    </p>
                                </div>""";
            emailService.sendHtmlEmail(fromResetPassword, user.getEmail(), "Reset Password", htmlBody);
        }
        model.addAttribute("message", "If your email is registered, you will receive a reset link.");
        return "auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);

        Optional<User> optionalUser = userService.findByResetToken(token);
        if (optionalUser.isEmpty() || optionalUser.get().getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Invalid or expired reset token");
            return "/login"; // Redirect to login if token is invalid or expired
        }

        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String token,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "auth/reset-password";
        }

        Optional<User> optionalUser = userService.findByResetToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userService.updatePassword(user, password);
            user.setResetToken(null);
            userService.save(user);
            return "redirect:/login?resetSuccess";
        } else {
            model.addAttribute("error", "Invalid reset token");
            return "auth/reset-password";
        }
    }

}
