package org.templateproject.boot.advice;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.templateproject.boot.advice.support.DateEditor;
import org.templateproject.boot.advice.support.DoubleEditor;
import org.templateproject.boot.advice.support.IntegerEditor;
import org.templateproject.boot.advice.support.LongEditor;

import java.util.Date;

/**
 * Created by wuwenbin on 2017/6/4.
 */
@ControllerAdvice
public class SimpleBootAdvice {

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

}
