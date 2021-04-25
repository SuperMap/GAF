package com.supermap.gaf.authority.service.p3sync.impl.sinfcloud.util;

import com.supermap.gaf.exception.GafException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * @author yd
 */
public final class SinfCloudRestUtil {

    private static SinfCloudRestUtil instance = null;

    private SinfCloudRestUtil() {
    }

    private RestTemplate restTemplate;

    public static SinfCloudRestUtil getInstance(RestTemplate restTemplate) {
        if (null == instance) {
            synchronized (SinfCloudRestUtil.class) {
                instance = new SinfCloudRestUtil();
                instance.setRestTemplate(restTemplate);
            }
        }
        return instance;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object doPostRequest(String url, HttpEntity<String> request) {
        ResponseEntity<Map> postForEntity = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        return getRequestData(postForEntity);
    }

    public Object doGetRequest(String url, HttpEntity<String> request) {
        ResponseEntity<Map> getForEntity = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        return getRequestData(getForEntity);
    }

    public Object doPutRequest(String url, HttpEntity<String> request) {
        ResponseEntity<Map> putForEntity = restTemplate.exchange(url, HttpMethod.PUT, request, Map.class);
        return getRequestData(putForEntity);
    }

    public Object doDeleteRequest(String url, HttpEntity<String> request) {
        ResponseEntity<Map> deleteForEntity = restTemplate.exchange(url, HttpMethod.DELETE, request, Map.class);
        return getRequestData(deleteForEntity);
    }

    private Object getRequestData(ResponseEntity<Map> postForEntity) {
        Map response = postForEntity.getBody();
        Integer code = (Integer) Objects.requireNonNull(response).get("code");
        if (0 != code) {
            throw new GafException(response.get("msg").toString());
        }
        return response.get("data");
    }

}
