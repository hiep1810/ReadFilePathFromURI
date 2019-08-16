package com.example.getjsonfilefromexternal;

import android.os.Environment;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

public class ReadJsonFile {
    private Ringtone ringtone;
    private List<Ringtone> ringtoneList;
    public List<Ringtone> readFile(String name){
        try {
            JSONObject jsonObj = new JSONObject(loadFileJson(name));
            JSONArray data  = jsonObj.getJSONArray("ringtones");
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                String file_name = c.getString("file_name");

                ringtone=new Ringtone();
                ringtone.setFileName(file_name);

                ringtoneList.add(ringtone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ringtoneList;
    }

    private String loadFileJson(String name){
        String jsonStr = null;
        Log.e("externalStorage",Environment.getExternalStorageDirectory().getAbsolutePath().toString());
        try {
            File yourFile = new File(Environment.getExternalStorageDirectory(), Configs.PATH_STORAGE_CATEGORY+"/"+name+"/ringtones.json");
            FileInputStream stream = new FileInputStream(yourFile);
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            jsonStr = Charset.defaultCharset().decode(bb).toString();
            stream.close();
            Log.e("jsonString",jsonStr);
        }
        catch(Exception e){
            e.printStackTrace();
        }


        return jsonStr;
    }
//    public String readFromFile(String name) {
//
//        String ret = "";
//        InputStream inputStream = null;
//        try {
//            File yourFile = new File(Environment.getExternalStorageDirectory(), Configs.PATH_STORAGE_CATEGORY+"/"+name+"/ringtones.json");
////            inputStream = openFileInput("names.json");
//            inputStream=new FileInputStream(yourFile);
//
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append(receiveString);
//                }
//
//                ret = stringBuilder.toString();
//            }
//        }
//        catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
//        finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return ret;
//    }
}
