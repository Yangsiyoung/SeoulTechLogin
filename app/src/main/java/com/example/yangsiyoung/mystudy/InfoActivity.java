package com.example.yangsiyoung.mystudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {
    @Bind(R.id.txtUserInfo)
    TextView txtUserInfo;
    String userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String userName = intent.getExtras().get("userName").toString();
        String userEnrollmentState = intent.getExtras().get("userEnrollmentState").toString();
        String userDepartment = intent.getExtras().get("userDepartment").toString();

        userInfo = userName + "님 \n" + userEnrollmentState + " \n" + userDepartment;

        txtUserInfo.setText(userInfo);

    }
}
