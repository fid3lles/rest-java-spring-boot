package br.com.fidelles.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenFilter extends GenericFilterBean {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	public JwtTokenFilter(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String token = tokenProvider.resolveToken((HttpServletRequest) req);
		
		if (token != null && tokenProvider.validateToken(token)) {
			Authentication auth = tokenProvider.getAuthentication(token);
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		chain.doFilter(req, res);
	}

}
