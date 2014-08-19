package org.marble.commons.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.UnresolvingLocaleDefinitionsFactory;
import org.apache.tiles.request.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

@Configuration
public class TilesConfig extends WebMvcConfigurerAdapter {

	private static final Map<String, Definition> tiles = new HashMap<String, Definition>();

	/* Apache Tiles Configuration */
	@Bean
	public TilesConfigurer tilesConfigurer() {

		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitionsFactoryClass(JavaDefinitionsFactory.class);
		tilesConfigurer.setDefinitions(new String[] {});

		// Here is where the views are defined
		addBaseDefinition("home", "Home", "<i class='fa fa-home fa-fw'></i>", Boolean.FALSE);
		addBaseDefinition("login", "Login", Boolean.FALSE);
		addBaseDefinition("error", "Oops!", Boolean.FALSE);
		addBaseDefinition("not_found", "Not found!", Boolean.FALSE);
		
		
		// Just for test
		//addBaseDefinition("signup", "Test", Boolean.FALSE);
		addBaseDefinition("admin", "Administration Panel", "<i class='fa fa-cog fa-fw'></i>", Boolean.FALSE);
		addBaseDefinition("edit_topic", "Edit Topic", "<i class='fa fa-pencil fa-fw'></i>" , Boolean.TRUE);
		addBaseDefinition("create_topic", "edit_topic", "Create New Topic", "<i class='fa fa-file-o fa-fw'></i>", Boolean.TRUE);
		addBaseDefinition("topics_list", "Topics", "<i class='fa fa-tags fa-fw'></i>", Boolean.FALSE);

		return tilesConfigurer;
	}

	private void addBaseDefinition(String name, String view, String title, String decoration, Boolean script) {
		Map<String, Attribute> attributes = getDefaultAttributes();

		attributes.put("title", new Attribute(title));
		attributes.put("content", new Attribute("/WEB-INF/views/" + view + ".jsp"));
		attributes.put("decoration", new Attribute(decoration));

		if (script) {
			attributes.put("script", new Attribute("/WEB-INF/views/scripts/" + view + ".jsp"));
		}
		else {
			attributes.put("script", new Attribute(""));
		}

		Attribute layout = new Attribute("/WEB-INF/views/layouts/baseLayout.jsp");
		tiles.put(name, new Definition(name, layout, attributes));
	}
	
	private void addBaseDefinition(String name, String title, String decoration, Boolean script) {
		addBaseDefinition(name, name, title, decoration, script);
	}
	
	private void addBaseDefinition(String name, String title, Boolean script) {
		addBaseDefinition(name, name, title, "", script);
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
}
