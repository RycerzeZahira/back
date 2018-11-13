package politechnika.lodzka.qrcode.scheme;

import org.junit.Test;
import politechnika.lodzka.qrcode.model.scheme.Element;
import politechnika.lodzka.qrcode.model.scheme.SchemeBuilder;
import politechnika.lodzka.qrcode.model.scheme.SchemeGroup;
import politechnika.lodzka.qrcode.model.scheme.TypeClass;


public class SchemeRepositoryTest {
    @Test
    public void createSchemeTest() {
        SchemeBuilder schemeBuilder = new SchemeBuilder();
        SchemeGroup root = new SchemeGroup("ROOT", "root");
        schemeBuilder.addElement(root);
        schemeBuilder.addElement(new Element(TypeClass.DATE, "DATE", "date", root));
        SchemeGroup schemeGroup = new SchemeGroup("GROUP", "schemeGroup", root);
        schemeBuilder.addElement(schemeGroup);
        schemeBuilder.addElement(new Element(TypeClass.STRING, "STRING", "string", schemeGroup));

        root = schemeBuilder.build();
        assert (root.getElements().size() == 2);
    }
}
