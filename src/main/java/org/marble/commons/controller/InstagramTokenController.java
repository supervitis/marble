package org.marble.commons.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.marble.commons.dao.model.InstagramToken;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.exception.InvalidInstagramTokenException;
import org.marble.commons.service.InstagramTokenService;
import org.marble.commons.util.MarbleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import twitter4j.JSONObject;

@Controller
@RequestMapping("/admin/keys/instagram")
public class InstagramTokenController {

	

	private static final Logger log = LoggerFactory
			.getLogger(InstagramTokenController.class);
	@Autowired
	InstagramTokenService instagramTokenService;
	@Autowired
	Environment env;
	
	
	@RequestMapping
	public ModelAndView home(HttpServletRequest request) {
		System.out.println(env.getProperty("mongodb.hostname", "defaultValue"));
		String basePath = MarbleUtil.getBasePath(request);
		ModelAndView modelAndView = new ModelAndView("instagram_tokens_list");
		modelAndView.addObject("instagram_tokens",
				instagramTokenService.getInstagramTokens());
		modelAndView.addObject("client_id",
				env.getProperty("instagram.clientid", "4096dac9b3b541d0a732c53a429718bc"));
		modelAndView.addObject("redirect_uri", basePath
				+ "/admin/keys/instagram/generate");
		return modelAndView;
	}

