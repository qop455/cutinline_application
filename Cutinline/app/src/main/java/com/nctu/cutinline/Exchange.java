package com.nctu.cutinline;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jason on 2016/5/19.
 */
public class Exchange extends Activity {
    private static final String TAG = "Exchange";
    private static final String URL = "http://reserve.mybluemix.net/";
    SharedPreferences prefs = null;
    TextView usernameTextView;
    TextView bonusTextView;
    TextView infoTextView;
    EditText receiverEditText;
    EditText tradingEditText;
    Button okBtn;
    Button cancelBtn;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = msg.getData().getString("response");
            switch (msg.what) {
                case ATTRIBUTES.ERROR:
                    infoTextView.setText(result);
                    break;
                case ATTRIBUTES.EXCHANGE:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int mStatus = jsonObject.getInt("STATUS");
                        int bonus = jsonObject.getInt("BONUS");
                        int transaction = jsonObject.getInt("TRANSCTION");
                        String receiver = jsonObject.getString("GIVEID");
                        prefs.edit().putInt("BONUS", bonus).apply();
                        if (mStatus == 1){
                            infoTextView.setText(String.format("Success: Given %s for '%s' bonus!", receiver, String.valueOf(transaction)));
                            bonusTextView.setText(String.valueOf(bonus));
                            receiverEditText.setText("");
                            tradingEditText.setText("");
                        }else if (mStatus == 0){
                            infoTextView.setText("交易金額不合法！");
                        }else if(mStatus == 2){
                            infoTextView.setText("餘額不足！");
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
        final String name = prefs.getString("USERNAME", "Unknown");
        int bonus = prefs.getInt("BONUS", 0);
        setContentView(R.layout.activity_exchange);
        usernameTextView = (TextView) findViewById(R.id.exchange_name_tv);
        bonusTextView = (TextView) findViewById(R.id.exchange_bonus_tv);
        infoTextView = (TextView) findViewById(R.id.exchange_info_tv);
        receiverEditText = (EditText) findViewById(R.id.exchange_receiver_et);
        tradingEditText = (EditText) findViewById(R.id.exchange_trading_et);
        usernameTextView.setText(name);
        bonusTextView.setText(String.valueOf(bonus));
        okBtn = (Button) findViewById(R.id.exchange_ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiver = receiverEditText.getText().toString();
                String bonus = tradingEditText.getText().toString();
                if ("".equals(receiver) || "".equals(bonus)) {
                    infoTextView.setText("Error: Receiver or Bonus can't be empty!");
                    return;
                } else {
                    OkHttpThread mThread = new OkHttpThread();
                    mThread.setUsername(name);
                    mThread.setReceiver(receiver);
                    mThread.setBonus(bonus);
                    mThread.start();
                    infoTextView.setText("Trade ing!");
                }
            }
        });
        cancelBtn = (Button) findViewById(R.id.exchange_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Exchange.this, Personal.class);
                startActivity(intent);
                Exchange.this.finish();
            }
        });
    }

    class OkHttpThread extends Thread {
        private String username = "";
        private String receiver = "";
        private String bonus = "";

        public void setUsername(String username) {
            this.username = username;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public void setBonus(String bonus) {
            this.bonus = bonus;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Log.d(TAG, "HttpThread");
            GetExample example = new GetExample();
            String response = "";
            String urlFinal = URL + "transaction?id=" + username + "&giveid=" + receiver + "&bonus=" + bonus;

            Log.d(TAG, "final Url is :" + urlFinal);
            try {
                response = example.run(urlFinal);
                response = Html.fromHtml(response).toString();
                Log.d(TAG, response);
                Bundle bundle = new Bundle();
                bundle.putString("response", response); //OKResponse

                Message msg = new Message();
                msg.what = ATTRIBUTES.EXCHANGE;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent();
            intent.setClass(Exchange.this, Personal.class);
            startActivity(intent);
            Exchange.this.finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
