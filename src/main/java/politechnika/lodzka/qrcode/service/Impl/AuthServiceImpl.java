package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import politechnika.lodzka.qrcode.exception.UserNotActivatedException;
import politechnika.lodzka.qrcode.exception.UserNotFoundException;
import politechnika.lodzka.qrcode.model.user.AccountStatus;
import politechnika.lodzka.qrcode.model.user.User;
import politechnika.lodzka.qrcode.model.request.AuthenticationRequest;
import politechnika.lodzka.qrcode.repository.UserRepository;
import politechnika.lodzka.qrcode.security.JwtTokenProvider;
import politechnika.lodzka.qrcode.service.AuthService;

@Service
class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication auth(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getLogin(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User userOpt = userRepository.getUserByEmail(authenticationRequest.getLogin()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(userOpt.getStatus().equals(AccountStatus.INACTIVE)){
            throw new UserNotActivatedException("User is not activated");
        }

        return authentication;
    }

    @Override
    public String getAuthenticationToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.getUserByEmail(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        User current = getCurrentUser();
        if (auth(new AuthenticationRequest(current.getEmail(), oldPassword)).isAuthenticated()) {
            current.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(current);
            return true;
        }
        return false;
    }
}
