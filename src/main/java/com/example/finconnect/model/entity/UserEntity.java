package com.example.finconnect.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(name = " \"user\"")
@SQLDelete(sql = "UPDATE \"user\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE userid = ?")
@SQLRestriction("deleteddatetime IS NULL")
public class UserEntity implements UserDetails {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    @Setter
    private String username;

    @Column
    @Setter
    private String password;

    @Getter
    @Setter
    @Column
    private String profile;

    @Getter
    @Setter
    @Column
    private String desription;

    @Getter
    @Setter
    @Column
    private ZonedDateTime createddatetime;

    @Getter
    @Setter
    @Column
    private ZonedDateTime updateddatetime;

    @Getter
    @Setter
    @Column
    private ZonedDateTime deleteddatetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(profile, that.profile) && Objects.equals(desription, that.desription) && Objects.equals(createddatetime, that.createddatetime) && Objects.equals(updateddatetime, that.updateddatetime) && Objects.equals(deleteddatetime, that.deleteddatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, profile, desription, createddatetime, updateddatetime, deleteddatetime);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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

    public static UserEntity of(String username, String password) {
        var userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);

        userEntity.setProfile("https://avatar.iran.liara.run/piublic/" + new Random().nextInt(100));

        return userEntity;
    }

    @PrePersist
    private void prePersist() {
        this.createddatetime = ZonedDateTime.now();
        this.updateddatetime = this.createddatetime;
    }

    @PreUpdate
    private void preUpdate() {
        this.updateddatetime = ZonedDateTime.now();
    }
}
