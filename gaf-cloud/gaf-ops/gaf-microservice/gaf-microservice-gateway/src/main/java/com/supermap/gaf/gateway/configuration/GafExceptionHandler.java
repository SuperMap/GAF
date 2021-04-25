/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.configuration;

import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.supermap.gaf.commontypes.GafCommonConstant.TRUE;


/**
 * 全局异常处理
 * @author : duke
 * @date:2021/3/25
 * @since 2020/4/29 4:01 PM
 */
public class GafExceptionHandler implements ErrorWebExceptionHandler {

    private static final String GAF_EXCEPTION_HANDLER_KEY = "GAF_EXCEPTION_HANDLER_KEY";
    private static final String DEV_ERROR = "DEV_ERROR";
    private List<ViewResolver> viewResolvers = Collections.emptyList();
    private List<HttpMessageReader<?>> messageReaders;
    private List<HttpMessageWriter<?>> messageWriters;
    private ErrorAttributes errorAttributes;
    public GafExceptionHandler(ServerCodecConfigurer serverCodecConfigurer, ErrorAttributes errorAttributes){
        this.messageReaders = serverCodecConfigurer.getReaders();
        this.messageWriters = serverCodecConfigurer.getWriters();
        this.errorAttributes = errorAttributes;
    }


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        //构造返回数据
        MessageResult messageResult = new MessageResult();
        messageResult.setSuccessed(false);
        messageResult.setMessage(throwable.toString());
        //如果参数里面带有DEV_ERROR = true,返回堆栈报错信息信息
        List<String> devErrors = exchange.getRequest().getQueryParams().get(DEV_ERROR);
        if (!CollectionUtils.isEmpty(devErrors) && TRUE.equals(devErrors.get(0))){
            messageResult.setData(throwable.getStackTrace());
        }
        //保存error对象
        errorAttributes.storeErrorInformation(throwable,exchange);
        //保存返回对象
        exchange.getAttributes().putIfAbsent(GAF_EXCEPTION_HANDLER_KEY,messageResult);
        //处理器
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(throwable);
        }
        ServerRequest request = ServerRequest.create(exchange,messageReaders);
        return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse)
                .route(request)
                .switchIfEmpty(Mono.error(throwable))
                .flatMap((handler) -> handler.handle(request))
                .flatMap((response) -> write(exchange, response));
    }

    /**
     * 构造response
     * @param request
     * @return
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        //获取保存的error的属性
        Map<String,Object> errorAttributesMap = errorAttributes.getErrorAttributes(request,false);
        //获取httpStatus
        HttpStatus httpStatus = getHttpStatus(errorAttributesMap);
        //获取保存的返回对象
        MessageResult result = request.exchange().getAttribute(GAF_EXCEPTION_HANDLER_KEY);
        result.setStatus(httpStatus.value());
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(result);
    }

    /**
     * 通过errorAttributes获取HttpStatus
     * @return
     */
    protected HttpStatus getHttpStatus(Map<String,Object> errorAttributesMap) {
        int statusCode = (int) errorAttributesMap.get("status");
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * response 写操作
     * @param exchange
     * @param response
     * @return
     */
    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        // force content-type since writeTo won't overwrite response header values
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }


    private class ResponseContext implements ServerResponse.Context {
        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return GafExceptionHandler.this.messageWriters;
        }
        @Override
        public List<ViewResolver> viewResolvers() {
            return GafExceptionHandler.this.viewResolvers;
        }
    }

}
