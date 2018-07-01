package com.cropaccounting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cropaccounting.config.SpringSecurityConfigUserList.User;

@EnableWebSecurity
@ComponentScan("com.cropaccounting")
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private SpringSecurityConfigUserList userDetail;

	@SuppressWarnings("rawtypes")
	private UserDetailsBuilder udb;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests().anyRequest().fullyAuthenticated().and().csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> abc = auth.inMemoryAuthentication()
				.passwordEncoder(passwordEncoder());

		for (int i = 0; i < userDetail.getUsers().size(); i++) {
			User user = userDetail.getUsers().get(i);
			if (i == 0) {
				udb = abc.withUser(user.getCode()).password(user.getPassword()).roles(user.getRoles());
			} else {
				udb = udb.and().withUser(user.getCode()).password(user.getPassword()).roles(user.getRoles());
			}
		}
	}

	/**
	 * passwords will be kept in the application.yml in encrypted format this method
	 * will return a encoder with password strength 10, to decrypt and match.
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
