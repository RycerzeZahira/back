package politechnika.lodzka.qrcode.service;

import politechnika.lodzka.qrcode.model.request.scheme.ElementRequest;
import politechnika.lodzka.qrcode.model.scheme.Element;
import politechnika.lodzka.qrcode.model.scheme.TypeClass;

import java.util.Collection;

public interface SchemeService {
    Collection<TypeClass> getAvailableFields();

    boolean checkScheme(Element element);

    boolean validate(Element scheme, ElementRequest answer);
}
