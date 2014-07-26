package org.pac4j.oauth.profile.mailru;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.converter.Converters;

/**
 * Created by foreach on 25.07.14.
 */
// TODO add more attributes @see http://api.mail.ru/docs/reference/rest/users-getinfo/
public class MailruAttributesDefinition extends AttributesDefinition {

    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String NICK = "nick";
    public static final String EMAIL = "email";

    public MailruAttributesDefinition() {
        addAttribute(FIRST_NAME, Converters.stringConverter);
        addAttribute(LAST_NAME, Converters.stringConverter);
        addAttribute(NICK, Converters.stringConverter);
        addAttribute(EMAIL, Converters.stringConverter);
    }
}
