package com.jooyer.jooyerretrofits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Activity 中测试Retrofit
     */
    public void onActivityTest(View view) {
        startActivity(new Intent(MainActivity.this,DemoActivity.class));
    }

    /**
     * Fragment 中测试Retrofit
     */
    public void onFragmentTest(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.fl_container,new DemoFragment()).commit();
    }
}
