package politechnika.lodzka.qrcode.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Language {
    PL("pl"),
    EN("en");

    private final String lang;

    public static Language fromString(final String value) {
        return Arrays.stream(Language.values()).filter(v -> v.lang.equals(value)).findFirst().orElse(EN);
    }
}
