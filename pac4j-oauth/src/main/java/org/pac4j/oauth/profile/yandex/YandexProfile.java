package org.pac4j.oauth.profile.yandex;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.core.profile.Gender;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;

/**
 * Created by foreach on 25.07.14.
 */
public class YandexProfile extends OAuth20Profile {


    private static final long serialVersionUID = 2989401982013525860L;

    @Override
    protected AttributesDefinition getAttributesDefinition() {
        return OAuthAttributesDefinitions.yandexDefinition;
    }

    @Override
    public Gender getGender() {
        final Gender gender = (Gender) getAttribute(YandexAttributesDefinition.SEX);
        if (gender == null) {
            return Gender.UNSPECIFIED;
        } else {
            return gender;
        }
    }

    public String getRealName() {
        return (String) getAttribute(YandexAttributesDefinition.REAL_NAME);
    }

    public String getDefaultEmail() {
        return (String) getAttribute(YandexAttributesDefinition.DEFAULT_EMAIL);
    }

}
