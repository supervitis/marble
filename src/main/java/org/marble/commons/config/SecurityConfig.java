package org.marble.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("marble").password("marble").roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				// Configures form login
				.formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/login")
				.loginProcessingUrl("/login/authenticate")
				.failureUrl("/login/failure")
				// Configures the logout function
				.and()
				.logout()
				.deleteCookies("JSESSIONID")
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				// Configures url based authorization
				.and()
				.authorizeRequests()
				// Anyone can access the urls
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
				.antMatchers("/dba/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
				.antMatchers("/login").permitAll()
				.antMatchers("/topic").permitAll()

		;
	}
}