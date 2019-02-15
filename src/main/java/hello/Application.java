package hello;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class Application {
    private static DatabaseReference database;
    private static final String DATABASE_URL = "";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

            try {
                FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(DATABASE_URL)
                        .build();
                System.out.println("###############options"+options);

                FirebaseApp.initializeApp(options);
                database = FirebaseDatabase.getInstance().getReference();
                System.out.println("############### database#########"+database);


            } catch (IOException e) {
                System.out.println("ERROR: invalid service account credentials. See README.");
                System.out.println(e.getMessage());

                System.exit(1);
            }
        };
    }

}
