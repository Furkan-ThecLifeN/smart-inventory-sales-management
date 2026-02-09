package com.company.inventory.service;

import com.company.inventory.config.JwtService;
import com.company.inventory.dto.AuthRequestDto;
import com.company.inventory.dto.AuthResponseDto;
import com.company.inventory.dto.UserRequestDto;
import com.company.inventory.model.ERole;
import com.company.inventory.model.User;
import com.company.inventory.repository.RoleRepository;
import com.company.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public String register(UserRequestDto request) {
        var user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var salesRole = roleRepository.findByName(ERole.ROLE_SALES)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRoles(Set.of(salesRole));

        userRepository.save(user);
        return "User registered successfully";
    }

    public AuthResponseDto login(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var roles = userEntity.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        var userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(jwtToken)
                .username(request.getUsername())
                .roles(roles)
                .build();
    }
}
