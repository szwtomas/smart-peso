package com.smartpeso.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Document
@JsonIgnoreProperties({ "userId", "password" })
public class User implements UserDetails {
        @Id
        @Getter
        @Field(name = "_id")
        private String userId;

        @Getter
        @Field(name = "email")
        private String email;

        @Field(name = "password")
        private String password;

        @Getter
        @Field(name = "role")
        private String role;

        @Getter
        @Field(name = "firstName")
        private String firstName;

        @Getter
        @Field(name = "lastName")
        private String lastName;

        public User(String email, String password, String role, String firstName, String lastName) {
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
