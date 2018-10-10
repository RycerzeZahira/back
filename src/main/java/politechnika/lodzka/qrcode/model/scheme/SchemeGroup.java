package politechnika.lodzka.qrcode.model.scheme;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table(name = "ELEMENT_T")
public class SchemeGroup extends Element {
    @JsonManagedReference
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Collection<Element> elements = new ArrayList<>();

    public SchemeGroup(String code, String name, Element parent) {
        super(TypeClass.GROUP, code, name, parent);
    }

    public SchemeGroup(String code, String name) {
        super(TypeClass.GROUP, code, name, null);
    }

    public SchemeGroup(String code){
        super(code);
    }

    public SchemeGroup() {
    }
}
