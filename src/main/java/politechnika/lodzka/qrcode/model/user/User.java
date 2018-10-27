package politechnika.lodzka.qrcode.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import politechnika.lodzka.qrcode.model.BaseEntity;
import politechnika.lodzka.qrcode.model.Group;

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

    @Column(name = "IS_ACTIVE", nullable = false)
    private AccountStatus status;

    @OneToMany(mappedBy = "moderator")
    private Collection<Group> moderatedGroups;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.expiredDate = null;
        this.isLocked = false;
        this.status = AccountStatus.INACTIVE;
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
        return expiredDate == null || new Date().before(this.expiredDate);
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
