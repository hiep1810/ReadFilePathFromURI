package com.example.getjsonfilefromexternal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_FILE = 101;
    private static final int PERMISSION_READ_STORAGE = 1;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        Log.e("externalStorage", Environment.getExternalStorageDirectory().getAbsolutePath().toString());
        //initPermissionRead();
        choseFile();

    }
    private void choseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//lấy ra tất cả dữ liệu
        startActivityForResult(intent, REQUEST_FILE);
    }
    public void initPermissionRead() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_STORAGE);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//kết quả trả về khi chọn ảnh
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();// lấy uri(không phải đường dẫn(gg biết thêm)
            String getReal = "";
            getReal += RealFilePathUtil.getPath(this,uri);
            Log.e("realpath",getReal);
            //realpath = getRealPathFromURI(uri);//lấy ra đường đẫn nè
            //Log.i("ahihi", "onActivityResult: "+realpath);
            try {

                InputStream inputStream = this.getContentResolver().openInputStream(uri);//tạo 1 luồng mở hình ảnh
                //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);//chuyển kiểu type hình ảnh về bitmap
                JsonElement element = new JsonParser().parse(new InputStreamReader(inputStream));
                JSONObject jsonObject = new JSONObject(element.getAsJsonObject().toString());


                textView.setText(jsonObject.toString(4));
                inputStream.close();
                /*
                imageProfile.setImageBitmap(bitmap);
                AlertDialog alertDialog = getArlertDialog(bitmap).create();
                alertDialog.show();
                */

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
