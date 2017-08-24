package com.nctu.cutinline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jason on 2016/5/15.
 */
public class Login extends Activity {
    private static final String TAG = "Login";
    private static final String URL = "http://reserve.mybluemix.net/";
    private static final String PROJECT_NUMBER = "947587828981";
    SharedPreferences prefs = null;
    BranchReaderDbHelper dbHelper;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginBtn;
    Button registerBtn;
    TextView infoTextView;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = msg.getData().getString("response");
            switch (msg.what) {
                case ATTRIBUTES.ERROR:
                    infoTextView.setText(result);
                    break;
                case ATTRIBUTES.LOGIN:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int mStatus = jsonObject.getInt("STATUS");
                        String username = jsonObject.getString("USERNAME");
                        int bonus = jsonObject.getInt("BONUS");
                        int reserve = jsonObject.getInt("RESERVE");
                        int time = jsonObject.getInt("TIME");
                        prefs.edit().putString("USERNAME", username).apply();
                        prefs.edit().putInt("BONUS", bonus).apply();
                        prefs.edit().putInt("RESERVE_BRANCH", reserve).apply();
                        prefs.edit().putInt("RESERVE_TIME", time).apply();
                        if (mStatus == 1) {
                            Intent intent = new Intent();
                            intent.setClass(Login.this, Personal.class);
                            startActivity(intent);
                            Login.this.finish();
                        } else {
                            infoTextView.setText("Login failed!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        infoTextView.setText(e.toString());
                    }
                    break;
                case ATTRIBUTES.REGISTER:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int mStatus = jsonObject.getInt("STATUS");
                        String username = jsonObject.getString("USERNAME");
                        int bonus = jsonObject.getInt("BONUS");
                        prefs.edit().putString("USERNAME", username).apply();
                        prefs.edit().putInt("BONUS", bonus).apply();
                        if (mStatus == 1) {
                            prefs.edit().putInt("RESERVE_BRANCH", 0).apply();
                            prefs.edit().putInt("RESERVE_TIME", 0).apply();
                            Intent intent = new Intent();
                            intent.setClass(Login.this, Personal.class);
                            startActivity(intent);
                            Login.this.finish();
                        } else {
                            infoTextView.setText("Register failed!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        infoTextView.setText(e.toString());
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("com.nctu.cutinline", MODE_PRIVATE);
        dbHelper = new BranchReaderDbHelper(this);
        createView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstCreateDB", true) || dbHelper.getBranchCount() == 0) {
            new BranchFetcherTask().execute();
        } else {
            Log.d(TAG, "Not First run the app!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void createView() {
        setContentView(R.layout.activity_login);
        usernameEditText = (EditText) findViewById(R.id.username_et);
        passwordEditText = (EditText) findViewById(R.id.password_et);
        infoTextView = (TextView) findViewById(R.id.info_tv);
        loginBtn = (Button) findViewById(R.id.login_btn);
        registerBtn = (Button) findViewById(R.id.register_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();
                if ("".equals(name) || "".equals(pass)) {
                    infoTextView.setText("Error: Username or Password can't be empty!");
                    return;
                } else {
                    OkHttpThread mThread = new OkHttpThread();
                    mThread.setUsername(name);
                    mThread.setPassword(pass);
                    mThread.setType(ATTRIBUTES.LOGIN);
                    mThread.start();
                    infoTextView.setText("Login ing!");
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usernameEditText.getText().toString();
                String pass = passwordEditText.getText().toString();
                if ("".equals(name) || "".equals(pass)) {
                    infoTextView.setText("Error: Username or Password is empty!");
                    return;
                } else {
                    OkHttpThread mThread = new OkHttpThread();
                    mThread.setUsername(name);
                    mThread.setPassword(pass);
                    mThread.setType(ATTRIBUTES.REGISTER);
                    mThread.start();
                    infoTextView.setText("Register ing!");
                }
            }
        });
    }

    class OkHttpThread extends Thread {
        private String username = "";
        private String password = "";
        private int type = ATTRIBUTES.ERROR;

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Log.d(TAG, "HttpThread");
            GetExample example = new GetExample();
            String response = "";
            String urlFinal = "";
            switch (type) {
                case ATTRIBUTES.LOGIN:
                    urlFinal = URL + "login?id=" + username + "&pswd=" + password;
                    break;
                case ATTRIBUTES.REGISTER:
                    urlFinal = URL + "finish_register?id=" + username + "&pswd=" + password;
                    break;
            }

            Log.d(TAG, "final Url is :" + urlFinal);
            try {
                response = example.run(urlFinal);
                response = Html.fromHtml(response).toString();
                Log.d(TAG, response);
                Bundle bundle = new Bundle();
                bundle.putString("response", response); //OKResponse

                Message msg = new Message();
                switch (type) {
                    case ATTRIBUTES.LOGIN:
                        msg.what = ATTRIBUTES.LOGIN;
                        break;
                    case ATTRIBUTES.REGISTER:
                        msg.what = ATTRIBUTES.REGISTER;
                        break;
                }
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                Bundle bundle = new Bundle();
                bundle.putString("response", e.toString());
                Message msg = new Message();
                msg.what = ATTRIBUTES.ERROR;
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        }
    }

    class BranchFetcherTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "BranchFetcherTask");
            GetExample example = new GetExample();
            String response = "";
            String urlFinal = getResources().getString(R.string.esunbank_api_branch);
            Log.d(TAG, "Branch Url is:" + urlFinal);
            try {
                response = example.run(urlFinal);
//                Log.d(TAG, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("branches");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectBranch = jsonArray.getJSONObject(i);
                    int branch_id = jsonObjectBranch.getInt("branch_id");
                    String branch_name = jsonObjectBranch.getString("branch_name");
                    String branch_city = jsonObjectBranch.getString("branch_city");
                    String branch_district = jsonObjectBranch.getString("branch_district");
                    String branch_village = jsonObjectBranch.getString("branch_village");
                    String branch_address = jsonObjectBranch.getString("branch_address");
                    Branch branch = new Branch(branch_id, branch_name, branch_city, branch_district, branch_village, branch_address);
                    dbHelper.insertBranchData(branch);
                }
                Log.d(TAG, "Create Branch Data Successfully.");
                if (dbHelper.getBranchCount() > 0)
                    prefs.edit().putBoolean("firstCreateDB", false).commit();
            } catch (JSONException e) {
                e.printStackTrace();
                e.printStackTrace();
                Bundle bundle = new Bundle();
                bundle.putString("response", e.toString());
                Message msg = new Message();
                msg.what = ATTRIBUTES.ERROR;
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
