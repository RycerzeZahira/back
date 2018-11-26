package politechnika.lodzka.qrcode.model.scheme;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.Data;
import politechnika.lodzka.qrcode.exception.AppParseException;
import politechnika.lodzka.qrcode.exception.scheme.TypeException;
import politechnika.lodzka.qrcode.model.BaseEntity;
import politechnika.lodzka.qrcode.model.Form;
import politechnika.lodzka.qrcode.model.user.User;

import javax.persistence.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Entity
@Table(name = "ANSWER_T")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Answer extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "SCHEME_ID", referencedColumnName = "ID")
    private Element scheme;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "FORM_ID", referencedColumnName = "ID")
    private Form form;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent")
    private Collection<Answer> childs;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    private Answer parent;

    @JsonIgnore
    @Column(name = "STRING_VALUE")
    private String stringValue;

    @JsonIgnore
    @Column(name = "BOOlEAN_VALUE")
    private Boolean booleanValue;

    @JsonIgnore
    @Column(name = "LONG_VALUE")
    private Long longValue;

    @JsonIgnore
    @Column(name = "DOUBLE_VALUE")
    private Double doubleValue;

    @JsonIgnore
    @Column(name = "DATE_VALUE")
    private Date dateValue;

    @JsonIgnore
    @Column(name = "OBJECT_ID_VALUE")
    private Long objectIdValue;

    public Object getValue() {
        Class clazz = scheme.getType().getClazz();
        if (clazz == String.class) {
            return this.stringValue;
        } else if (clazz == AtomicLong.class) {
            return this.longValue;
        } else if (clazz == AtomicBoolean.class) {
            return this.booleanValue;
        } else if (clazz == AtomicDouble.class) {
            return this.doubleValue;
        } else if (clazz == Date.class) {
            return this.dateValue;
        } else if (scheme.isObjectValue()) {
            return this.objectIdValue;
        }
        return null;
    }

    public void setValue(Object value) {
        TypeClass type = scheme.getType();
        try {
            switch (type) {
                case STRING:
                    this.stringValue = (String) value;
                    break;
                case BOOLEAN:
                    this.booleanValue = (Boolean) value;
                    break;
                case LONG:
                    this.longValue = (Long) value;
                    break;
                case DOUBLE:
                    this.doubleValue = (Double) value;
                    break;
                case GROUP:
                    break;
                case DATE:
                    this.dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mmZ").parse((String) value);
                    break;
                default:
                    this.objectIdValue = NumberFormat.getInstance().parse(String.valueOf(value)).longValue();
                    break;
            }
        } catch (ParseException ex) {
            throw new AppParseException(ex.getMessage());
        }
    }

    @Transient
    @JsonIgnore
    public String getParentCode() {
        return this.getParent().getScheme().getCode();
    }
}
