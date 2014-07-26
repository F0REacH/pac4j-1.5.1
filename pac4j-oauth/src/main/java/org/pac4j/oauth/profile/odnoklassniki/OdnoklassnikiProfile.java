package org.pac4j.oauth.profile.odnoklassniki;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;

/**
 * Created by foreach on 26.07.14.
 */
public class OdnoklassnikiProfile extends OAuth20Profile {

    private static final long serialVersionUID = 2838108550315702327L;

    @Override
    protected AttributesDefinition getAttributesDefinition() {
        return OAuthAttributesDefinitions.odnoklassnikiDefinition;
    }

    public String getName() {
        return (String) getAttribute(OdnoklassnikiAttributesDefinition.NAME);
    }

}
