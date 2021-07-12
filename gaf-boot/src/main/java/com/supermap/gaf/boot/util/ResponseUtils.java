package com.supermap.gaf.boot.util;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wxl
 * @since 2021/7/7
 */
public class ResponseUtils {

    private ResponseUtils() {
    }


    public static void unAuth(HttpServletResponse httpServletResponse, String message) throws IOException {
        MessageResult<String> result = MessageResult.failed(String.class).status(HttpStatus.UNAUTHORIZED.value()).message(message).build();
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().print(JSON.toJSONString(result));
    }

}
