package com.nnyy.seafood;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private WebView webView;
//    private String homeUrl="http://ff.leanote.top/mobile/";
    private String homeUrl= "http://wx.bjdglt.com/mobile/";
    private String CookieUrl="wx.bjdglt.com/mobile";
//private String homeUrl="http://wx.bjdglt.com/mobile/index.php?m=user&origin=app&openid=123123123&token=33333";
//    private ImageView backImage;
    private TextView bactText;
    private ImageView titlebackImage;
    private String ECS_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        webView = findViewById(R.id.webview);
//        backImage=findViewById(R.id.back_image);
//        backImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                onBackPressed();
////                new ShareAction(MainActivity.this).withText("aaaaa")
////                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
////                        .setCallback(shareListener).open();
//                UMShareAPI.get(getApplicationContext()).getPlatformInfo(MainActivity.this,SHARE_MEDIA.WEIXIN,authListener);
//            }
//        });
        titlebackImage=findViewById(R.id.back_icon);
        bactText=findViewById(R.id.back_text);
        bactText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        titlebackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setUpWebview();
        HttpUtils.getInstall().setLister(new HttpUtils.HttpFinish() {
            @Override
            public void onLoginfinish(int resultCode, final String ID) {
                if(resultCode==0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ECS_ID=ID;
                            setCookie(ECS_ID);
                            webView.goBack();
                        }
                    });
                }
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String event){
        webView.loadUrl(skipUrl);
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,"成功了",Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,"失败"+t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,"取消了",Toast.LENGTH_LONG).show();

        }
    };

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//            Toast.makeText(MainActivity.this, "成功了", Toast.LENGTH_LONG).show();
            Log.d(TAG,data.get("uid"));
            Log.d(TAG,data.get("name"));
            Log.d(TAG,data.get("iconurl"));
            HttpUtils.getInstall().wechatLogin(data);
