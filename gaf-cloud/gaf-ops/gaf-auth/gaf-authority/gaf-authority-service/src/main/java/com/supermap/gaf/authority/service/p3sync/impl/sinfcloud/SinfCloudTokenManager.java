package com.supermap.gaf.authority.service.p3sync.impl.sinfcloud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.gaf.authority.service.p3sync.impl.sinfcloud.util.SinfCloudEncryptedUtil;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * sinfcloud token管理服务
 * 单例
 *
 * @author yd
 */
public final class SinfCloudTokenManager {

    private static final Logger logger = LogUtil.getLocLogger(SinfCloudTokenManager.class);

    private static SinfCloudTokenManager instance = null;

    private SinfCloudTokenManager() {
    }

    /**
     * sinfcloud 用户名与token映射关系缓存
     */
    private final Map<String, String> tokenMap = new HashMap<>();

    private RestTemplate restTemplate;

    private String sinfCloudPublicKeyUrl;
    private String sinfCloudLoginUrl;
    private String sinfCloudVerifyTokenUrl;

    public static SinfCloudTokenManager getInstance(String sinfCloudPublicKeyUrl, String sinfCloudLoginUrl, String sinfCloudVerifyTokenUrl, RestTemplate restTemplate) {
        if (null == instance) {
            synchronized (SinfCloudTokenManager.class) {
                instance = new SinfCloudTokenManager();
                instance.setSinfCloudVerifyTokenUrl(sinfCloudVerifyTokenUrl);
                instance.setSinfCloudPublicKeyUrl(sinfCloudPublicKeyUrl);
                instance.setSinfCloudLoginUrl(sinfCloudLoginUrl);
                instance.setRestTemplate(restTemplate);
            }
        }
        return instance;
    }

    /**
     * 获取sinfcloud token
     *
     * @param hostUrl  sinfcloud分为平台管理端和业务操作端，hostUrl不一样，已传参解决
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    public synchronized String getToken(String hostUrl, String username, String password, String tenantId) {
        String accessToken;
        try {
            //1、尝试从缓存中获取token
            accessToken = tokenMap.get(username);
            //1.1 校验缓存中token有效性
            boolean isTokenValid = verifyToken(hostUrl, accessToken);
            if (isTokenValid) {
                return accessToken;
            }
            //2、移除缓存的token，并尝试获取新token，缓存下来
            //2.1、移除缓存token
            tokenMap.remove(username);

            //2.2、尝试获取新token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

            HttpEntity<String> publicKeyRequest = new HttpEntity<>(null, headers);

            String publicKeyUrl = String.format("%s%s", hostUrl, sinfCloudPublicKeyUrl);
            ResponseEntity<Map> publicKeyPostForEntity = restTemplate.postForEntity(publicKeyUrl, publicKeyRequest, Map.class);
            Map publicKeyPostForEntityBody = publicKeyPostForEntity.getBody();
            Integer code = (Integer) publicKeyPostForEntityBody.get("code");
            if (0 == code) {
                Map<String, String> dataMap = (Map<String, String>) publicKeyPostForEntityBody.get("data");
                String publicKey = dataMap.get("publicKey");
                String id = dataMap.get("id");

                logger.info("publicKey: " + publicKey);
                logger.info("keyid: " + id);
                password = SinfCloudEncryptedUtil.encryptedBase64(password, publicKey);
                logger.info("加密后密码：" + password);

                String loginUrl = String.format("%s%s", hostUrl, sinfCloudLoginUrl);
                String accessTokenUrl = String.format("%s?keyid=%s", loginUrl, id);

                Map<String, String> loginUser = new HashMap<>(3);
                loginUser.put("username", username);
                loginUser.put("password", password);
                //todo tenantID通过传参解决
                loginUser.put("tenantID", tenantId);

                ObjectMapper mapper = new ObjectMapper();
                String param = mapper.writeValueAsString(loginUser);
                HttpEntity<String> accessTokenRequest = new HttpEntity<>(param, headers);
                ResponseEntity<Map> accessTokenPostForEntity = restTemplate.postForEntity(accessTokenUrl, accessTokenRequest, Map.class);
                Map accessTokenPostForEntityBody = accessTokenPostForEntity.getBody();
                Integer accessTokenCode = (Integer) accessTokenPostForEntityBody.get("code");
                if (0 == accessTokenCode) {
                    Map<String, String> accessTokenDataMap = (Map<String, String>) accessTokenPostForEntityBody.get("data");
                    accessToken = accessTokenDataMap.get("token");
                } else {
                    throw new GafException(accessTokenPostForEntityBody.get("msg").toString());
                }
            } else {
                throw new GafException(publicKeyPostForEntityBody.get("msg").toString());
            }
            //2.3、缓存新token
            tokenMap.put(username, accessToken);
            return accessToken;
        } catch (Exception e) {
            logger.error("获取sinfcloud token异常", e);
            throw new GafException("获取sinfcloud token异常," + e.getMessage());
        }
    }

    /**
     * 校验sinfcloud token有效性
     *
     * @param hostUrl     地址
     * @param accessToken token
     * @return true 成功 false 实现
     * @throws JsonProcessingException josn处理异常
     */
    private boolean verifyToken(String hostUrl, String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return false;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("access_token", accessToken);

        HttpEntity<String> verifyTokenRequest = new HttpEntity<>(null, headers);

        String verifyUrl = String.format("%s%s", hostUrl, sinfCloudVerifyTokenUrl);
        ResponseEntity<Map> verifyTokenPostForEntity = restTemplate.postForEntity(verifyUrl, verifyTokenRequest, Map.class);
        Map verifyTokenPostForEntityBody = verifyTokenPostForEntity.getBody();
        Integer code = (Integer) verifyTokenPostForEntityBody.get("code");
        if (0 == code) {
            return true;
        }
        logger.warn("sinfcloud token无效，" + verifyTokenPostForEntityBody.get("msg"));
        return false;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setSinfCloudPublicKeyUrl(String sinfCloudPublicKeyUrl) {
        this.sinfCloudPublicKeyUrl = sinfCloudPublicKeyUrl;
    }

    public void setSinfCloudLoginUrl(String sinfCloudLoginUrl) {
        this.sinfCloudLoginUrl = sinfCloudLoginUrl;
    }

    public void setSinfCloudVerifyTokenUrl(String sinfCloudVerifyTokenUrl) {
        this.sinfCloudVerifyTokenUrl = sinfCloudVerifyTokenUrl;
    }
}
