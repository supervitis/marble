package org.marble.commons.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.UnresolvingLocaleDefinitionsFactory;
import org.apache.tiles.request.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

@EnableWebMvc
@ComponentScan(basePackages = { "org.marble.commons" })
@Import(DbConfig.class)
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

	private static final Map<String, Definition> tiles = new HashMap<String, Definition>();

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public InternalResourceViewResolver getInternalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(TilesView.class);
		viewResolver.setOrder(-2);
		return viewResolver;
	}

	/* Apache Tiles Configuration */
	@Bean
	public TilesConfigurer tilesConfigurer() {

		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitionsFactoryClass(JavaDefinitionsFactory.class);
		tilesConfigurer.setDefinitions(new String[] {});

		addBaseDefinition("home", "Home", "/WEB-INF/views/home.jsp");
		addBaseDefinition("login", "Login", "/WEB-INF/views/login.jsp");

		return tilesConfigurer;
	}

	private void addBaseDefinition(String name, String title, String content) {
		Map<String, Attribute> attributes = getDefaultAttributes();

		attributes.put("title", new Attribute("Marble - " + title));
		attributes.put("content", new Attribute(content));

		Attribute layout = new Attribute("/WEB-INF/views/layouts/baseLayout.jsp");
		tiles.put(name, new Definition(name, layout, attributes));
	}

	private Map<String, Attribute> getDefaultAttributes() {
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();

		attributes.put("topNavigation", new Attribute("/WEB-INF/views/layouts/topNavigation.jsp"));
		attributes.put("leftNavigation", new Attribute("/WEB-INF/views/layouts/leftNavigation.jsp"));
		attributes.put("author", new Attribute("Miguel Fernandes"));

		return attributes;
	}

	public static class JavaDefinitionsFactory extends UnresolvingLocaleDefinitionsFactory {

		public JavaDefinitionsFactory() {
		}

		@Override
		public Definition getDefinition(String name, Request tilesContext) {
			return tiles.get(name);
		}
	}
	
	/* I18n */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		Locale defaultLocale = new Locale("en");
		sessionLocaleResolver.setDefaultLocale(defaultLocale);
		return sessionLocaleResolver;
	}
	
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return messageSource;
	}
}
