package org.marble.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
@PropertySource(value = "classpath:auth.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	Environment env;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// TODO MFC Move the user database somewhere else (DB, LDAP or anything more flexible)
		auth.inMemoryAuthentication().withUser(env.getProperty("access.admin.username")).password(env.getProperty("access.admin.password"))
				.roles("ADMIN");
		auth.inMemoryAuthentication().withUser(env.getProperty("access.oper.username")).password(env.getProperty("access.oper.password"))
				.roles("OPER");
		auth.inMemoryAuthentication().withUser(env.getProperty("access.guest.username")).password(env.getProperty("access.guest.password"))
				.roles("GUEST");
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
					.exceptionHandling()
					.accessDeniedPage("/login/promote")
				.and()
					.logout()
					.deleteCookies("JSESSIONID")
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")

				// Configures url based authorization
				.and()
					.authorizeRequests()
					    // Urls open to the public
                        .antMatchers("/login").permitAll()
                        .antMatchers("/").permitAll()
                        // Urls reserved to Admin and Operators
                        .antMatchers("/topic/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPER')")
                        .antMatchers("/execution/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPER')")
                        .antMatchers("/rest/execution/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_OPER')")
                        // Urls reserved to Admin
						.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
						.antMatchers("/rest/**").access("hasRole('ROLE_ADMIN')")
						
						
				;
	}

}