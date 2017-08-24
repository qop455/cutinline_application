package com.nctu.cutinline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jason on 2016/5/19.
 */
public class Personal extends Activity {
    private static final String TAG = "Personal";
    SharedPreferences prefs = null;
    TextView usernameTextView;
    TextView bonusTextView;
    Button reserveBtn;
    Button exchangeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("com.nctu.cutinline", MODE_PRIVATE);
        String name = prefs.getString("USERNAME", "Unknown");
        int bonus = prefs.getInt("BONUS", 0);
        final int reserve = prefs.getInt("RESERVE_BRANCH", 0);
        final int time = prefs.getInt("RESERVE_TIME", 0);

        Log.d(TAG,String.format("%s %s %s %s ",name,bonus,reserve,time));

        setContentView(R.layout.activity_personal);
        usernameTextView = (TextView) findViewById(R.id.info_name_tv);
        bonusTextView = (TextView) findViewById(R.id.info_bonus_tv);
        usernameTextView.setText(name);
        bonusTextView.setText(String.valueOf(bonus));
        reserveBtn = (Button) findViewById(R.id.reserve_btn);
        if (reserve > 0 && time > 0) {
            reserveBtn.setBackgroundResource(R.drawable.check);
        }
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reserve > 0 && time > 0) {
                    Intent intent = new Intent();
                    intent.setClass(Personal.this, Reserve.class);
                    startActivity(intent);
                    Personal.this.finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(Personal.this, BranchList.class);
                    startActivity(intent);
                    Personal.this.finish();
                }
            }
        });
        exchangeBtn = (Button) findViewById(R.id.bonus_btn);
        exchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Personal.this, Exchange.class);
                startActivity(intent);
                Personal.this.finish();
            }
        });
    }
}
