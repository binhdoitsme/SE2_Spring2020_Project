package com.hanu.util.authentication;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.hanu.domain.model.User;
import com.hanu.domain.repository.UserRepository;
import com.hanu.util.configuration.Configuration;
import com.hanu.util.di.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Use JWT to provide authentication
 */
public class Authenticator {
    private static final String ISSUER = Configuration.get("auth.issuer");
    private static final int TIMEOUT = new Integer(Configuration.get("auth.timeout.minutes"));
    private static final String SECRET_KEY = Configuration.get("auth.secret");
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);

    private static Set<String> invalidatedTokens = new HashSet<>();

    @Inject
    private UserRepository repository;

    public Authenticator() { }

    /**
     * Authenticate the user based on username and password
     * 
     * @return JWT token for the user
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public String authenticate(String username, String password) {
        // use repository to check whether the tuplet is correct
        User expectedUser = repository.getById(username);
        if (expectedUser == null) {
            return new String();
        }

        // check password
        if (!checkPassword(password, expectedUser.getSalt(), expectedUser.getPassword())) {
            return new String();
        }

        // if authenticated, create jwt
        return createJwt(username);
    }

    /**
     * Hash password+salt using bcrypt
     * 
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String hashPassword(String unhashed, String salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassOne = digest.digest(unhashed.getBytes("UTF-8"));
        byte[] hashedSalt = digest.digest(salt.getBytes("UTF-8"));
        byte[] joined = joinArrays(hashedPassOne, hashedSalt);
        byte[] hashedPassTwo = digest.digest(joined);
        return bytesToHex(hashedPassTwo);
    }

    private boolean checkPassword(String unhashed, String salt, String expected) {
        try {
            return expected.equals(hashPassword(unhashed, salt));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    private static byte[] joinArrays(byte[] a, byte[] b) {
        int aLength = a.length;
        int bLength = b.length;
        byte[] joined = new byte[aLength + bLength];
        for (int i = 0; i < aLength; i++) {
            joined[i] = a[i];
        }
        for (int i = 0; i < bLength; i++) {
            joined[aLength + i] = b[i];
        }
        return joined;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Create JSON Web Token using JJWT
     */
    private String createJwt(String username) {
        Date now = new Date();
        Instant expiration = Instant.ofEpochSecond(now.toInstant()
                                                    .getEpochSecond() + TIMEOUT * 60);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());
        return Jwts.builder()
                    .setIssuer(ISSUER)
                    .setIssuedAt(now)
                    .setSubject(username)
                    .claim("username", username)
                    .setExpiration(Date.from(expiration))
                    .signWith(
                        SignatureAlgorithm.HS256,
                        signingKey
                    )
                    .compact();
    }

    private boolean tokenIsBlacklisted(String jwt) {
        return invalidatedTokens.contains(jwt);
    }

    /**
     * Check if the token is valid
     */
    public boolean validateJwt(String jwt) {
        if (tokenIsBlacklisted(jwt)) {
            return false;
        }
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                                .parseClaimsJws(jwt)
                                .getBody();
            String username = claims.get("username").toString();
            Date expiration = claims.getExpiration();
            return validateUsername(username) && validateExpiration(expiration);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateUsername(String username) {
        return true;
    }

    private boolean validateExpiration(Date expiration) {
        return expiration.after(new Date());
    }

    /**
     * Add expired or logged out tokens to invalidated tokens
     */
    public static void invalidateJwt(String jwt) {
        invalidatedTokens.add(jwt);
    }
}