	@RequestMapping(value = "/edit/{instagramTokenId}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Integer instagramTokenId)
			throws InvalidInstagramTokenException {
		ModelAndView modelAndView = new ModelAndView();

		InstagramToken instagramToken;
		instagramToken = instagramTokenService
				.getInstagramToken(instagramTokenId);
		modelAndView.setViewName("instagram_token_edit");
		modelAndView.addObject("instagram_token", instagramToken);
		return modelAndView;
	}

	@RequestMapping(value = "/edit/{instagramTokenId}", method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Integer instagramTokenId,
			@Valid InstagramToken instagramToken, BindingResult result)
			throws InvalidInstagramTokenException {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("instagram_token_edit");
		modelAndView.addObject("instagram_token", instagramToken);

		if (result.hasErrors()) {
			modelAndView.addObject("notificationMessage",
					"InstagramTokenController.editInstagramTokenError");
			modelAndView.addObject("notificationIcon",
					"fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			return modelAndView;
		}

		instagramTokenService.updateInstagramToken(instagramToken);

		modelAndView.addObject("notificationMessage",
				"InstagramTokenController.instagramTokenModified");
		modelAndView.addObject("notificationIcon", "fa-check-circle");
		modelAndView.addObject("notificationLevel", "success");

		// TODO Set list view as return

		return modelAndView;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() throws InvalidInstagramTokenException {
		ModelAndView modelAndView = new ModelAndView();
		InstagramToken instagramToken = new InstagramToken();
		modelAndView.setViewName("create_instagram_token");
		modelAndView.addObject("instagram_token", instagramToken);
		return modelAndView;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid InstagramToken instagramToken,
			BindingResult result, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws InvalidInstagramTokenException {

		String basePath = MarbleUtil.getBasePath(request);
		ModelAndView modelAndView = new ModelAndView();

		if (result.hasErrors()) {
			modelAndView.addObject("notificationMessage",
					"InstagramTokenController.addInstagramTokenError");
			modelAndView.addObject("notificationIcon",
					"fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			modelAndView.setViewName("create_instagram_token");
			modelAndView.addObject("instagram_token", instagramToken);
			return modelAndView;
		}

		instagramToken = instagramTokenService
				.createInstagramToken(instagramToken);
		// Setting message
		redirectAttributes.addFlashAttribute("notificationMessage",
				"InstagramTokenController.instagramTokenCreated");
		redirectAttributes.addFlashAttribute("notificationIcon",
				"fa-check-circle");
		redirectAttributes.addFlashAttribute("notificationLevel", "success");
		modelAndView.setViewName("redirect:" + basePath
				+ "/admin/keys/instagram");
		return modelAndView;
	}
	

	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public ModelAndView generate(@Valid InstagramToken instagramToken,
			BindingResult result, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws InvalidInstagramTokenException {

		String basePath = MarbleUtil.getBasePath(request);
		ModelAndView modelAndView = new ModelAndView();

		if (result.hasErrors()) {
			modelAndView.addObject("notificationMessage",
					"InstagramTokenController.addInstagramTokenError");
			modelAndView.addObject("notificationIcon",
					"fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			modelAndView.setViewName("create_instagram_token");
			modelAndView.addObject("instagram_token", instagramToken);
			return modelAndView;
		}

		instagramToken = instagramTokenService
				.createInstagramToken(instagramToken);
		// Setting message
		redirectAttributes.addFlashAttribute("notificationMessage",
				"InstagramTokenController.instagramTokenCreated");
		redirectAttributes.addFlashAttribute("notificationIcon",
				"fa-check-circle");
		redirectAttributes.addFlashAttribute("notificationLevel", "success");
		modelAndView.setViewName("redirect:" + basePath
				+ "/admin/keys/instagram");
		return modelAndView;
	}

	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public ModelAndView generateInstagramToken(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView();
		String basePath = MarbleUtil.getBasePath(request);
		String redirectUrl = basePath + "/admin/keys/instagram/generate";
		String code = request.getParameter("code");
		
		if(code == null){
			modelAndView.addObject("notificationMessage","InstagramTokenController.generateInstagramTokenError");
			modelAndView.addObject("notificationIcon",
					"fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			modelAndView.setViewName("create_instagram_token");
			InstagramToken instagramToken = new InstagramToken();
			modelAndView.addObject("instagram_token", instagramToken);
			return modelAndView;
		}
		
		log.info(code);
		log.info(request.getRequestURL().toString());
		String jsonToken = "";
		JSONObject json = null;
		String token = "";
		try {
			jsonToken = convertInstagramCodeToToken(code,redirectUrl);
			json = new JSONObject(jsonToken);
			System.out.println(jsonToken);
			token = json.getString("access_token");

			System.out.println(token);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			modelAndView.addObject("notificationMessage","InstagramTokenController.generateInstagramTokenError");
			modelAndView.addObject("notificationIcon",
					"fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			modelAndView.setViewName("create_instagram_token");
			InstagramToken instagramToken = new InstagramToken();
			modelAndView.addObject("instagram_token", instagramToken);
			return modelAndView;
		}

		
		InstagramToken instagramToken = new InstagramToken();
		instagramToken.setAccessToken(token);
		modelAndView.setViewName("create_instagram_token");
		modelAndView.addObject("instagram_token", instagramToken);
		return modelAndView;

	}

	private String convertInstagramCodeToToken(String code,String redirectionUrl) throws IOException {
		String url = "https://api.instagram.com/oauth/access_token";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");

		String urlParameters = "client_id="
				+ env.getProperty("instagram.clientid","4096dac9b3b541d0a732c53a429718bc") + "&client_secret="
				+ env.getProperty("instagram.clientsecret","25d42660042442d0a1dfee86df468368")
				+ "&grant_type=authorization_code&redirect_uri=" + redirectionUrl + "&code=" + code;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = null;
		String inputLine;
		StringBuffer response = new StringBuffer();

		in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		// print result
		return response.toString();

	}

	@RequestMapping(value = "/delete/{instagramTokenId}")
	public String delete(@PathVariable Integer instagramTokenId,
			RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws InvalidInstagramTokenException {
		String basePath = MarbleUtil.getBasePath(request);
		instagramTokenService.deleteInstagramToken(instagramTokenId);
		// Setting message
		redirectAttributes.addFlashAttribute("notificationMessage",
				"InstagramTokenController.instagramTokenDeleted");
		redirectAttributes.addFlashAttribute("notificationIcon",
				"fa-check-circle");
		redirectAttributes.addFlashAttribute("notificationLevel", "success");
		return "redirect:" + basePath + "/admin/keys/instagram";
	}

}