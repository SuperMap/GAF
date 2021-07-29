package com.supermap.gaf.authority.client;

import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "GAF-AUTHORITY", contextId = "AuthUserClient")
public interface AuthUserClient {

    @GetMapping("/authority/auth-users/{username}/roles")
    MessageResult<List<AuthRole>> selectUserRoles(@PathVariable("username") String username);
}
