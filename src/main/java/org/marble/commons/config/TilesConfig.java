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
		addBaseDefinition("home", "Home", Boolean.FALSE);
		addBaseDefinition("login", "Login", Boolean.FALSE);
		addBaseDefinition("admin", "Administration Panel", Boolean.FALSE);

		addBaseDefinition("edit_topic", "Edit Topic", Boolean.TRUE);

		return tilesConfigurer;
	}

	private void addBaseDefinition(String name, String title, Boolean script) {
		Map<String, Attribute> attributes = getDefaultAttributes();

		attributes.put("title", new Attribute("Marble - " + title));
		attributes.put("content", new Attribute("/WEB-INF/views/" + name + ".jsp"));

		if (script) {
			attributes.put("script", new Attribute("/WEB-INF/views/scripts/" + name + ".jsp"));
		}
		else {
			attributes.put("script", new Attribute(""));
		}

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
}
