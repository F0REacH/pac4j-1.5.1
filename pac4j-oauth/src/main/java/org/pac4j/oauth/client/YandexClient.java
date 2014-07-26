package org.pac4j.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;
import org.pac4j.oauth.profile.yandex.YandexProfile;
import org.scribe.builder.api.YandexApi;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.oauth.YandexOAuth20ServiceImpl;

/**
 * Created by foreach on 25.07.14.
 */
public class YandexClient extends BaseOAuth20Client<YandexProfile> {

    public YandexClient() {
    }

    public YandexClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected YandexClient newClient() {
        YandexClient client = new YandexClient();
        return client;
    }

    @Override
    protected void internalInit() {
        super.internalInit();
        this.service = new YandexOAuth20ServiceImpl(new YandexApi(),
                new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, null, null));
    }

    @Override
    protected String getProfileUrl(final Token accessToken) {
        return "https://login.yandex.ru/info?format=json&oauth_token="+accessToken.getToken();
    }

    @Override
    protected YandexProfile extractUserProfile(final String body) {
        final YandexProfile profile = new YandexProfile();
        final JsonNode json = JsonHelper.getFirstNode(body);

        if (json != null) {
            profile.setId(JsonHelper.get(json, "id"));
            for (final String attribute : OAuthAttributesDefinitions.yandexDefinition.getPrincipalAttributes()) {
                profile.addAttribute(attribute, JsonHelper.get(json, attribute));
            }
        }
        return profile;
    }

    @Override
    protected boolean requiresStateParameter() {
        return false;
    }

    @Override
    protected boolean hasBeenCancelled(WebContext context) {
        return false;
    }



}
