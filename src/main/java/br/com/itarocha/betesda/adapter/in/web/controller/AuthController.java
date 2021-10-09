package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.EmailRequest;
import br.com.itarocha.betesda.adapter.dto.RedefinicaoSenha;
import br.com.itarocha.betesda.core.ports.in.AutenticacaoUseCase;
import br.com.itarocha.betesda.domain.User;
import br.com.itarocha.betesda.adapter.in.web.security.AuthenticationToken;
import br.com.itarocha.betesda.domain.UserLogin;
import br.com.itarocha.betesda.domain.UserToSave;
import br.com.itarocha.betesda.security.JwtTokenProvider;

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

    private final AutenticacaoUseCase autenticacaoService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    //private final EmailService emailService;

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


    // TODO Criar endpoint para gerenciamento de usuários. list, create, details, update(alterar roles), desabilitar
    @PostMapping("/assinar") // novo_usuario
    @PreAuthorize("hasAnyRole('ROOT')")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserToSave userToSave) {
        return ResponseEntity.ok().body(autenticacaoService.create(userToSave));
    }

    @PostMapping("/solicitar-nova-senha")
    public ResponseEntity<?> solicitarNovaSenha(@Valid @RequestBody EmailRequest request){
        //emailService.solicitarSenha(detalhes.getEmailDestinatario(), detalhes.getNome(), detalhes.getToken());
        autenticacaoService.solicitarEnvioSenha(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<?> redefinirSenha(@Valid @RequestBody RedefinicaoSenha request){
        autenticacaoService.redefinirSenha(request);
        return ResponseEntity.ok().build();
    }
 }