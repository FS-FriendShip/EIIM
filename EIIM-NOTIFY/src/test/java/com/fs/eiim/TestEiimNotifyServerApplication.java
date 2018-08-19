package com.fs.eiim;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestEiimNotifyServerApplication {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestEiimNotifyServerConfig.class);
        System.out.println("Press any key to shutdown......");
        System.in.read();
        context.close();
    }
}
