package com.givee.demo.client.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.givee.demo.client.AppUI;
import com.givee.demo.client.HasLogger;
import com.givee.demo.client.domain.ColumnDto;
import com.givee.demo.client.domain.LoginDto;
import com.givee.demo.client.domain.TableDto;
import com.givee.demo.client.utils.BeanUtil;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

public class AppRestTemplate implements HasLogger {
	private AppRestTemplateFactory factory = BeanUtil.getBean(AppRestTemplateFactory.class);
	private ObjectMapper objectMapper = BeanUtil.getBean(ObjectMapper.class);

	private String POSTGRES_API_PATH = factory.getServerAddress() + "/api/v1/postgres";
	private String ORACLE_API_PATH = factory.getServerAddress() + "/api/v1/oracle";
	private String LOGIN_PATH = POSTGRES_API_PATH + "/login";

	private String TABLES = POSTGRES_API_PATH + "/tables";

	private String TABLE_COLUMNS = TABLES + "/%s/columns";
	private String TABLE_VALUES = TABLES + "/%s/values";

	/**
	 * Create simple template with basic auth for requests
	 * @return RestOperations
	 */
	private RestOperations initRestTemplate() {
		LoginDto login = AppUI.getCurrentLogin();
		if (login == null) return factory.createRestOperations();
		else return factory.createRestOperations(login);
	}

	/**
	 * Log exceptions for debug purposes
	 * @param url requested url
	 * @param e exception
	 */
	private void logExceptions(String url, RestClientException e) {
		if (e instanceof RestClientResponseException) {
			getLogger().error("Request {} returns with error {}", url, ((RestClientResponseException)e).getResponseBodyAsString());
		}
		if (e instanceof ResourceAccessException) {
			getLogger().error(e.getMessage());
		}
	}

	/**
	 * Create map for request parameters
	 * @return MultiValueMap
	 */
	private MultiValueMap<String, String> createMap() {
		LoginDto login = AppUI.getCurrentLogin();
		LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		if (login != null) {
			multiValueMap.add("database", login.getDatabase());
		}
		return multiValueMap;
	}

	/**
	 * Common template for entity request
	 * @param url URL
	 * @param method HTTP method of request
	 * @param httpEntity body of request
	 * @param tClass returned class
	 * @param <T> parametrized class
	 * @return entity
	 */
	private <T> T makeEntityRequest(String url, HttpMethod method, HttpEntity httpEntity, Class<T> tClass) {
		try {
			TypeToken<T> resultTypeToken = TypeToken.of(tClass);
			ParameterizedTypeReference<T> responseTypeRef = ParameterizedTypeReferenceBuilder.fromTypeToken(
					resultTypeToken.where(new TypeParameter<T>() {}, tClass));
			ResponseEntity<T> response = initRestTemplate().exchange(url, method, httpEntity, responseTypeRef);
			return response.getBody();
		} catch (RestClientException e) {
			logExceptions(url, e);
			return null;
		}
	}

	/**
	 * Common template for list request
	 * @param url URL
	 * @param method HTTP method of request
	 * @param httpEntity body of request
	 * @param tClass returned class
	 * @param <T> parametrized class
	 * @return list of entities
	 */
	private <T> List<T> makeListRequest(String url, HttpMethod method, HttpEntity httpEntity, Class<T> tClass) {
		try {
			TypeToken<T> resultTypeToken = TypeToken.of(tClass);
			ParameterizedTypeReference<List<T>> responseTypeRef = ParameterizedTypeReferenceBuilder.fromTypeToken(
					new TypeToken<List<T>>() {}.where(new TypeParameter<T>() {}, resultTypeToken));
			ResponseEntity<List<T>> response = initRestTemplate().exchange(url, method, httpEntity, responseTypeRef);
			return response.getBody();
		} catch (RestClientException e) {
			logExceptions(url, e);
			return Collections.emptyList();
		}
	}

	/**
	 * Simple function to return entity (GET request)
	 * @param url URL
	 * @param tClass returned class
	 * @param <T> parametrized class
	 * @return entity
	 */
	private <T> T getEntity(String url, Class<T> tClass) {
		return makeEntityRequest(url, HttpMethod.GET, null, tClass);
	}

	/**
	 * Simple function to save entity (POST request)
	 * @param url URL
	 * @param entity entity to save
	 * @param tClass returned class
	 * @param <T> parametrized class
	 * @return saved entity
	 */
	private <T> T saveEntity(String url, T entity, Class<T> tClass) {
		return makeEntityRequest(url, HttpMethod.POST, new HttpEntity<>(entity), tClass);
	}

	/**
	 * Simple function to update entity (PUT request)
	 * @param url URL
	 * @param entity entity to update
	 * @param tClass returned class
	 * @param <T> parametrized class
	 * @return updated entity
	 */
	private <T> T updateEntity(String url, T entity, Class<T> tClass) {
		return makeEntityRequest(url, HttpMethod.PUT, new HttpEntity<>(entity), tClass);
	}

	/**
	 * Simple function to delete entity (DELETE request)
	 * @param url URL
	 */
	private void deleteEntity(String url) {
		makeEntityRequest(url, HttpMethod.DELETE, null, Void.class);
	}

	/**
	 * Simple function to return list of entities (GET request)
	 * @param url URL
	 * @param tClass returned class
	 * @param <T> parametrized class
	 * @return list of entities
	 */
	private <T> List<T> getList(String url, Class<T> tClass) {
		return makeListRequest(url, HttpMethod.GET, null, tClass);
	}

	/**
	 * Simple function to save list of entities (POST request)
	 * @param url URL
	 * @param entityList list of entities to save
	 * @param tClass returned class
	 * @param <T> parametrized class
	 * @return saved list of entities
	 */
	private <T> List<T> saveList(String url, List<T> entityList, Class<T> tClass) {
		return makeListRequest(url, HttpMethod.POST, new HttpEntity<>(entityList), tClass);
	}

	/**
	 * Simple function to update list of entities (POST request)
	 * @param url URL
	 * @param entityList list of entities to update
	 * @param tClass returned class
	 * @param <T> parametrized class
	 * @return updated list of entities
	 */
	private <T> List<T> updateList(String url, List<T> entityList, Class<T> tClass) {
		return makeListRequest(url, HttpMethod.PUT, new HttpEntity<>(entityList), tClass);
	}

	public String doLogin() {
		MultiValueMap<String, String> map = createMap();
		String url = UriComponentsBuilder.fromUriString(LOGIN_PATH).queryParams(map).build().toUriString();
		return saveEntity(url, null, String.class);
	}

	public List<TableDto> getTables() {
		MultiValueMap<String, String> map = createMap();
		String url = UriComponentsBuilder.fromUriString(TABLES).queryParams(map).build().toUriString();
		return getList(url, TableDto.class);
	}

	public List<ColumnDto> getTableColumns(String tableName) {
		MultiValueMap<String, String> map = createMap();
		String url = UriComponentsBuilder.fromUriString(String.format(TABLE_COLUMNS, tableName)).queryParams(map).build().toUriString();
		return getList(url, ColumnDto.class);
	}

	public List<ObjectNode> getTableValues(String tableName) {
		MultiValueMap<String, String> map = createMap();
		String url = UriComponentsBuilder.fromUriString(String.format(TABLE_VALUES, tableName)).queryParams(map).build().toUriString();
		return getList(url, ObjectNode.class);
	}
}
