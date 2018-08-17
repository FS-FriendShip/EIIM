package com.fs.eiim;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestService {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        System.out.println("Enter any key to shutdown......");
        System.in.read();
        context.close();
    }
}
