package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.recsys.recPet.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    private String nome;
    private String telefone;
    private String genero;
    private LocalDate dataNascimento;
    private String cpf;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Endereco endereco;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_adocao",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "adocao_id")
    )
    private Set<Adocao> adocoes = new HashSet<>();



    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Questionario questionario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = tipoUsuario.name();
        return tipoUsuario == null ? new HashSet<>() :
                Set.of(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
