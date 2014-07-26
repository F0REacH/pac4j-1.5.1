package org.pac4j.oauth.profile.yandex;

import org.pac4j.core.profile.converter.Converters;
import org.pac4j.oauth.profile.OAuthAttributesDefinition;

/**
 * Created by foreach on 25.07.14.
 */
// TODO add more attributes @see http://api.yandex.ru/login/doc/dg/reference/response.xml

public class YandexAttributesDefinition extends OAuthAttributesDefinition {
    public static final String SEX = "sex";
    public static final String REAL_NAME = "real_name";
    public static final String DEFAULT_EMAIL = "default_email";

    public YandexAttributesDefinition() {
        addAttribute(SEX, Converters.genderConverter);
        addAttribute(REAL_NAME, Converters.stringConverter);
        addAttribute(DEFAULT_EMAIL, Converters.stringConverter);
    }
}
