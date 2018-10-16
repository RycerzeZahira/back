package politechnika.lodzka.qrcode.model.scheme;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import politechnika.lodzka.qrcode.model.BaseEntity;

import javax.persistence.*;

@Data
@AllArgsConstructor
@MappedSuperclass
public class SchemeInformation extends BaseEntity {
    @Column(name = "CODE", nullable = false)
    private String code;
    @Column(name = "NAME", nullable = false)
    private String name;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARENT_ID")
    private Element parent;

    public SchemeInformation(String code) {
        this.code = code;
        this.name = code.toLowerCase();
    }

    public SchemeInformation() {
    }

    @JsonIgnore
    public boolean isObjectValue() {
        return false;
    }
}
