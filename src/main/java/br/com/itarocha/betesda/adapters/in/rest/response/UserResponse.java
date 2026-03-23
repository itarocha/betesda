package br.com.itarocha.betesda.adapters.in.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserResponse implements Serializable {
    private Long id;
    private String name;
    private String username;
    private String email;
}
