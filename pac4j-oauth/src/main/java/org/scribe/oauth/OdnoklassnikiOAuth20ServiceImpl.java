package org.scribe.oauth;

import java.util.SortedMap;
import java.util.TreeMap;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;

import java.util.Map;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Odnoklassniki.ru {@link OAuth20ServiceImpl) customization
 */
public class OdnoklassnikiOAuth20ServiceImpl extends OAuth20ServiceImpl {

    /**
     * Default constructor
     *
     * @param api    OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public OdnoklassnikiOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config, final String publicKey) {
        super(api, config);
        //
        if (null == api) {
            throw new IllegalArgumentException("api");
        }
        if (null == config) {
            throw new IllegalArgumentException("config");
        }
        if (null == publicKey) {
            throw new IllegalArgumentException("publicKey");
        }
        //
        this.api = api;
        this.config = config;
        this.publicKey = publicKey;
    }

    private DefaultApi20 api;
    private OAuthConfig config;
    private String publicKey;

    /**
     * Additional public api key, need for API invocation (used to calculate signature in requestSign method).
     * @return
     */
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        if (null == publicKey || publicKey.isEmpty()) {
            throw new IllegalArgumentException("publicKey");
        }
        //
        this.publicKey = publicKey;
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQuerystringParameter("grant_type", "authorization_code");
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        //
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    // sig = md5( request_params_composed_string + md5(access_token + application_secret_key)  )
    private String calculateSig(ParameterList parametersList, String accessTokenValue, String apiSecret) {
        String accessTokenPlusSecretMd5 = md5Hex(accessTokenValue + apiSecret);
        // Sort parameters alphabetically before performing a sign
        parametersList = parametersList.sort();

        String urlPart = parametersList.asFormUrlEncodedString();
        Map<String, String> map = new TreeMap<String, String>();
        for (String param : urlPart.split("&")) {
            String[] parts = param.split("=");
            map.put(parts[0], (parts.length == 1) ? "" : parts[1]);
        }
        StringBuilder urlNew = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            urlNew.append(entry.getKey());
            urlNew.append('=');
            urlNew.append(entry.getValue());
        }

        return md5Hex(urlNew.toString() + accessTokenPlusSecretMd5);
    }

    @Override
    public void signRequest(Token accessToken, OAuthRequest request) {

        if (null == publicKey || publicKey.isEmpty()) {
            throw new IllegalStateException("publicKey must be initialized before signing requests.");
        }

        request.addQuerystringParameter("application_key", this.publicKey);
        //
        String sig = calculateSig(request.getQueryStringParams(), accessToken.getToken(), this.config.getApiSecret());
        request.addQuerystringParameter("sig", sig);
        request.addQuerystringParameter("access_token", accessToken.getToken());
    }
}