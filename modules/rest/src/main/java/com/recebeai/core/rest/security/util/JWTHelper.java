package tech.jannotti.billing.core.rest.security.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTHelper {

    private static final String ISSUER = "jannotti.tech";

    public static String buildToken(BaseUser user, String secret, int expiration) {

        LocalDateTime issuedDate = DateTimeHelper.getNowDateTime();
        LocalDateTime expirationDate = LocalDateTime.now().plus(expiration, ChronoUnit.HOURS);

        JwtBuilder builder = Jwts.builder()
            .setIssuer(ISSUER)
            .setSubject(user.getUsername())
            .claim("role", user.getRole().getCode())
            .setIssuedAt(DateTimeHelper.toDate(issuedDate))
            .setExpiration(DateTimeHelper.toDate(expirationDate));

        return builder.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
    }

    public static Jws<Claims> parseToken(String token, String secret) {

        Jws<Claims> jws = null;
        try {
            jws = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);

        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Token expirado recebido", e);
        } catch (JwtException e) {
            throw new BadCredentialsException("Erro lendo token", e);
        }
        return jws;
    }

}
