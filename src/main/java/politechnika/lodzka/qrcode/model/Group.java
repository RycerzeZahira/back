package politechnika.lodzka.qrcode.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import politechnika.lodzka.qrcode.model.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "GROUP_T")
public class Group extends BaseEntity implements Serializable {

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "MODERATOR_ID", referencedColumnName = "ID", nullable = false)
    private User moderator;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "GROUP_USER_T",
            joinColumns = {@JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")}
    )
    private Set<User> users;

    @JsonBackReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Form> forms;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CODE", unique = true, nullable = false)
    private String code;

    @Column(name = "PUBLIC", nullable = false)
    private boolean publicGroup;
}
