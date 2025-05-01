package com.project.spring.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Table(name = "app_users") // Renamed to avoid conflict with SQL keywords in postgresSql
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String email;
    private String password;
    private boolean enabled;

    // Add other fields as needed

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the user's roles or authorities
        return null; // Replace with actual implementation
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize as needed
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
