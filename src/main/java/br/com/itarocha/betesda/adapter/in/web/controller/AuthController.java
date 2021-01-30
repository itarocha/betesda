package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.RedefinicaoSenhaRequest;
import br.com.itarocha.betesda.application.port.in.AutenticacaoUseCase;
import br.com.itarocha.betesda.domain.User;
import br.com.itarocha.betesda.adapter.in.web.security.AuthenticationToken;
import br.com.itarocha.betesda.domain.UserLogin;
import br.com.itarocha.betesda.domain.UserToSave;
import br.com.itarocha.betesda.security.JwtTokenProvider;
import br.com.itarocha.betesda.application.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacaoUseCase service;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationToken> authenticateUser(@Valid @RequestBody UserLogin userLogin) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.getUsernameOrEmail(),
                        userLogin.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = tokenProvider.generateToken(authentication);

        // Deve sair
        //emailService.redefinirSenha("somebody@gmail.com", "Some Name", jwt);
        
        return ResponseEntity.ok(AuthenticationToken.builder().accessToken(jwtToken).build());
    }

    //TODO Criar endpoint para redefinir senha

    // TODO Criar endpoint para gerenciamento de usuários. list, create, details, update(alterar roles), desabilitar
    @PostMapping("/assinar") // novo_usuario
    @PreAuthorize("hasAnyRole('ROOT')")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserToSave userToSave) {
        return ResponseEntity.ok().body(service.create(userToSave));
    }

    @PostMapping("/redefinir_senha")
    public ResponseEntity<?> redefinirSenha(@RequestBody RedefinicaoSenhaRequest detalhes){
        emailService.redefinirSenha(detalhes.getEmailDestinatario(), detalhes.getNome(), detalhes.getToken());
        return ResponseEntity.noContent().build();
    }
    
 }