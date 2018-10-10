package politechnika.lodzka.qrcode.model.scheme;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.util.concurrent.AtomicDouble;
import politechnika.lodzka.qrcode.exception.scheme.TypeException;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Podczas dodawania nowego typu, który będzie klasą należy zmodyfikować funkcją isObjectValue w klasie SchemeInformation
 */
public enum TypeClass {
    STRING("STRING", String.class, false),
    BOOLEAN("BOOLEAN", AtomicBoolean.class, false),
    LONG("LONG", AtomicLong.class, false),
    DOUBLE("DOUBLE", AtomicDouble.class, false),
    GROUP("GROUP", SchemeGroup.class, false),
    DATE("DATE", Date.class, false);

    private String code;
    private Class clazz;
    private Boolean selectable;

    TypeClass(String code, Class clazz, Boolean selectable) {
        this.code = code;
        this.clazz = clazz;
        this.selectable = selectable;
    }

    public static Boolean isSelectable(Class clazz) {
        for (TypeClass t : TypeClass.values()) {
            if (t.getClazz().equals(clazz)) {
                return t.getSelectable();
            }
        }
        throw new TypeException("Not found class: " + clazz.getName() + " in TypeClass enum");
    }

    public static Class getClass(String code) {
        for (TypeClass t : TypeClass.values()) {
            if (t.getCode().equals(code)) {
                return t.getClazz();
            }
        }
        throw new TypeException("Not found code: " + code + " in TypeClass enum");
    }

    public static TypeClass getTypeClassByClass(Class clazz) {
        for (TypeClass t : TypeClass.values()) {
            if (clazz.equals(t.clazz)) {
                return t;
            }
        }
        throw new TypeException("Not found class: " + clazz.getName() + " in TypeClass enum");
    }

    public static TypeClass getTypeClassByCode(String code) {
        for (TypeClass t : TypeClass.values()) {
            if (code.equals(t.code)) {
                return t;
            }
        }
        throw new TypeException("Not found code: " + code + " in TypeClass enum");
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public Class getClazz() {
        return clazz;
    }

    public Boolean getSelectable() {
        return selectable;
    }

}
