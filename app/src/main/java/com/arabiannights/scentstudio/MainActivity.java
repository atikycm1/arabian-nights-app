package com.arabiannights.scentstudio;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.ValueCallback;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.content.ContentValues;
import android.provider.MediaStore;
import java.io.OutputStream;

public class MainActivity extends Activity {
  private static final int FILE_PICKER_REQUEST=7301;
  private WebView web;
  private ValueCallback<Uri[]> pendingFileCallback;
  @Override public void onCreate(Bundle state){
    super.onCreate(state);
    web=new WebView(this);
    web.setWebViewClient(new WebViewClient());
    web.setWebChromeClient(new WebChromeClient(){
      @Override public boolean onShowFileChooser(WebView w,ValueCallback<Uri[]> callback,FileChooserParams params){
        if(pendingFileCallback!=null)pendingFileCallback.onReceiveValue(null);
        pendingFileCallback=callback;
        try{startActivityForResult(params.createIntent(),FILE_PICKER_REQUEST);return true;}
        catch(Exception error){pendingFileCallback=null;return false;}
      }
    });
    WebSettings settings=web.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setAllowFileAccess(true);
    settings.setDefaultTextEncodingName("UTF-8");
    web.addJavascriptInterface(new BackupBridge(),"AndroidBackup");
    web.addJavascriptInterface(new ThemeBridge(),"AndroidTheme");
    getWindow().setStatusBarColor(Color.rgb(13,23,20));
    getWindow().setNavigationBarColor(Color.rgb(13,23,20));
    setContentView(web);
    web.loadUrl("file:///android_asset/index.html");
  }
  @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){
    super.onActivityResult(requestCode,resultCode,data);
    if(requestCode==FILE_PICKER_REQUEST&&pendingFileCallback!=null){
      pendingFileCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,data));
      pendingFileCallback=null;
    }
  }
  public class ThemeBridge{
    @JavascriptInterface public void setTheme(String theme){
      runOnUiThread(()->{
        boolean light="light".equals(theme);
        int color=light?Color.rgb(247,247,241):Color.rgb(13,23,20);
        getWindow().setStatusBarColor(color);
        getWindow().setNavigationBarColor(color);
        int flags=getWindow().getDecorView().getSystemUiVisibility();
        int contrast=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        getWindow().getDecorView().setSystemUiVisibility(light?(flags|contrast):(flags&~contrast));
      });
    }
  }
  public class BackupBridge{
    @JavascriptInterface public void saveJson(String json){
      try{
        ContentValues values=new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME,"arabian-nights-backup-"+System.currentTimeMillis()+".json");
        values.put(MediaStore.Downloads.MIME_TYPE,"application/json");
        values.put(MediaStore.Downloads.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS);
        Uri uri=getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI,values);
        if(uri==null)return;
        try(OutputStream out=getContentResolver().openOutputStream(uri)){if(out!=null)out.write(json.getBytes("UTF-8"));}
      }catch(Exception ignored){}
    }
  }
  @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
}
