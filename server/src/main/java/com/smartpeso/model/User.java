package com.smartpeso.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@JsonIgnoreProperties({ "userId", "password" })
public class User implements UserDetails {
        @Id
        @Getter
        @Setter
        private int userId;

        @Getter
        private String email;

        private final String password;

        @Getter
        private String role;

        @Getter
        private String firstName;

        @Getter
        private String lastName;

        public User(int userId, String email, String password, String role, String firstName, String lastName) {
                this.userId = userId;
                this.email = email;
                this.password = password;
                this.role = role;
                this.firstName = firstName;
                this.lastName = lastName;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(this.role));
                return authorities;
        }

        @Override
        public String getPassword() {
                return password;
        }

        @Override
        public String getUsername() {
                return email;
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
