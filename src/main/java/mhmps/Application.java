package mhmps;

import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableSalespoint
public class Application {
	
	private static final String LOGIN_ROUTE = "/login";


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Configuration
	static class VideoShopWebConfiguration implements WebMvcConfigurer {

		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController(LOGIN_ROUTE).setViewName("login");
			registry.addViewController("/").setViewName("index");
		}
	}

	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();  // for lab purposes, that's ok!
			http.authorizeRequests().antMatchers("/**").permitAll().and()
					.formLogin().loginPage(LOGIN_ROUTE).loginProcessingUrl(LOGIN_ROUTE).and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/");
			http.authorizeRequests().antMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated();
        http.headers().frameOptions().sameOrigin();
		}
	}
}