package hello;

import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
//import java.util.concurrent.CountDownLatch;



@RestController
public class HelloController {
    private FirebaseDatabase firebaseDatabase;
    private Map<String, Boolean> role = (new HashMap<String, Boolean>() {
        {
            put("free_user", true);
            put("paid_user", false);
            put("admin", false);
        }
    });

    @RequestMapping("/hello")
    public ResponseEntity<?> index(HttpServletRequest request) {
        return new ResponseEntity<>("Greetings from Spring Boot by http!", HttpStatus.OK);
    }


    /**
     * Save users object in Firebase.
     *
     * @param user
     */
    private void save(JsonObject user,String uid) {
        if (user != null) {
//            initFirebase();
            firebaseDatabase = FirebaseDatabase.getInstance();
            System.out.println("#############firebaseDatabase is " + firebaseDatabase);

            /* Get database root reference */
            DatabaseReference databaseReference = firebaseDatabase.getReference("/");

            /* Get existing child or will be created new child. */
            DatabaseReference childReference = databaseReference.child("users/" + uid);

            /**
             * The Firebase Java client uses daemon threads, meaning it will not prevent a process from exiting.
             * So we'll wait(countDownLatch.await()) until firebase saves record. Then decrement `countDownLatch` value
             * using `countDownLatch.countDown()` and application will continues its execution.
             */
            Map<String, Object> userMap = new Gson().fromJson(
                    user, new TypeToken<HashMap<String, Object>>() {}.getType()
            );

            HashMap<String, Boolean> roleMap = new HashMap<>();
            roleMap.put("free_user", true);
            roleMap.put("paid_user", false);
            roleMap.put("admin", false);

            HashMap<String, HashMap> role = new HashMap<>();
            role.put("role",roleMap);

            userMap.putAll(role);
            childReference.setValueAsync(userMap);//setValueAsync has to be HashMap object

        }
    }

    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody String jsonString) throws FirebaseAuthException {//String jsonString

        //


        JsonObject user = new JsonParser().parse(jsonString).getAsJsonObject();

        String pageName = user.get("name").getAsString();
        System.out.println("##########called!!!" + user+"name is:"+pageName);
//        System.out.println("##########called!!!" + users.getName()+"email is "+users.getEmail()+"password is :"+users.getPassword());
//

        //
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(user.get("email").getAsString())
                .setEmailVerified(true)
                .setPassword(user.get("password").getAsString())
                .setDisplayName(user.get("name").getAsString())
                .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user: " + userRecord.getUid());
//        users.setId(userRecord.getUid());
        //



        //
        save(user, userRecord.getUid());
        return new ResponseEntity<>("Successfully created new user", HttpStatus.OK);
    }

}



