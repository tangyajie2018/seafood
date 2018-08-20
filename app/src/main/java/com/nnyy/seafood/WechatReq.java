package com.nnyy.seafood;

import java.io.Serializable;

public class WechatReq implements Serializable {
//    {"appid":"wx56005f539ef506f2","partnerid":"1510976901","prepayid":"wx2021314843640043fcc2d6a90327905549","timestamp":"1534771908","noncestr":"nRLcxtF5UfQEEdhn","package":"Sign=WXPay","sign":"7770358AAD9247517A8DF45210922BCB"}
    private String appid;
    private String partnerid;
    private String prepayid;
    private String timestamp;
    private String noncestr;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
