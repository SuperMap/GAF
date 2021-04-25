package com.supermap.gaf.boot.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class HeaderRequestWrapper extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public HeaderRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    private final Map<String, String> customHeaders = new HashMap<>();

    /**
     *  put a header with given name and value
     *  note:
     *  The header name is case sensitive.
     *  this is not same with  "HttpServletRequest.getHeader"
     * @param name
     * @param value
     */
    void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);
        if (headerValue != null){
            return headerValue;
        }
        return super.getHeader(name);
    }

    /**
     * get the Header names
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());
        Enumeration<String> headerEnums = super.getHeaderNames();
        if (headerEnums != null) {
            while (headerEnums.hasMoreElements()) {
                String headerName = headerEnums.nextElement();
                set.add(headerName);
            }
        }
        return Collections.enumeration(set);
    }

}
