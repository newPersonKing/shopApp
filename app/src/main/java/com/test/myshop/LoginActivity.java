package com.test.myshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import com.test.myshop.bean.User;
import com.test.myshop.http.OkHttpHelper;
import com.test.myshop.http.SpotsCallBack;
import com.test.myshop.msg.LoginRespMsg;
import com.test.myshop.utils.DESUtil;
import com.test.myshop.utils.ToastUtils;
import com.test.myshop.widget.CNiaoToolBar;
import com.test.myshop.widget.ClearEditText;


public class LoginActivity extends AppCompatActivity {



    @ViewInject(R.id.etxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;
    @ViewInject(R.id.iv_back)
    private ImageView iv_back;


    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        initToolBar();
    }


    private void initToolBar(){


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.btn_login)
    public void login(View view){

        String phone = mEtxtPhone.getText().toString().trim();
        if(!phone.equals("13111222333")){
            ToastUtils.show(this, "请输入正确的手机号码");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if(!pwd.equals("123456")){
            ToastUtils.show(this,"请输入正确的密码");
            return;
        }


        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));

        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

               CniaoApplication application =  CniaoApplication.getInstance();
;              User user=new User();
               user.setUsername("斗破苍穹");
               user.setMobi(mEtxtPhone.getText().toString());
               application.putUser(user, userLoginRespMsg.getToken());

                if(application.getIntent() == null){
                    setResult(RESULT_OK);
                    finish();
                }else{

                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();

                }



            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });





    }



}
