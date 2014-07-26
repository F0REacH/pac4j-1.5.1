package org.pac4j.oauth.profile.odnoklassniki;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.converter.Converters;

/**
 * Created by foreach on 26.07.14.
 */
// TODO add attributes @see http://apiok.ru/wiki/display/api/users.getInfo
public class OdnoklassnikiAttributesDefinition extends AttributesDefinition {
    public static final String NAME = "name";

    public OdnoklassnikiAttributesDefinition() {
        addAttribute(NAME, Converters.stringConverter);
    }
}
