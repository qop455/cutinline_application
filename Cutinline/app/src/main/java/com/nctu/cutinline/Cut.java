package com.nctu.cutinline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jason on 2016/5/20.
 */
public class Cut extends Activity {
    private static final String TAG = "Cut";
    SharedPreferences prefs = null;

    TextView usernameTextView;
    TextView bonusTextView;
    TextView branchTextView;
    TextView addressTextView;
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String branch_name = "";
        String branch_address = "";
        prefs = getSharedPreferences("com.nctu.cutinline", MODE_PRIVATE);
        String username = prefs.getString("USERNAME", "Max");
        int branch_id = prefs.getInt("RESERVE_BRANCH", 598);
        int bonus = prefs.getInt("BONUS",0);

        BranchReaderDbHelper dbHelper = new BranchReaderDbHelper(Cut.this);
        Branch branch;
        branch = dbHelper.getBranchItem(branch_id);
        branch_name = branch.getBranch_name();
        branch_address = branch.getBranch_address();

        setContentView(R.layout.activity_cut);

        usernameTextView = (TextView) findViewById(R.id.cut_username_tv);
        bonusTextView = (TextView) findViewById(R.id.cut_bonus_tv);
        branchTextView = (TextView) findViewById(R.id.cut_branch_tv);
        addressTextView = (TextView) findViewById(R.id.cut_address_tv);
        backBtn = (Button) findViewById(R.id.cut_back_btn);
        usernameTextView.setText(username);
        bonusTextView.setText(String.valueOf(bonus));
        branchTextView.setText(branch_name);
        addressTextView.setText(branch_address);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Cut.this, Personal.class);
                startActivity(intent);
                Cut.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent();
            intent.setClass(Cut.this, Personal.class);
            startActivity(intent);
            Cut.this.finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
