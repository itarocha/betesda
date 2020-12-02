package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.exception.AppException;
import br.com.itarocha.betesda.adapter.out.persistence.entity.RoleEntity;
import br.com.itarocha.betesda.domain.enums.RoleNameEnum;
import br.com.itarocha.betesda.adapter.out.persistence.entity.UserEntity;
import br.com.itarocha.betesda.adapter.ApiResponse;
import br.com.itarocha.betesda.payload.JwtAuthenticationResponse;
import br.com.itarocha.betesda.adapter.LoginRequest;
import br.com.itarocha.betesda.adapter.SignUpRequest;
import br.com.itarocha.betesda.adapter.out.persistence.repository.RoleRepository;
import br.com.itarocha.betesda.adapter.out.persistence.repository.UserRepository;
import br.com.itarocha.betesda.security.JwtTokenProvider;
import br.com.itarocha.betesda.application.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    EmailService emailService;

    @PostMapping("/login")
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

    @PostMapping("/assinar") // novo_usuario
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
        UserEntity userEntity = new UserEntity(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        RoleEntity userRole = roleRepository.findByName(RoleNameEnum.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        userEntity.setRoles(Collections.singleton(userRole));

        UserEntity result = userRepository.save(userEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Usuário registrado com sucesso!"));
    }
    
 }