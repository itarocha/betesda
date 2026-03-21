package br.com.itarocha.betesda.controller;

import br.com.itarocha.betesda.exception.AppException;
import br.com.itarocha.betesda.model.RoleEntity;
import br.com.itarocha.betesda.model.RoleName;
import br.com.itarocha.betesda.model.UserEntity;
import br.com.itarocha.betesda.payload.ApiResponse;
import br.com.itarocha.betesda.payload.JwtAuthenticationResponse;
import br.com.itarocha.betesda.payload.LoginRequest;
import br.com.itarocha.betesda.payload.SignUpRequest;
import br.com.itarocha.betesda.repository.RoleEntityRepository;
import br.com.itarocha.betesda.repository.UserEntityRepository;
import br.com.itarocha.betesda.security.JwtTokenProvider;
import br.com.itarocha.betesda.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserEntityRepository userRepository;

    @Autowired
    RoleEntityRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    EmailService emailService;

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        // Deve sair
        //emailService.redefinirSenha("somebody@gmail.com", "Some Name", jwt);
        
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @RequestMapping(value="/assinar", method = RequestMethod.POST) // novo_usuario
    @PreAuthorize("hasAnyRole('ROOT')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    	System.out.println(signUpRequest);
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
        	return new ResponseEntity(new ApiResponse(false, "Nome de usuário já cadastrado!"), HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email já está em uso!"), HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        UserEntity user = new UserEntity(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        UserEntity result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Usuário registrado com sucesso!"));
    }
    
 }