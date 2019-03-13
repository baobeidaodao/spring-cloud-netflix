package com.baobeidaodao.springcloudnetflix.zuul.service;

import com.netflix.zuul.context.RequestContext;

/**
 * @author DaoDao
 */
public interface ZuulFilterService {

    /**
     * 记录日志
     *
     * @param filterType     String
     * @param requestContext RequestContext
     */
    void log(String filterType, RequestContext requestContext);

}
