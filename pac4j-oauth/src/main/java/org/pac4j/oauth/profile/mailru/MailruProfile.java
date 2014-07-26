package org.pac4j.oauth.profile.mailru;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;

/**
 * Created by foreach on 25.07.14.
 */
public class MailruProfile extends OAuth20Profile {
    private static final long serialVersionUID = -426641063918180600L;

    @Override
    protected AttributesDefinition getAttributesDefinition() {
        return OAuthAttributesDefinitions.mailruDefinition;
    }

    public String getFirstName() {
        return (String) getAttribute(MailruAttributesDefinition.FIRST_NAME);
    }

    public String getLastName() {
        return (String) getAttribute(MailruAttributesDefinition.LAST_NAME);
    }

    public String getNick() {
        return (String) getAttribute(MailruAttributesDefinition.NICK);
    }

    public String getEmail() {
        return (String) getAttribute(MailruAttributesDefinition.EMAIL);
    }
}
