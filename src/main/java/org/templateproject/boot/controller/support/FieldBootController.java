package org.templateproject.boot.controller.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wuwenbin on 2017/6/4.
 */
public class FieldBootController {

    @Autowired
    private HttpServletRequest request;

    /**
     * 日志对象
     */
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());


    /**
     * 获取request
     *
     * @return
     */
    protected HttpServletRequest getRequest() {
        return new TemplateRequestWrapper(this.request);
    }
}
