package com.pizza.ui.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.util.FileCopyUtils;

import com.pizza.domain.PizzaSize;
import com.pizza.domain.Topping;
import com.pizza.exception.InvalidMediaException;
import com.pizza.util.PresentationUtils;

public class ControllerUtils {

	
	private static final String TOPPINGS = "toppings";
	private static final String SIZES = "sizes";
	private static final String RESULTS_COUNT = "resultsCount";

	/**
	 * Look for the context map inside the given model under the key 'context', if one doesn't exist
	 * then create one.  Then add the given object to it using the given key.
	 * @param model The model that contains the context map.
	 * @param key The key of the object being added to the context.
	 * @param value The value to be added to the context.
	 */
	@SuppressWarnings({"unchecked", "rawtypes" })
	public static void addToContext(final Map model, final String key, final Object value) {
		Map context = (Map)model.get("context");
		if (context == null) {
			context = new HashMap();
			model.put("context", context);
		}
		context.put(key, value);
	}
	
	
	/**
	 * Efficiently stream a blob to the response.
	 * @param blob  the blob
	 * @param contentType  the content type
	 * @param response  the response
	 * @throws InvalidMediaException an error occurred transferring the blob
	 */
	public static void writeBlob(final Blob blob, final String contentType, final HttpServletResponse response)
			throws InvalidMediaException {
		if (blob == null) {
			return;
		}

		try {
			response.setContentType(contentType);
			FileCopyUtils.copy(blob.getBinaryStream(), response.getOutputStream());
			response.flushBuffer();
		}
		catch (final SQLException e) {
			throw new InvalidMediaException("Error getting media from the database.", e);
		}
		catch (final IOException e) {
			throw new InvalidMediaException(e);
		}
	}
	
	
	public static void sendJson(HttpServletResponse httpServletResponse, JSON json) throws IOException {
		httpServletResponse.setContentType("text/plain;charset=utf-8");
		String cnt = json.toString();
		httpServletResponse.getWriter().write(cnt);
		httpServletResponse.getWriter().flush();
	}
	
	public static JSON buildToppingsJson(Set<Topping> tops) {
		JSONArray topsArray = new JSONArray();
		for(Topping top: tops) {
			JSONObject toppingJson = new JSONObject()
									.element("id", top.getId())
									.element("toppingName", top.getName())
									.element("type", top.getType())
									.element("imageUrl", PresentationUtils.buildToppingImageUrl(top))
									.element("active", top.getActive());
			topsArray.element(toppingJson);
		}
		
		JSON results = new JSONObject()
		.element(RESULTS_COUNT, topsArray == null ? 0 : topsArray.size())
		.element(TOPPINGS, topsArray);
	
		return results;
		
	}
	
	public static JSON buildSizesJson(List<PizzaSize> sizes) {
		JSONArray sizesArray = new JSONArray();
//		sizesArray.element(new JSONObject()
//		.element("id"," ").element("sizeName", "-Pizza Size-"));
		for(PizzaSize size: sizes) {
			JSONObject sizeJson = new JSONObject()
									.element("id", size.getId())
									.element("sizeName", size.getName())
									.element("active", size.getActive());
			sizesArray.element(sizeJson);
		}
		JSON results = new JSONObject()
		.element(RESULTS_COUNT, sizesArray == null ? 0 : sizesArray.size())
		.element(SIZES, sizesArray);
		return results;
	}
	
	/**
	 * Wrap a JSON object in an HTML <code>textarea</code> node.  This is necessary for writing JSON
	 * when using certain UI transports like Dojo's <code>dojo.io.frame.send</code> module for file upload.
	 * Includes JS hijacking prevention measures.
	 * @param response the response
	 * @param json the JSON object or array
	 * @throws IOException error writing to response
	 */
	public static void writeInTextarea(final HttpServletResponse response, final JSON json) throws IOException {
		write(response, "<textarea>" + "{}&&" + json.toString() + "</textarea>", true);
	}

	
	/**
	 * Write a JSON object indicating that the operation was successful.
	 * The JSON will look like: <code>{ status: 0 }</code>.
	 * @param response the response to write to.
	 * @throws IOException if an error occurs writing the response.
	 */
	public static void writeSuccess(final HttpServletResponse response)
			throws IOException {
		write(response, "{ status: 0 }");
	}
	
	/**
	 * Write JSON to the response.
	 * @param response response object
	 * @param json json object
	 * @throws IOException if an error occurs
	 */
	public static void write(final HttpServletResponse response, final JSON json)
		throws IOException {
		// for now just call write with toString(), in the future we may want to write json responses differently
		write(response, json.toString(), false);
	}

	
	/**
	 * Write plain text directly to the response.
	 * @param response the response
	 * @param content the text
	 * @throws IOException if an error occurs
	 */
	public static void write(
			final HttpServletResponse response,
			final String content) throws IOException {

		write(response, content, false);
	}

	/**
	 * Write text directly to the response.
	 * @param response the response
	 * @param content the text
	 * @param html writes html to response if true
	 * @throws IOException if an error occurs
	 */
	public static void write(final HttpServletResponse response, final String content,
			final boolean html) throws IOException {
		write(response, content, html, "no-cache");
	}
	
	/**
	 * Write text directly to the response.
	 * @param response the response
	 * @param content the text
	 * @param html writes html to response if true
	 * @param cacheControl optional 'Cache-Control' header to write to the response.
	 * @throws IOException if an error occurs
	 */
	public static void write(
			final HttpServletResponse response,
			final String content, final boolean html,
			final String cacheControl) throws IOException {

		response.setContentType(html ? "text/html;charset=utf-8" : "text/plain;charset=utf-8");
		if (cacheControl != null) {
			response.setHeader("Cache-Control", cacheControl);
		}
		String cnt = content;
		if (!html && JSONUtils.mayBeJSON(cnt)) {
			// prepend all JSON responses with {}&& to prevent JavaScript hijacking.
			// see the wiki for more details
			cnt = "{}&&" + cnt;
		}
		response.getWriter().write(cnt);
		response.getWriter().flush();
	}
	
}
