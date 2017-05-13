package com.baidu.lining.displayadapter.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.lining.displayadapter.R;
import com.baidu.lining.displayadapter.base.BaseFragment;

import butterknife.BindView;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WXTextObject;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static cn.smssdk.SMSSDK.getSupportedCountries;
import static cn.smssdk.SMSSDK.getVerificationCode;
import static cn.smssdk.SMSSDK.submitVerificationCode;

/**
 * Created by Administrator on 2017/4/27.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    String TAG = "debugli";
    //控制按钮样式是否改变
    private boolean tag = true;
    //每次验证请求需要间隔60S
    private int i = 60;
    private String code;
    private String phone;

    @BindView(R.id.weixin_login)
    public ImageView weixin_login;
    @BindView(R.id.qq_login)
    public ImageView qq_login;
    @BindView(R.id.login_et)
    public EditText login_et;
    @BindView(R.id.getcode_et)
    public EditText getcode_et;
    @BindView(R.id.getcode_bt)
    public Button bt;
    @BindView(R.id.register_bt)
    public Button regist_bt;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    //客户端验证成功，可以进行注册,返回校验的手机和国家代码phone/country
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    //获取验证码成功
                    //  Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //返回支持发送验证码的国家列表
                    //  Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_register;
    }

    public void initData() {
        code = getcode_et.getText().toString();

        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Message msg = new Message();
                        msg.arg1 = 0;
                        msg.obj = data;
                        handler.sendMessage(msg);
                        Log.d(TAG, "提交验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Message msg = new Message();
                        //获取验证码成功
                        msg.arg1 = 1;
                        msg.obj = "获取验证码成功";
                        handler.sendMessage(msg);
                        Log.d(TAG, "获取验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        Message msg = new Message();
                        //返回支持发送验证码的国家列表
                        msg.arg1 = 2;
                        msg.obj = "返回支持发送验证码的国家列表";
                        handler.sendMessage(msg);
                        Log.d(TAG, "返回支持发送验证码的国家列表");
                    }
                } else {
                    Message msg = new Message();
                    //返回支持发送验证码的国家列表
                    msg.arg1 = 3;
                    msg.obj = "验证失败";
                    handler.sendMessage(msg);
                    Log.d(TAG, "验证失败");
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }


    public void initView() {

        bt.setOnClickListener(this);
        regist_bt.setOnClickListener(this);
        weixin_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getcode_bt:
                phone = login_et.getText().toString();
                if (isMobileNO(phone)) {
                    bt.setClickable(true);
                    changeBtnGetCode();
                    getSupportedCountries();
                    getVerificationCode("86", phone);
                } else {
                    //手机号格式有误
                    Toast.makeText(activity, "手机号格式错误，请检查", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_bt:
                //填写了验证码，进行验证
                submitVerificationCode("86", phone, code);
                break;
            case R.id.weixin_login:
                WXTextObject textObject = new WXTextObject();
                textObject.text = "你好";
                WXMediaMessage msg = new WXMediaMessage();

                break;
        }
    }

    /*
 * 改变按钮样式
 * */
    private void changeBtnGetCode() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (activity == null) {
                            break;
                        }

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bt.setText("获取验证码(" + i + ")");
                                bt.setClickable(false);
                                bt.setBackgroundResource(R.color.press_code);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;

                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bt.setText("获取验证码");
                            bt.setClickable(true);
                            bt.setBackgroundResource(R.color.press_false_code);
                        }
                    });
                }
            }
        };
        thread.start();
    }

    private boolean isMobileNO(String phone) {
       /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.matches(telRegex);
    }
}
