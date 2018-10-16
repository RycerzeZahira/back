package politechnika.lodzka.qrcode.model.scheme;

import politechnika.lodzka.qrcode.exception.AppException;

import java.util.Optional;

public class SchemeBuilder {

    private SchemeGroup root = null;

    public void addElement(Element element) {
        if (element.getParent() == null) {
            if (root == null && element.getType().equals(TypeClass.GROUP)) {
                root = (SchemeGroup) element;
            } else {
                throw new AppException(new StringBuilder().append("Trying to override root by group: ")
                        .append(element.getCode()).append(" or element is not GROUP").toString());
            }
        } else if (root == null) {
            throw new AppException(new StringBuilder().append("Cannot add element without parent code. Element: ")
                    .append(element.getCode()).toString());
        } else {
            ((SchemeGroup) findElementInScheme(element.getParent().getCode())).getElements().add(element);
        }
    }

    public Element findElementInScheme(String code) {
        return findElementByCodeInGroup(code, root).orElseThrow(() -> new AppException(new StringBuilder()
                .append("Element not found: ").append(code).toString()));
    }

    private Optional<Element> findElementByCodeInGroup(String code, SchemeGroup schemeGroup) {
        if (code.equals(schemeGroup.getCode())) {
            return Optional.of(schemeGroup);
        }
        for (Element element : schemeGroup.getElements()) {
            if (code.equals(element.getCode())) {
                return Optional.of(element);
            } else if (element.getType().equals(TypeClass.GROUP)) {
                Optional<Element> e = findElementByCodeInGroup(code, (SchemeGroup) element);
                if (e.isPresent()) {
                    return e;
                }
            }
        }
        return Optional.empty();
    }

    public SchemeGroup build() {
        return root;
    }
}
