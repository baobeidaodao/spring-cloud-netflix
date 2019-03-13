package com.baobeidaodao.springcloudnetflix.zuul.filter;

import com.baobeidaodao.springcloudnetflix.zuul.service.ZuulFilterService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author DaoDao
 */
@Slf4j
@Component
public class RouteFilter extends ZuulFilter {

    @Resource
    private ZuulFilterService zuulFilterService;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String filterType = filterType();
        zuulFilterService.log(filterType, requestContext);
        return null;
    }

}
