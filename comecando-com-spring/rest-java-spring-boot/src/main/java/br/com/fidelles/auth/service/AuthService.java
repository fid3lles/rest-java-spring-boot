package br.com.fidelles.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fidelles.security.jwt.JwtTokenProvider;
import br.com.fidelles.security.vo.v1.AccountCredentialsVO;
import br.com.fidelles.security.vo.v1.TokenVO;
import br.com.fidelles.user.repository.UserRepository;

@Service
public class AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserRepository repository;
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity signin(AccountCredentialsVO data) {
		try {
			var username = data.getUsername();
			var password = data.getPassword();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			var user = repository.findByUsername(username);
			
			var tokenReponse = new TokenVO();
			
			if (user != null) {
				tokenReponse = tokenProvider.createAccessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Username " + username + " not found!");
			}
			
			return ResponseEntity.ok(tokenReponse);
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password supplied!");
		}
	}
	
}
