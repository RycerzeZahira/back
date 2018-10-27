package politechnika.lodzka.qrcode.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import politechnika.lodzka.qrcode.model.scheme.Answer;
import politechnika.lodzka.qrcode.model.scheme.Element;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table(name = "FORM_T")
public class Form extends BaseEntity {

    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    private Group group;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRED_DATE")
    private Date expiredDate;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ELEMENT_ID", referencedColumnName = "ID")
    private Element root;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @JsonManagedReference
    @OneToMany(mappedBy = "form")
    private Collection<Answer> answers;

    public Form(Group group, Date expiredDate, Element root, String code) {
        this.group = group;
        this.expiredDate = expiredDate;
        this.root = root;
        this.code = code;
    }
}