package org.pac4j.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;
import org.pac4j.oauth.profile.mailru.MailruProfile;
import org.scribe.builder.api.MailruApi;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.oauth.MailruOAuth20ServiceImpl;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;


/**
 * Created by foreach on 25.07.14.
 */
public class MailruClient extends BaseOAuth20Client<MailruProfile> {

    public MailruClient() {
    }

    public MailruClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected MailruClient newClient() {
        MailruClient client = new MailruClient();
        return client;
    }

    @Override
    protected void internalInit() {
        super.internalInit();
        this.service = new MailruOAuth20ServiceImpl(new MailruApi(),
                new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, null, null));
    }


    @Override
    protected String getProfileUrl(final Token accessToken) {

        String params = "app_id="+this.getKey()+"method=users.getInfosecure=1session_key="+accessToken.getToken();
        String sigRaw = params+this.getSecret();
        String sig = md5Hex(sigRaw);
        String url = "http://www.appsmail.ru/platform/api?method=users.getInfo&app_id="+this.getKey()+"&session_key="+accessToken.getToken()+"&secure=1&sig="+sig;

        return url;
    }

    @Override
    protected MailruProfile extractUserProfile(final String body) {
        final MailruProfile profile = new MailruProfile();
        final JsonNode json = JsonHelper.getFirstNode(body).get(0);

        if (json != null) {
            profile.setId(JsonHelper.get(json, "uid"));
            for (final String attribute : OAuthAttributesDefinitions.mailruDefinition.getPrincipalAttributes()) {
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
