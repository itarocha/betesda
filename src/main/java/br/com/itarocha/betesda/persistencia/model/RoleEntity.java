package br.com.itarocha.betesda.persistencia.model;

import br.com.itarocha.betesda.model.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 64)
    private RoleName name;

    public RoleEntity(RoleName name) {
        this.name = name;
    }
}
