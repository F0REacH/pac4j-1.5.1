package org.pac4j.oauth.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.OAuthAttributesDefinitions;
import org.pac4j.oauth.profile.odnoklassniki.OdnoklassnikiProfile;
import org.scribe.builder.api.OdnoklassnikiApi;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.oauth.OdnoklassnikiOAuth20ServiceImpl;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Created by foreach on 26.07.14.
 */
public class OdnoklassnikiClient extends BaseOAuth20Client<OdnoklassnikiProfile> {


    protected String publicKey = "";

    public OdnoklassnikiClient() {
    }

    public OdnoklassnikiClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected OdnoklassnikiClient newClient() {
        OdnoklassnikiClient client = new OdnoklassnikiClient();
        client.setPublicKey(this.publicKey);
        return client;
    }

    @Override
    protected void internalInit() {
        super.internalInit();
        this.service = new OdnoklassnikiOAuth20ServiceImpl(new OdnoklassnikiApi(),
                new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, null, null), this.getPublicKey());
    }

    @Override
    protected String getProfileUrl(final Token accessToken) {

        String params = "application_key="+this.getPublicKey()+"method=users.getCurrentUser";
        String accessTknAndSecretKeyRaw = accessToken.getToken()+this.getSecret();
        String sigRaw = params+md5Hex(accessTknAndSecretKeyRaw);
        String sig= md5Hex(sigRaw);
        String url= "http://api.odnoklassniki.ru/fb.do?method=users.getCurrentUser&access_token="+accessToken.getToken()+"&application_key="+this.getPublicKey()+"&sig="+sig;

        return url;
    }

    @Override
    protected OdnoklassnikiProfile extractUserProfile(final String body) {

        final OdnoklassnikiProfile profile = new OdnoklassnikiProfile();
        final JsonNode json = JsonHelper.getFirstNode(body);

        if (json != null) {
            profile.setId(JsonHelper.get(json, "uid"));
            for (final String attribute : OAuthAttributesDefinitions.odnoklassnikiDefinition.getPrincipalAttributes()) {
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

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(final String pk) {
        this.publicKey = pk;
    }
}
