package com.nctu.cutinline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jason on 2016/5/19.
 */
public class BranchList extends Activity {
    private static final String TAG = "BranchList";
    ListView listView;
    BranchAdapter branchAdapter;
    ArrayList<Branch> arrayList;
    BranchReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.branch_list);
        listView = (ListView)findViewById(R.id.branch_list_lv);
        dbHelper = new BranchReaderDbHelper(BranchList.this);
        arrayList = dbHelper.getAllBranch();
        branchAdapter = new BranchAdapter(BranchList.this,arrayList);
        listView.setAdapter(branchAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                Branch branch = arrayList.get(position);
                bundle.putInt("branch_id",branch.getBranch_id());
                bundle.putString("branch_name",branch.getBranch_name());
                bundle.putString("branch_address",branch.getBranch_address());
                intent.setClass(BranchList.this, Reserve.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent();
            intent.setClass(BranchList.this, Personal.class);
            startActivity(intent);
            BranchList.this.finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
