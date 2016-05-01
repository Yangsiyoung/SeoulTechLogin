package com.example.yangsiyoung.mystudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.List;

import butterknife.Bind;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.editStudentId)
    EditText editStudentId;

    @Bind(R.id.editPassword)
    EditText editPassword;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    Source source;

    final StringBuffer stringBuffer = new StringBuffer();
    public static final String url="https://portal.seoultech.ac.kr/user/seouolTechLogin.face";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(onClickListener);



    }

    class ConnectServer{
        OkHttpClient client = new OkHttpClient();

        public void run(String url, String studentId, String password){
            RequestBody formBody = new FormBody.Builder().add("userId",studentId).add("password",password).add("ssoLangKnd","ko").build();

            Request request = new Request.Builder().url(url).post(formBody).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "ConnectServer Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String result = response.body().string();
                    try {
                        source = new Source(result);
                        if(source.getNextElementByClass(0, "name") != null && source.getNextElementByClass(0,"type").getChildElements().get(1) != null && source.getNextElementByClass(0, "colle") != null){
                        Element elementName = source.getNextElementByClass(0, "name");
                        Element elementEnrollomentState = source.getNextElementByClass(0,"type").getChildElements().get(1);
                        Element elementDepartment = source.getNextElementByClass(0, "colle");


                            String userName = elementName.getContent().toString();
                            String userEnrollmentState = elementEnrollomentState.getContent().toString();
                            String userDepartment = elementDepartment.getContent().toString();

                            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                            intent.putExtra("userName", userName);
                            intent.putExtra("userEnrollmentState", userEnrollmentState);
                            intent.putExtra("userDepartment", userDepartment);

                            startActivity(intent);
                        } else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "학번과 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }



                    }catch (Exception e){
                        Log.d("error","Jericho error is "+ e.toString());
                    }

                }
            });
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConnectServer connectServer = new ConnectServer();

            connectServer.run(url,editStudentId.getText().toString(),editPassword.getText().toString());

        }
    };
}
