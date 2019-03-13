package com.baobeidaodao.springcloudnetflix.zuul.service.impl;

import com.baobeidaodao.springcloudnetflix.zuul.service.ZuulFilterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.util.Pair;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author DaoDao
 */
@Slf4j
@Service
public class ZuulFilterServiceImpl implements ZuulFilterService {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void log(String filterType, RequestContext requestContext) {
        switch (filterType) {
            case FilterConstants.PRE_TYPE:
                preLog(requestContext);
                break;
            case FilterConstants.ROUTE_TYPE:
                routeLog(requestContext);
                break;
            case FilterConstants.POST_TYPE:
                postLog(requestContext);
                break;
            case FilterConstants.ERROR_TYPE:
                errorLog(requestContext);
                break;
            default:
                break;
        }
    }

    private void preLog(RequestContext requestContext) {
        HttpServletRequest httpServletRequest = requestContext.getRequest();
        requestContext.getResponseDataStream();
        requestContext.getResponseBody();
        String requestURI = httpServletRequest.getRequestURI();
        log.info("request URI:" + requestURI);
        String method = httpServletRequest.getMethod();
        log.info("request method:" + method);
        String queryString = httpServletRequest.getQueryString();
        log.info("request query string:" + queryString);
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        try {
            String parameterMapString = objectMapper.writeValueAsString(parameterMap);
            log.info("request parameter map:" + parameterMapString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Map<String, String> headerMap = new HashMap<>(1 << 4);
        Enumeration headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            headerMap.put(key, value);
        }
        try {
            String headerMapString = objectMapper.writeValueAsString(headerMap);
            log.info("request header map:" + headerMapString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String headerMsgTp = httpServletRequest.getHeader("MsgTp");
        log.info("request header MsgTp:" + headerMsgTp);
        try {
            InputStream inputStream = httpServletRequest.getInputStream();
            String requestBody = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));
            log.info("request body:" + requestBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void routeLog(RequestContext requestContext) {
    }

    private void postLog(RequestContext requestContext) {
        HttpServletResponse httpServletResponse = requestContext.getResponse();
        Map<String, String> responseHeaderMap = new HashMap<>(1 << 4);
        Collection<String> headerNames = httpServletResponse.getHeaderNames();
        for (String headerName : headerNames) {
            String headerValue = httpServletResponse.getHeader(headerName);
            responseHeaderMap.put(headerName, headerValue);
        }
        try {
            String responseHeaderMapString = objectMapper.writeValueAsString(responseHeaderMap);
            log.info("response header map:" + responseHeaderMapString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Map<String, String> originResponseHeaderMap = new HashMap<>(1 << 4);
        List<Pair<String, String>> originResponseHeaderList = requestContext.getOriginResponseHeaders();
        for (Pair<String, String> pair : originResponseHeaderList) {
            String headerName = pair.first();
            String headerValue = pair.second();
            originResponseHeaderMap.put(headerName, headerValue);
        }
        try {
            String originResponseHeaderMapString = objectMapper.writeValueAsString(originResponseHeaderMap);
            log.info("origin response header map:" + originResponseHeaderMapString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Map<String, String> zuulResponseHeaderMap = new HashMap<>(1 << 4);
        List<Pair<String, String>> zuulResponseHeaderList = requestContext.getZuulResponseHeaders();
        for (Pair<String, String> pair : zuulResponseHeaderList) {
            String headerName = pair.first();
            String headerValue = pair.second();
            zuulResponseHeaderMap.put(headerName, headerValue);
        }
        try {
            String zuulResponseHeaderMapString = objectMapper.writeValueAsString(zuulResponseHeaderMap);
            log.info("zuul response header map:" + zuulResponseHeaderMapString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        int responseStatus = httpServletResponse.getStatus();
        log.info("response status:" + responseStatus);
        InputStream inputStream = requestContext.getResponseDataStream();
        String responseData = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining(System.lineSeparator()));
        log.info("response data:" + responseData);
        // 记录日志之后，一定要把 response data 设置回 response body ！！！
        requestContext.setResponseBody(responseData);
    }

    private void errorLog(RequestContext requestContext) {
    }

}
