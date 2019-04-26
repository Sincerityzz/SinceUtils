package com.sincerity.sinceutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sincerity.utilslibrary.http.ResponseEntity;
import com.sincerity.utilslibrary.http.SVolley;

public class MainActivity extends AppCompatActivity {
    private Button btnRequest;
    private TextView tvResponse;
    private String url = "https://wanandroid.com/wxarticle/chapters/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRequest = findViewById(R.id.btn_request);
        tvResponse = findViewById(R.id.tvResponse);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SVolley.sendJsonRequest(url, null, new ResponseEntity() {
                    @Override
                    public void onSuccess(Object object) {
//                        JSONObject jsonObject = new JSONObject(object.toString())
                        tvResponse.setText(object.toString());
                    }

                    @Override
                    public void onFail(String errorString) {
                        tvResponse.setText(errorString);
                    }
                });
            }
        });
    }
}
