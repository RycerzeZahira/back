package politechnika.lodzka.qrcode.service.Impl;

import org.springframework.stereotype.Service;
import politechnika.lodzka.qrcode.model.request.scheme.ElementRequest;
import politechnika.lodzka.qrcode.model.scheme.Element;
import politechnika.lodzka.qrcode.model.scheme.SchemeGroup;
import politechnika.lodzka.qrcode.model.scheme.TypeClass;
import politechnika.lodzka.qrcode.service.SchemeService;

import java.util.Arrays;
import java.util.Collection;

@Service
class SchemeServiceImpl implements SchemeService {
    @Override
    public Collection<TypeClass> getAvailableFields() {
        return Arrays.asList(TypeClass.values());
    }

    @Override
    public boolean checkScheme(Element element) {
        return element.getType() == TypeClass.GROUP;
    }

    @Override
    public boolean validate(Element scheme, ElementRequest answer) {
        return checkScheme(scheme) && validateGroup((SchemeGroup) scheme, answer);
    }

    private boolean validateGroup(SchemeGroup group, ElementRequest request) {
        if (!group.getCode().equals(request.getCode())) {
            return false;
        }
        for (ElementRequest element : (Collection<ElementRequest>) request.getElement()) {
            Element e = group.getElements().stream().filter(el -> el.getCode().equals(element.getCode())).findFirst().get();
            if (e == null) {
                return false;
            }
            if (!e.getType().getClazz().isInstance(element.getElement())) {
                return false;
            }
            if (TypeClass.GROUP.equals(e.getType())) {
                if (!validateGroup((SchemeGroup) e, element)) {
                    return false;
                }
            }
        }
        return true;
    }
}
