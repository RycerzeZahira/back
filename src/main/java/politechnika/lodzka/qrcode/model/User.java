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
@Table(name = "USER_T")
public class User extends BaseEntity implements UserDetails {

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @JsonIgnore
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "CREATED")
    @JsonIgnore
    private Date created;

    @Column(name = "UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @JsonIgnore
    private Date updated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRED_DATE")
    @JsonIgnore
    private Date expiredDate;

    @Column(name = "IS_LOCKED", nullable = false)
    private Boolean isLocked;

    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.password = password;
        this.expiredDate = null;
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
        if (expiredDate == null || new Date().before(this.expiredDate)) {
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
