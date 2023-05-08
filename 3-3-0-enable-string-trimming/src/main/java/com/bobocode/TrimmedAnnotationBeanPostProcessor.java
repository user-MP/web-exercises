package com.bobocode;

import com.bobocode.annotation.EnableStringTrimming;
import com.bobocode.annotation.Trimmed;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is processor class implements {@link BeanPostProcessor}, looks for a beans where method parameters are marked with
 * {@link Trimmed} annotation, creates proxy of them, overrides methods and trims all {@link String} arguments marked with
 * {@link Trimmed}. For example if there is a string " Java   " as an input parameter it has to be automatically trimmed to "Java"
 * if parameter is marked with {@link Trimmed} annotation.
 * <p>
 *
 * Note! This bean is not marked as a {@link Component} to avoid automatic scanning, instead it should be created in
 * {@link StringTrimmingConfiguration} class which can be imported to a {@link Configuration} class by annotation
 * {@link EnableStringTrimming}
 */

public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {

//todo: Implement TrimmedAnnotationBeanPostProcessor according to javadoc

    private final Map<String, Class<?>> beanClassesWithTrimmedAnnotation = new HashMap<>();
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {


        if(isAnnotatedParams(bean)){
            beanClassesWithTrimmedAnnotation.put(beanName, bean.getClass());


        }


        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        Class<?> proxiedClass = beanClassesWithTrimmedAnnotation.get(beanName);
        if (proxiedClass != null && proxiedClass.isAssignableFrom(beanClass)) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback(new MethodInterceptor() {
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    Object[] trimmedArgs = modifyParametersIfAnnotated(method, args);
                    return proxy.invokeSuper(obj, trimmedArgs);
                }
            });
            return enhancer.create();
        }
        return bean;
    }



    private MethodInterceptor getTrimmingInterceptor() {
        return new MethodInterceptor() {
            @Override
            public Object intercept(Object beanInstance, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                Object[] trimmedArgs = modifyParametersIfAnnotated(method, args);
                return methodProxy.invokeSuper(beanInstance, trimmedArgs);
            }
        };
    }




    private Object[] modifyParametersIfAnnotated(Method method, Object[] args) {
        Parameter[] params = method.getParameters();
        List<Object> processedArgs = new ArrayList<>();

        for (Parameter param : params) {
            if (param.isAnnotationPresent(Trimmed.class)) {
                processedArgs.add(trimString(args[processedArgs.size()]));
            } else {
                processedArgs.add(args[processedArgs.size()]);
            }
        }

        return processedArgs.toArray();
    }


    public boolean isAnnotatedParams(Object bean){
        Parameter[] parameters = new Parameter[0];
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                if (parameter.isAnnotationPresent(Trimmed.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Object trimString(Object params){

        if (!(params instanceof String)) {
            return params;
        }
        String string = (String) params;
        return StringUtils.hasLength(string) ? string.trim() : params;
    }
}
