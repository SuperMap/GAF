package com.supermap.gaf.authority.client;

import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "GAF-AUTHORITY", contextId = "AuthUserClient")
@Component
public interface AuthUserClient {

    String SOME_INFO_ROLE_IDS_KEY = "roleIds";
    String SOME_INFO_TENANT_ID_KEY = "tenantId";

    @GetMapping("/authority/auth-users/{username}/some-info")
    MessageResult<Map<String,Object>> someInfo(@PathVariable("username") String username);

}
