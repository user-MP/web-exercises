package com.bobocode.config;

import com.bobocode.util.ExerciseNotCompletedException;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This class is used to configure DispatcherServlet and links it with application config classes
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ RootConfig.class};
//        throw new ExerciseNotCompletedException(); //todo: use {@link RootConfig} as root application config class
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
//        throw new ExerciseNotCompletedException(); //todo: use {@link WebConfig} as ServletConfig class
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
//        throw new ExerciseNotCompletedException(); //todo: provide default servlet mapping ("/")
    }
}
