package com.invoicgenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class CustomHttpRequest {

	private static Logger log = Logger.getLogger(CustomHttpRequest.class.getName()); 
	
	private static ObjectMapper mapper=new ObjectMapper();
	private String contentType="multipart/form-data";
	
	public static String generatePostRequest(String url, Map<String,Object> parameterMap) throws JsonGenerationException, JsonMappingException, IOException{
		String resp = null;
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		HTTPRequest request;
		try {
			request = new HTTPRequest(new URL(url), HTTPMethod.POST, FetchOptions.Builder.withDeadline(60));
			ByteArrayOutputStream os=new ByteArrayOutputStream();
			mapper.writeValue(os, parameterMap);
			request.setPayload(os.toByteArray());
			request.setHeader(new HTTPHeader("Content-type","application/json"));
			HTTPResponse response=fetcher.fetch(request);

			log.info("Response code -> "+response.getResponseCode());
			if(response.getResponseCode()!=200)
				throw new IOException("The post response did not have status code 200");
			resp = new String(response.getContent());
			log.info("PostData returned : "+resp);
			

		} catch (MalformedURLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return resp;
	}
	
	public static String generatePostRequest(String url){
		String resp = null;
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		HTTPRequest request;
		try {
			request = new HTTPRequest(new URL(url), HTTPMethod.POST, FetchOptions.Builder.withDeadline(60));
			request.setHeader(new HTTPHeader("Content-type","text/html"));
			HTTPResponse response=fetcher.fetch(request);

			log.info("Response code -> "+response.getResponseCode());
			if(response.getResponseCode()!=200)
				throw new IOException("The post response did not have status code 200");
			resp = new String(response.getContent());
			log.info("PostData returned : "+resp);
			
		} catch (MalformedURLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
			return resp;
	}
	
	public static byte[] generateGetRequest(String url){
		try {
			URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
			HTTPRequest request=new HTTPRequest(new URL(url),HTTPMethod.GET , FetchOptions.Builder.withDeadline(30));
			HTTPResponse response=fetcher.fetch(request);
			if(response.getResponseCode()!=200)
				throw new Exception("Could not fetch the content at "+new URL(url).toExternalForm());
			return response.getContent();

		} catch (MalformedURLException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
	

	public static void redirectToUrl(HttpServletResponse response,String url) {
		try {
			response.sendRedirect(url);
		} catch(IOException e) {
			log.log(Level.SEVERE,e.getMessage(),e);
		}
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
