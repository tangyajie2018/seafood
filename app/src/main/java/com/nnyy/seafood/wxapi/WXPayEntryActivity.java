package com.nnyy.seafood.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nnyy.seafood.BaseActivity;
import com.nnyy.seafood.Constants;
import com.nnyy.seafood.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final String TAG = "wx";
    private IWXAPI api;
    private static final String APP_ID = Constants.WX_APPID;
    private static final String WXPAY_TOAC = "wxtoactivity";
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq baseReq) {
    }
    //微信返回的接口
    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("errcode",baseResp.errCode+"");
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            if (baseResp.errCode==0)
            {
                Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
            }
            else if (baseResp.errCode==-2)
            {
                Toast.makeText(this,"支付取消",Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }












}
