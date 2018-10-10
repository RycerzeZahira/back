package politechnika.lodzka.qrcode.model.scheme;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ELEMENT_T")
public class Element extends SchemeInformation {
    @Column(name = "TYPE")
    private TypeClass type;

    public Element(TypeClass type, String code, String name, Element parent) {
        super(code, name, parent);
        this.type = type;
    }

    public Element(String code){
        super(code);
    }

    public Element() {
    }
}
