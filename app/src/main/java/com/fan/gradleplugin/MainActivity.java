package com.fan.gradleplugin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity<T> extends AppCompatActivity {
    private String hello = "hhh";

    T t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

//        new GradleSingleton();
//        new Test();
    }


    /**
     * 成员方法
     *
     * @param n
     * @return
     */
    Class<? extends T> m(int n) {
        return null;
    }

    /**
     * 泛型方法
     *
     * @param n
     * @param <T>
     * @return
     */
    static <T> Class<? extends T> m2(int n) {
        return null;
    }

}