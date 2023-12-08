package com.example.test_activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private WebView webview;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置渲染模板
        setContentView(R.layout.activity_main);

        // 获取 webview 实例
        webview = findViewById(R.id.webView);
        WebSettings webSetting = webview.getSettings();
        // 设置与Js交互的权限
        webSetting.setJavaScriptEnabled(true);
        // 插入包含 jsBridge 方法的对象，对象命名为 android
        webview.addJavascriptInterface(new JsInterface(webview) , "android");

        this.initJsAlert();
        this.initUrlChange();

        webview.loadUrl("http://10.253.36.51:5173");

        // 获取 button 实例
        button = findViewById(R.id.button);
        // 给 button 绑定点击事件，事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EvaluateJs.callJsFunction(webview,"callByAndroid", "客户端吊起Js方法");
                webview.loadUrl("javascript:returnResult(123)");
            }
        });
    }


    private void initJsAlert(){
        webview.setWebChromeClient(new WebChromeClient() {
            // 重写 onJsAlert 方法
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                result.confirm();
                Log.e("onJsAlert", message);
                try {
                    JSONObject resObj = new JSONObject(message);
                    Log.e("onJsAlert-key", resObj.getString("key"));
                    Log.e("onJsAlert-value",resObj.getString("value"));
                    Log.e("onJsAlert-callback", resObj.getString("callback"));
                    String callback = resObj.getString("callback");
                    EvaluateJs.callJsFunction(webview, callback, "来自onJsAlert的回调");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private void initUrlChange(){
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String urlString) {
                if (urlString.startsWith("native://")) {
                    Log.e("urlChange", urlString);
                    Map<String, String> resObj = MainActivity.this.getUrlQueryObj(urlString);
                    Log.e("urlChange-key", resObj.get("key"));
                    Log.e("urlChange-value",resObj.get("value"));
                    Log.e("urlChange-callback", resObj.get("callback"));
                    String callback = resObj.get("callback");
                    EvaluateJs.callJsFunction(webview, callback, "来自urlChange的回调");
                    return true;
                }
                return super.shouldOverrideUrlLoading(webview, urlString);
            }
        });
    }

    public static Map<String, String> getUrlQueryObj(String url){
        Map<String, String> map = new HashMap<String, String>();
        try {
            final String charset = "utf-8";
            url = URLDecoder.decode(url, charset);
            if (url.indexOf('?') != -1) {
                final String contents = url.substring(url.indexOf('?') + 1);
                String[] keyValues = contents.split("&");
                for (int i = 0; i < keyValues.length; i++) {
                    String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
                    String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}

class JsInterface{
    private WebView webView;

    // 实例化对象时传入 WebView 实例
    public JsInterface(WebView _webView){
        webView = _webView;
    }

    @JavascriptInterface
    public void jsFunction(String name,String data, String callback){
        Log.e("jsFunction-name", name);
        Log.e("jsFunction-data", data);
        Log.e("jsFunction-callback", callback);
        webView.post(new Runnable() {
            @Override
            public void run() {
                EvaluateJs.callJsFunction(webView,"callByAndroid", "来自jsFunction的回调");
            }
        });
    }

    @JavascriptInterface
    public void jsFunction2(String value){
        Log.e("jsFunction2-value", value);
    }

}

class EvaluateJs {
    public static void callJsFunction(WebView webView, String funcName, String value){
        String jsCode = String.format("window.%s('%s')",funcName, value);
        webView.evaluateJavascript(jsCode,new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                // 此处为JS返回的结果
                Log.e("js 函数返回值", value);
            }
        });
    }
}