package com.fs.eiim;

import com.fs.eiim.config.EiimAppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EiimApplication {
    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) throws Exception {
        context = new AnnotationConfigApplicationContext(EiimAppConfig.class);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter quit command to stop the server: [QUIT | quit | EXIT | exit]");
            String line = reader.readLine();
            if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                stopServer();
                break;
            }
        }
    }

    public static void stopServer() {
        if (context != null) {
            context.close();
        }
    }
}
