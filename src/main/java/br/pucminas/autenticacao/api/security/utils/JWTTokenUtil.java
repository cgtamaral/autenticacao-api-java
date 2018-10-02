package br.pucminas.autenticacao.api.security.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.pucminas.autenticacao.api.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTTokenUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;
	
	static final String CLAIM_KEY_ROLE = "role";

	public String generateToken(User user)
	{
		//The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);

	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put(CLAIM_KEY_ROLE, user.getUserProfile().toString());
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setClaims(claims)
	    							.setId(user.getId().toString())
	                                .setIssuedAt(now)
	                                .setSubject(user.getEmail())
	                                //.setIssuer(issuer)
	                                .signWith(signatureAlgorithm, signingKey)
	        						.setExpiration(genarateExpirationDate());

	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	
	/**
	 * Retorna a data de expiração com base na data atual.
	 * 
	 * @return Date
	 */
	private Date genarateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	/**
	 * Realiza o parse do token JWT para extrair as informações contidas no
	 * corpo dele.
	 * 
	 * @param token
	 * @return Claims
	 */
	private Claims getClaimsFromToken(String token) 
	{
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	
		return claims;
	}

	public void validateToken(String jwtToken, String userEmail) throws Exception 
	{
		Claims claims = getClaimsFromToken(jwtToken);
		if (claims.getExpiration().getTime() < System.currentTimeMillis()) 
		{
			throw new Exception("Token expirado!");
		}
		if (!claims.getSubject().equals(userEmail)) 
		{
			throw new Exception("Informações do Token não conferem com o usuário informado!");
		}
	}
}
