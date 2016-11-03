package allan.mydccar;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.ContentResolver;

public class Main4Activity extends AppCompatActivity {
    private ImageView imgPicture;
    private final String PERMISSION_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private Button show;
    private final static int PHOTO = 99 ;
    private DisplayMetrics mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        initView();
        show= (Button) findViewById(R.id.show);

        show.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                //建立一個AsyncTask執行緒進行圖片讀取動作，並帶入圖片連結網址路徑
                new AsyncTask<String, Void, Bitmap>()
                {
                    @Override
                    protected Bitmap doInBackground(String... params)
                    {
                        String url = params[0];
                        return getBitmapFromURL(url);
                    }

                    @Override
                    protected void onPostExecute(Bitmap result)
                    {
                        imgPicture. setImageBitmap (result);
                        super.onPostExecute(result);
                    }
                }.execute("http://www.99166.com/article/UpLoadFile/UploadFile/20121025111436571.jpg");
            }
        });
        Button mPhoto = (Button) findViewById(R.id.photo);
        mPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //開啟相簿相片集，須由startActivityForResult且帶入requestCode進行呼叫，原因為點選相片後返回程式呼叫onActivityResult
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PHOTO);
            }
        });
    }

    //讀取網路圖片，型態為Bitmap
    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void initView() {
        imgPicture = (ImageView)findViewById(R.id.imgPicture);
        //
        Button btnSavePicture = (Button)findViewById(R.id.btnSavePicture);
        btnSavePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermission()) {
                    if (needCheckPermission()) {
                        //如果須要檢查權限，由於這個步驟要等待使用者確認，
                        //所以不能立即執行儲存的動作，
                        //必須在 onRequestPermissionsResult 回應中才執行
                        return;
                    }
                }
                doSavePicture();
            }
        });
    }
    private void doSavePicture() {
        if (saveToPictureFolder()) {
            Toast.makeText(Main4Activity.this, "儲存成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Main4Activity.this, "儲存失敗", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 儲存到圖片庫
     */
    private boolean saveToPictureFolder() {
        //取得 Pictures 目錄
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.d(">>>", "Pictures Folder path: " + picDir.getAbsolutePath());
        //假如有該目錄
        if (picDir.exists()) {
            //儲存圖片
            File pic = new File(picDir,"gg"+ System.currentTimeMillis()+".jpg");
            imgPicture.setDrawingCacheEnabled(true);
            imgPicture.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
            Bitmap bmp = imgPicture.getDrawingCache();
            return saveBitmap(bmp, pic);
        }
        return false;
    }
    private boolean saveBitmap(Bitmap bmp, File pic) {
        if (bmp == null || pic == null) return false;
        //
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pic);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            scanGallery(this, pic);
            Log.d(">>>", "bmp path: " + pic.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Log.e(">>>", "save bitmap failed!");
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private void scanGallery(Context ctx, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }
    ////////////////////////////////////////////////
    /**
     * 確認是否要請求權限(API > 23)
     * API < 23 一律不用詢問權限
     */
    private boolean needCheckPermission() {
        //MarshMallow(API-23)之後要在 Runtime 詢問權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {PERMISSION_WRITE_STORAGE};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
            return true;
        }
        return false;
    }
    /**
     * 是否已經請求過該權限
     * API < 23 一律回傳 true
     */
    private boolean hasPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return(ActivityCompat.checkSelfPermission(this, PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200){
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(">>>", "取得授權，可以執行動作了");
                    doSavePicture();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ((requestCode == PHOTO ) && data != null)
        {
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            try
            {
                //讀取照片，型態為Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
            }
            catch (FileNotFoundException e)
            {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ScalePic(Bitmap bitmap,int phone)
    {
        //縮放比例預設為1
        float mScale = 1 ;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if(bitmap.getWidth() > phone )
        {
            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
            imgPicture.setImageBitmap(mScaleBitmap);
        }
        else imgPicture.setImageBitmap(bitmap);
    }

    public void back(View v) {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}