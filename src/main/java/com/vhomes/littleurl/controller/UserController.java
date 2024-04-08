package com.vhomes.littleurl.controller;

import com.vhomes.littleurl.DTOS.CreateUserDTO;
import com.vhomes.littleurl.DTOS.LoginRequestDTO;
import com.vhomes.littleurl.DTOS.LoginResponseDTO;
import com.vhomes.littleurl.models.Role;
import com.vhomes.littleurl.models.User;
import com.vhomes.littleurl.repository.RoleRepository;
import com.vhomes.littleurl.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Void> register(@RequestBody CreateUserDTO createUserDTO) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userExists = userRepository.findByUsername(createUserDTO.username());

        if (userExists.isPresent()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        var user = new User();
        user.setName(createUserDTO.name());
        user.setUsername(createUserDTO.username());
        user.setPassword(passwordEncoder.encode(createUserDTO.password()));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        var user = userRepository.findByUsername(loginRequestDTO.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequestDTO, passwordEncoder)) {
            throw new BadCredentialsException("user or password is invalid");
        }

        var now = Instant.now();
        var expiresIn = 3600L;
        var scopes = user.get().getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("backend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponseDTO(jwtValue, expiresIn));
    }


}
