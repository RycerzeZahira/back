package politechnika.lodzka.qrcode.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import politechnika.lodzka.qrcode.model.user.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "GROUP_T")
public class Group extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "MODERATOR_ID", referencedColumnName = "ID", nullable = false)
    private User moderator;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "GROUP_USER_T",
            joinColumns = {@JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")}
    )
    private Set<User> users;

    @JsonBackReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Form> forms;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CODE", unique = true, nullable = false)
    private String code;
}
