package com.demo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppFactory {

    private ApplicationContext context;

    private static AppFactory instance;

    private AppFactory() {
        loadContext();
    }

    private void loadContext() {
        try {
            context = new ClassPathXmlApplicationContext("client-context.xml");
        } catch (BeansException e) {
            throw new IllegalArgumentException("Failed to instantiate Spring context", e);
        }
    }

    public static AppFactory getInstance() {
        if (instance == null) {
            instance = new AppFactory();
        }
        return instance;
    }

    public <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
