package com.example.watch_selling.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.ForgetPasswordDto;
import com.example.watch_selling.dtos.LoginDto;
import com.example.watch_selling.dtos.RecoverPasswordEmailBody;
import com.example.watch_selling.dtos.RegisterDto;
import com.example.watch_selling.dtos.ResponseDto;
import com.example.watch_selling.mailing.EmailServiceImpl;
import com.example.watch_selling.model.Account;
import com.example.watch_selling.model.Customer;
import com.example.watch_selling.repository.AccountRepository;
import com.example.watch_selling.repository.CustomerRepository;

@Service
public class AuthenticationService {
    private final AccountRepository accountRepository;

    private final CustomerRepository customerRepository;

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final CartService cartService;

    private final EmailServiceImpl emailServiceImpl;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";

    public AuthenticationService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            AccountService accountService,
            CartService cartService,
            EmailServiceImpl emailServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.cartService = cartService;
        this.emailServiceImpl = emailServiceImpl;
    }

    public Account signup(RegisterDto input) {
        if (input.getEmail() == null || input.getEmail().isEmpty()) {
            throw new BadCredentialsException("Email is required");
        }

        if (input.getPassword() == null || input.getPassword().isEmpty()) {
            throw new BadCredentialsException("Password is required");
        }

        if (input.getConfirmPassword() == null || input.getConfirmPassword().isEmpty()) {
            throw new BadCredentialsException("Confirm password is required");
        }

        if (!isValidEmail(input.getEmail())) {
            throw new BadCredentialsException("Email is invalid");
        }

        if (!input.getPassword().equals(input.getConfirmPassword())) {
            throw new BadCredentialsException("Password and confirm password do not match");
        }

        if (accountService.findByEmail(input.getEmail()).isPresent()) {
            throw new BadCredentialsException("Email is already taken");
        }

        Account buffer = new Account();
        buffer.setEmail(input.getEmail());
        buffer.setPassword(passwordEncoder.encode(input.getPassword()));
        buffer.setIsDeleted(false);
        Account newAccount = accountRepository.save(buffer);
        cartService.createNewCart(newAccount.getId());

        ZonedDateTime zonedDateTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        long epochMilli = zonedDateTime.toInstant().toEpochMilli();
        String dummyValue = Long.toString(epochMilli);

        Customer newProfile = new Customer();
        newProfile.setCitizenId(dummyValue);
        newProfile.setFirstName("Họ");
        newProfile.setLastName("Tên");
        newProfile.setPhoneNumber(dummyValue);
        newProfile.setGender("Nam");
        newProfile.setDateOfBirth(LocalDate.now());
        newProfile.setAddress("Địa chỉ");
        newProfile.setTaxCode("Mã số thuế");
        newProfile.setPhoto(null);
        newProfile.setAccount(newAccount);
        newProfile.setIsDeleted(false);

        this.customerRepository.save(newProfile);

        return newAccount;
    }

    public Account authenticate(LoginDto input) {
        if (input.getEmail() == null || input.getEmail().isEmpty()) {
            throw new BadCredentialsException("Email is required");
        }

        if (input.getPassword() == null || input.getPassword().isEmpty()) {
            throw new BadCredentialsException("Password is required");
        }

        if (!isValidEmail(input.getEmail())) {
            throw new BadCredentialsException("Email is invalid");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()));

        return accountRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public ResponseDto<String> recoverPassword(ForgetPasswordDto payload) {
        Optional<Account> account = this.accountService.findByEmail(payload.getEmail());
        if (account.isEmpty()) {
            return new ResponseDto<>(
                    "Bad request", "Invalid email!", HttpStatus.BAD_REQUEST);
        }

        String recipient = payload.getEmail();
        String newPassword = this.generateRandomPassword(12);
        String subject = "Password recovery";
        RecoverPasswordEmailBody body = new RecoverPasswordEmailBody();
        body.setAccountEmail(recipient);
        body.setContact(this.emailServiceImpl.getSendingEmail());
        body.setNewPassword(subject);
        body.setNewPassword(newPassword);

        Boolean savingNewPassword = accountService.updatePassword(recipient, newPassword);
        if (!savingNewPassword) {
            return new ResponseDto<>(
                    "Failed!", "Cannot save new password!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseDto<Boolean> emailSender = this.emailServiceImpl.sendEmail(recipient, subject, body.toHtmlBody());
        if (emailSender.getData() == false) {
            return new ResponseDto<>(
                    "Failed!", "Cannot send email!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseDto<>(
                "Success", "Success", HttpStatus.OK);
    }

    private Boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }

    private String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        Random random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
