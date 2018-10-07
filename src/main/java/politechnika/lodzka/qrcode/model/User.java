package politechnika.lodzka.qrcode.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
public class User implements UserDetails {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column
    @JsonIgnore
    private Date created;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @JsonIgnore
    private Date updated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @JsonIgnore
    private Date expiriedDate;

    @Column(nullable = false)
    private Boolean isLocked;

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.expiriedDate = null;
        this.isLocked = false;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        if (expiriedDate == null || new Date().before(this.expiriedDate)) {
            return true;
        }
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !this.isLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return isAccountNonExpired();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isAccountNonLocked();
    }
}
