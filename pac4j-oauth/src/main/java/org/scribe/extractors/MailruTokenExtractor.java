package org.scribe.extractors;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link AccessTokenExtractor} for Mail.ru OAuth API
 *
 */
public class MailruTokenExtractor extends TokenExtractor20Impl
{

    private final Pattern accessTokenPattern = Pattern.compile("\\{(?:\\s|.)*?\"access_token\"\\s*:\\s*\"([^\"]*)\"(?:\\s|.)*\\}");

    public Token extract(final String response) {
        Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
        final Matcher matcher = this.accessTokenPattern.matcher(response);
        if (matcher.find()) {
            return new Token(matcher.group(1), "", response);
        } else {
            throw new OAuthException("Cannot extract an acces token. Response was: " + response);
        }
    }
}