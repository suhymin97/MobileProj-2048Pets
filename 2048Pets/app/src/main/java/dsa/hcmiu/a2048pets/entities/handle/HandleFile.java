package dsa.hcmiu.a2048pets.entities.handle;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

import dsa.hcmiu.a2048pets.MyApplication;
import dsa.hcmiu.a2048pets.R;
import dsa.hcmiu.a2048pets.entities.model.Features;
import dsa.hcmiu.a2048pets.entities.model.User;

import static dsa.hcmiu.a2048pets.entities.model.Features.GUEST;
import static dsa.hcmiu.a2048pets.entities.model.Features.user;
import static dsa.hcmiu.a2048pets.entities.model.Features.usersList;

public class HandleFile {

    private static HandleFile instance;
    private static Context context;
    private static String file = "feature.json";
    private static StringWriter output;

    private HandleFile() {
        context = MyApplication.getContext();
    }

    public static HandleFile get() {
        if (instance == null) {
            instance = new HandleFile();
        }
        return instance;
    }
    public static String readJSONFile() throws IOException, JSONException {

        // Đọc nội dung text của file company.json
        String jsonText = readText(file);
        if (jsonText == null || jsonText.trim().isEmpty()) {
            readUserFile(GUEST);
            return null;
        }
        // Đối tượng JSONObject gốc mô tả toàn bộ tài liệu JSON.
        JSONObject jsonRoot = new JSONObject(jsonText);
        //text.write(jsonText); //show
        Log.d("HandleFile", "readJSONFile: " + jsonText);
        Features.sound = jsonRoot.getBoolean("sound");
        Features.theme = jsonRoot.getInt("theme");
        readUserFile(jsonRoot.getString("currPlayer"));
//      Features.user =  usersList.get(jsonRoot.getString("uid"));
        return user.getName();
    }

    public static void readUserFile() {
        try {
            String filename;
//            if (user.getSocialType() != null) {
                filename = user.socialType + user.getIDFacebook() + ".json";
//            } else filename = GUEST;
            readUserFile(filename);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void readUserFile(String filename) throws IOException, JSONException {

        // Đọc nội dung text của file .json
        String jsonText = readText(filename);
        if (jsonText == null || jsonText.trim().isEmpty()) {
            usersList.put(String.valueOf(user.getUID()),Features.user);
            return;
        }
        // Đối tượng JSONObject gốc mô tả toàn bộ tài liệu JSON.
        JSONObject jsonRoot = new JSONObject(jsonText);
            user.setUID(jsonRoot.getLong("uid"));
            user.setName(jsonRoot.getString("name"));
            user.setLogged(jsonRoot.getBoolean("logged"));
            user.setSocialType(jsonRoot.getString("SocialMedia"));
            user.setEmail(jsonRoot.getString("email"));
            user.setIDFacebook(jsonRoot.getString("IDFb"));
            user.setProfilePic(jsonRoot.getString("profilePic"));
            user.setAvatar(jsonRoot.getInt("avatar"));
            user.highScore = jsonRoot.getLong("highScore");
            user.undo = jsonRoot.getInt("undo");
            user.hammer = jsonRoot.getInt("hammer");
            user.totalGold = jsonRoot.getLong("totalGold");

            JSONArray jsonArray = jsonRoot.getJSONArray("purchasedIdItem");
            int[] id = new int[jsonArray.length()];
            user.purchasedIdItem = new ArrayList<>();
            for (int i = 0 ; i< jsonArray.length(); i++) {
                user.purchasedIdItem.add(jsonArray.getInt(i));
            }
            usersList.put(user.getIDFacebook(),user);
    }


    // Đọc nội dung text của một file nguồn.
    private static String readText(String filename) throws IOException {
        File f = new File(context.getFilesDir().getPath().toString() +"/" + filename);
        if(!f.exists()) {
            Log.i("File path",f.getPath());
            f.createNewFile(); //creating it
            return null;
        }
        InputStream is = context.openFileInput(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void convertToJSON() throws IOException {
        output = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(output);
        jsonWriter.beginObject();// begin root
            // "user": { ... }
        jsonWriter.name("uid").value(user.getUID());
        jsonWriter.name("name").value(user.getName());
        jsonWriter.name("logged").value(user.isLogged());
        jsonWriter.name("SocialMedia").value(user.getSocialType());
        jsonWriter.name("email").value(user.getEmail());
        jsonWriter.name("IDFb").value(user.getIDFacebook());
        jsonWriter.name("profilePic").value(user.getProfilePic());
        jsonWriter.name("avatar").value(user.getAvatar());
        jsonWriter.name("highScore").value(user.highScore);
        jsonWriter.name("undo").value(user.undo);
        jsonWriter.name("hammer").value(user.hammer);
        jsonWriter.name("totalGold").value(user.totalGold);

            // "purchase array": [ ....]
        jsonWriter.name("purchasedIdItem").beginArray(); // begin purchase array
        for(int item: user.purchasedIdItem) {
            jsonWriter.value(item);
        }
        jsonWriter.endArray();// end purchase array

        // end root
        jsonWriter.endObject();
    }

    public static void convertFeatureToJSON() throws IOException {
        output = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(output);
        String currPlayer;
//        if (user.getSocialType()!=null) {
            currPlayer = user.socialType+user.getIDFacebook()+".json";
//        } else currPlayer = GUEST;
        jsonWriter.beginObject();// begin root
        jsonWriter.name("sound").value(Features.sound);
        jsonWriter.name("currPlayer").value(currPlayer);
        jsonWriter.name("theme").value(Features.theme);
        // end root
        jsonWriter.endObject();
    }

    public static void writeUserToFile() throws IOException {
        convertToJSON();
        String filename;
//        if (user.getSocialType()!=null && user.getIDFacebook()!=null) {
            filename = user.socialType+user.getIDFacebook()+".json";
//        }
//        else {
//            filename = GUEST;
//        }
        File f = new File(context.getFilesDir().getPath().toString() +"/" + filename);//
        if(!f.exists()) {
            f.createNewFile(); //creating it
        }
        FileOutputStream fos = context.openFileOutput(filename,Context.MODE_PRIVATE);
        fos.write(output.toString().getBytes());
        fos.close();
    }

    public static void writeFeatureToFile() throws IOException {
        convertFeatureToJSON();
        File f = new File(context.getFilesDir().getPath().toString() +"/" + file);//
        if(!f.exists()) {
            f.createNewFile(); //creating it
        }
        FileOutputStream fos = context.openFileOutput(file,Context.MODE_PRIVATE);
        fos.write(output.toString().getBytes());
        fos.close();
    }
}