//            HttpUtils.getInstall().getWechatOrder("aaaaaa","11214545","aaaa","1");
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(MainActivity.this, "失败：" + t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };



    private void setUpWebview() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);//设置支持javascript
        // 排版适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // 网页内容的宽度是否可大于WebView控件的宽度
        webSettings.setLoadWithOverviewMode(false);
        webView.addJavascriptInterface(new appjs(this),"appjs");
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        setCookie();
        webView.loadUrl(homeUrl);
    }
    private void setCookie(String ecid){
        CookieManager cookieManager = CookieManager.getInstance();
        String CookieStr = cookieManager.getCookie(CookieUrl);
        Log.d("Cookies", "Cookies = " + CookieStr);
        if(TextUtils.isEmpty(CookieStr)){
            cookieManager.setCookie("ECS_ID", ecid);
        }else {
            List<HttpCookie> cookieList =new ArrayList<>();
            String[] cookies=CookieStr.split(";");
            for (String cooki:cookies ) {
                String[] keyValue=cooki.split("=");
                HttpCookie bean=new HttpCookie(keyValue[0],keyValue[1]);
                cookieList.add(bean);
            }
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除旧的[可以省略]
            for (int i = 0; i < cookieList.size(); i++) {
                HttpCookie cookie = cookieList.get(i);
                String value = cookie.getName() + "=" + cookie.getValue();
                cookieManager.setCookie(CookieUrl, value);
            }
            cookieManager.setCookie(CookieUrl, "ECS_ID="+ecid);
        }
        CookieSyncManager.getInstance().sync();
        String CookieStr1 = cookieManager.getCookie(CookieUrl);
        Log.d("Cookies", "Cookies = " + CookieStr1);
    }
    private void setCookie(){
        CookieManager cookieManager = CookieManager.getInstance();
        String CookieStr = cookieManager.getCookie(CookieUrl);
        Log.d("Cookies", "Cookies = " + CookieStr);
        if(TextUtils.isEmpty(CookieStr)){
            cookieManager.setCookie(CookieUrl, "nnyyappload=1");
        }else {
            if(CookieStr.contains("nnyyappload"))return;
            List<HttpCookie> cookieList =new ArrayList<>();
            String[] cookies=CookieStr.split(";");
            for (String cooki:cookies ) {
                String[] keyValue=cooki.split("=");
                if(keyValue[0].equals("ECS_ID")){
                    ECS_ID=keyValue[1];
                }
                HttpCookie bean=new HttpCookie(keyValue[0],keyValue[1]);
                cookieList.add(bean);
            }
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除旧的[可以省略]
            for (int i = 0; i < cookieList.size(); i++) {
                HttpCookie cookie = cookieList.get(i);
                String value = cookie.getName() + "=" + cookie.getValue();
                cookieManager.setCookie(CookieUrl, value);
            }
            cookieManager.setCookie(CookieUrl, "ECS_ID="+ECS_ID);
            cookieManager.setCookie(CookieUrl, "nnyyappload=1");
        }
        CookieSyncManager.getInstance().sync();
        String CookieStr1 = cookieManager.getCookie(CookieUrl);
        Log.d("Cookiesafter", "Cookies = " + CookieStr1);
    }

    public class MyWebChromeClient extends WebChromeClient {

        private View mXCustomView;
        private CustomViewCallback mXCustomViewCallback;


        /**
         * 判断是否是全屏
         */
        public boolean inCustomView() {
            return (mXCustomView != null);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.d(TAG,newProgress+"..newProgress");
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {

        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            openFileChooser(valueCallback);
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            openFileChooser(valueCallback);
        }

        //For Android  >= 4.1
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            openFileChooser(valueCallback);
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();
//                ivFinish.setVisibility(View.VISIBLE);

            //退出网页
        } else {
//            webView.loadUrl("about:blank");
            finish();
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(CookieUrl);
            Log.d("Cookies", "url"+url+"\nCookies = " + CookieStr);
            hideWaitingDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            backImage.setVisibility(url.equals(homeUrl)?View.GONE:View.VISIBLE);
            Log.d(TAG,url);
            titlebackImage.setVisibility(url.equals(homeUrl)?View.GONE:View.VISIBLE);
            bactText.setVisibility(url.equals(homeUrl)?View.GONE:View.VISIBLE);
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
            showWaitingDialog("加载中...");
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

//    private void wxPay(){
//        WXAPIFactory.createWXAPI(MainActivity.this, null);
        IWXAPI msgApi = WXAPIFactory.createWXAPI(MainActivity.this, null);
//        msgApi.registerApp(result.getAppid());
//        StringUtil.appidwxpay = result.getAppid();
//        PayReq requestwx = new PayReq();
//        requestwx.appId = result.getAppid();
//        requestwx.partnerId = result.getPartnerid();
//        requestwx.prepayId = result.getPrepayid();
//        requestwx.packageValue = "Sign=WXPay";
//        requestwx.nonceStr = result.getNoncestr();
//        requestwx.timeStamp = result.getTimestamp();
//        requestwx.sign = result.getSign();
//        requestwx.extData = "balance";
//        msgApi.sendReq(requestwx);
//    }

    private void boxVi(){
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
//        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
//        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
//        request.setBizModel(model);
//        model.setOutTradeNo(System.currentTimeMills());
//        model.setTotalAmount("88.88");
//        model.setSubject("Iphone6 16G");
//        AlipayTradePrecreateResponse response = alipayClient.execute(request);
//        System.out.print(response.getBody());
//        System.out.print(response.getQrCode());
    }




    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.d(TAG,resultStatus+".."+payResult.getResult());
                    if (TextUtils.equals(resultStatus, "9000")) {
                        webView.loadUrl(skipUrl);
//                        Intent intent = new Intent(BalanceActivity.this, BuyPointSucessActivity.class);
//                        intent.putExtra("payno", payno);
////                        intent.putExtra("iid", iid);
//                        intent.putExtra("point", point);
//                        intent.putExtra("tag", "balance");
//                        startActivity(intent);
//                        finish();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        Toast.makeText(MainActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    // js通信接口
    public class appjs {

        private Context context;

        public appjs(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void wechatLogin() {     //把所有图片的url保存在ArrayList<String>中
//            listimg.add(img);
//            HttpUtils.getInstall().wechatLogin(data);
            Log.d(TAG,"wechatLogin");
            UMShareAPI.get(getApplicationContext()).getPlatformInfo(MainActivity.this,SHARE_MEDIA.WEIXIN,authListener);
        }

        @android.webkit.JavascriptInterface
        public void doShare() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UMWeb  web = new UMWeb(webView.getUrl());
                    web.setTitle("年年有鱼生鲜-"+webView.getTitle());//标题
//            web.setThumb(thumb);  //缩略图
            web.setDescription("海外直采 包机直达");//描述
                    new ShareAction(MainActivity.this).withMedia(web)
                            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                            .setCallback(shareListener).open();
                }
            });

            //
//            Intent intent = new Intent();
//            intent.putExtra("url", img);
//            intent.putExtra("urllist", listimg);
//            intent.setClass(context, ShowBigImageActivity.class);
//            context.startActivity(intent);
        }

        @android.webkit.JavascriptInterface
        public void doPay(int panType,String body,String skip_url) {
            Log.d(TAG,"textClick.setOnClickListener\n"+panType+"\n"+body+"\n"+skip_url);
            skipUrl=skip_url;
            if(panType==2){
                alipay(body);
            }else {
                WechatReq req=JsonMananger.jsonToBean(body, WechatReq.class);
                wxPay(req);
            }
//            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(activity.getEditText().getApplicationWindowToken(), 0);
        }
    }

    private String skipUrl;

    private void wxPay(WechatReq req){
//        WXAPIFactory.createWXAPI(MainActivity.this, null);
        IWXAPI msgApi = WXAPIFactory.createWXAPI(MainActivity.this, null);
        msgApi.registerApp(req.getAppid());
//        StringUtil.appidwxpay = result.getAppid();
        PayReq requestwx = new PayReq();
        requestwx.appId = req.getAppid();
        requestwx.partnerId = req.getPartnerid();
        requestwx.prepayId = req.getPrepayid();
        requestwx.packageValue = "Sign=WXPay";
        requestwx.nonceStr = req.getNoncestr();
        requestwx.timeStamp = req.getTimestamp();
        requestwx.sign = req.getSign();
//        requestwx.extData = "balance";
        msgApi.sendReq(requestwx);
    }

    private void alipay(final String restring){
        Runnable authRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, URLDecoder.decode(restring));
                PayTask alipay = new PayTask(MainActivity.this);
                Map<String, String> result = alipay.payV2(URLDecoder.decode(restring), true);
//                Map<String, String> result = alipay.payV2(restring, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }
}
