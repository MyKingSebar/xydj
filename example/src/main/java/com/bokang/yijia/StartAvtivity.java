package com.bokang.yijia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bokang.yijia.R;
import com.example.latte.activities.ProxyActivity;
import com.example.latte.delegates.LatteDelegate;

public class StartAvtivity extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent=new Intent(this,ExampleActivity.class);
        startActivity(intent);
        finish();
    }

}
