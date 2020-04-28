package com.musicdb.artistservice.aspect.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

public class Util {

	private static final Collection<String> KNOWN_PATH_VARIABLES = Collections
			.unmodifiableCollection(new HashSet<String>() {
				{
					add("artistId");
				}
			});
	private static final Collection<String> KNOWN_QUERY_PARAMETERS = Collections
			.unmodifiableCollection(new HashSet<String>() {
				{
					add("keyword");
				}
			});

	public static HttpServletRequest extractRequest(Object[] args) {
		if (args.length != 0) {
			for (final Object item : args) {
				if (item != null && HttpServletRequest.class.isAssignableFrom(item.getClass())) {
					return (HttpServletRequest) item;
				}
			}
		}
		throw new RuntimeException("HttpServletRequest not found as argument.");
	}

	public static Map<String, String> extractPathVariables(HttpServletRequest request) {
		return Collections.unmodifiableMap(new HashMap<String, String>() {
			{
				KNOWN_PATH_VARIABLES.forEach(item -> {
					final String pathVariableValue = ((Map<String, String>) request
							.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).get(item);
					if (pathVariableValue != null) {
						put(item, pathVariableValue);
					}
				});
			}
		});
	}

	public static Map<String, String> extractQueryParams(HttpServletRequest request) {
		return Collections.unmodifiableMap(new HashMap<String, String>() {
			{
				KNOWN_QUERY_PARAMETERS.forEach(item -> {
					final String pathVariableValue = request.getParameter(item);
					if (pathVariableValue != null) {
						put(item, pathVariableValue);
					}
				});
			}
		});
	}

	public static String getAsStringNullable(Map<String, String> map, String key) {
		return StringUtils.trimToNull(map.get(key));
	}

	public static String extractPassId(Map<String, String> pathVariables) {
		return getAsStringNullable(pathVariables, "artistId");
	}

}
