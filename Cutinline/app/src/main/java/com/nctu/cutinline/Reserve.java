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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jason on 2016/5/20.
 */
public class Reserve extends Activity {
    private static final String TAG = "Reserve";
    private static final String URL_RESERVE = "http://reserve.mybluemix.net/reserve";
    private static final String URL_CHECKIN = "http://reserve.mybluemix.net/checkin";
    private static final String URL_CUT = "http://reserve.mybluemix.net/cutinline";
    private static final String URL_CANCEL = "http://reserve.mybluemix.net/dreserve";
    int super_bid = 0;
    int reserve_time = 0;
    int activityStatus = 0;

    SharedPreferences prefs = null;
    LinearLayout timeLinearLayout;
    TextView cidTextView;
    TextView bNameTextView;
    TextView addressTextView;
    TextView timeTextView;
    TextView infoTextView;
    EditText hourEditText;
    EditText mimEditText;
    Button reserveBtn;
    Button cutInLineBtn;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = msg.getData().getString("response");
            switch (msg.what) {
                case ATTRIBUTES.ERROR:
                    infoTextView.setText(result);
                    break;
                case ATTRIBUTES.RESERVEONE:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int mStatus = jsonObject.getInt("status");
                        reserve_time = jsonObject.getInt("number");
                        int time = jsonObject.getInt("time");
                        if (mStatus == 1 && reserve_time > 0) { //成功
                            timeTextView.setText("您想要預約這個時間嗎:");
                            hourEditText.setText(String.valueOf(reserve_time / 100));
                            mimEditText.setText(String.valueOf(reserve_time % 100));
                            hourEditText.setFocusable(false);
                            mimEditText.setFocusable(false);
                            reserveBtn.setBackgroundResource(R.drawable.ok);
                            cutInLineBtn.setBackgroundResource(R.drawable.cancel);
                            infoTextView.setText("Good!");
                            activityStatus = 1;
                        } else if (mStatus == 2) {
                            infoTextView.setText("現在非上班時間!");
                        } else if (mStatus == 3) {
                            infoTextView.setText("不合法的時間，請輸入現在時間點以後，且上班時間!");
                        } else {
                            infoTextView.setText("你可能已經預約!");
                            Intent intent = new Intent();
                            intent.setClass(Reserve.this, Personal.class);
                            startActivity(intent);
                            Reserve.this.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        infoTextView.setText("Error: Failed!");
                    }
                    break;
                case ATTRIBUTES.RESERVETWO:
                    try {
                        Log.d(TAG, "ATTRIBUTES.RESERVETWO");
                        JSONObject jsonObject = new JSONObject(result);
                        int mStatus = jsonObject.getInt("status");
                        reserve_time = jsonObject.getInt("number");
                        int time = jsonObject.getInt("time");
                        if (mStatus == 0) {
                            infoTextView.setText("Error: !");
                        } else if (mStatus == 1) {
                            if (reserve_time > 0) {
                                int bid = super_bid;
                                prefs.edit().putInt("RESERVE_BRANCH",bid ).apply();
                                prefs.edit().putInt("RESERVE_TIME", reserve_time).apply();
                                timeTextView.setText("您預約到的時間為:");
                                hourEditText.setText(String.valueOf(reserve_time / 100));
                                mimEditText.setText(String.valueOf(reserve_time % 100));
                                hourEditText.setFocusable(false);
                                mimEditText.setFocusable(false);
                                reserveBtn.setBackgroundResource(R.drawable.check);
                                cutInLineBtn.setBackgroundResource(R.drawable.cancel);
                                infoTextView.setText("Success!");
                                activityStatus = 2;
                            } else {
                                infoTextView.setText("Error: Failed!");
                            }
                        } else if (mStatus == 2) {
                            infoTextView.setText("現在非上班時間!");
                        } else if (mStatus == 3) {
                            infoTextView.setText("不合法的時間，請輸入現在時間點以後，且上班時間!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        infoTextView.setText("Error: Failed!");
                    }
                    break;
                case ATTRIBUTES.RESERVETHREE:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int mStatus = jsonObject.getInt("status");
                        if (mStatus == 0) {
                            timeTextView.setText("別傻了，你沒有預約");
                            prefs.edit().putInt("RESERVE_BRANCH", 0).apply();
                            prefs.edit().putInt("RESERVE_TIME", 0).apply();
                        } else if (mStatus == 1) {
                            timeTextView.setText("報到成功，請耐心等候");
                            prefs.edit().putInt("RESERVE_BRANCH", 0).apply();
                            prefs.edit().putInt("RESERVE_TIME", 0).apply();
                        } else {
                            infoTextView.setText("奇怪的錯誤!");
                            prefs.edit().putInt("RESERVE_BRANCH", 0).apply();
                            prefs.edit().putInt("RESERVE_TIME", 0).apply();
                        }
                        hourEditText.setText("0");
                        mimEditText.setText("0");
                        timeLinearLayout.setVisibility(View.INVISIBLE);
                        reserveBtn.setBackgroundResource(R.drawable.back);
                        cutInLineBtn.setVisibility(View.INVISIBLE);
                        activityStatus = 3;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        infoTextView.setText("Error: Failed!");
                    }
                    break;
                case ATTRIBUTES.CUT:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int mStatus = jsonObject.getInt("STATUS");
                        int bonus = jsonObject.getInt("USERBONUS");
                        if (mStatus == 0) {
                            infoTextView.setText(String.format("您的紅利不足，餘額：%s，至少需要10紅利！", bonus));
                            prefs.edit().putInt("BONUS", bonus).apply();
                        } else if (mStatus == 1) {
                            infoTextView.setText("Success!");
                            int bid = super_bid;
                            prefs.edit().putInt("BONUS", bonus).apply();
                            prefs.edit().putInt("RESERVE_BRANCH",bid ).apply();

                            Intent intent = new Intent();
                            intent.setClass(Reserve.this, Cut.class);
                            startActivity(intent);
                            Reserve.this.finish();
                        } else if (mStatus == 2) {
                            infoTextView.setText("現在非上班時間喔!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        infoTextView.setText("Error: Failed!");
                    }
                    break;
                case ATTRIBUTES.CANCEL:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int mStatus = jsonObject.getInt("STATUS");
                        if (mStatus == 1) {
                            prefs.edit().putInt("RESERVE_BRANCH", 0).apply();
                            prefs.edit().putInt("RESERVE_TIME", 0).apply();
                            Intent intent = new Intent();
                            intent.setClass(Reserve.this, Personal.class);
                            startActivity(intent);
                            Reserve.this.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        infoTextView.setText("Error: Failed!");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("com.nctu.cutinline", MODE_PRIVATE);
        final String username = prefs.getString("USERNAME", "Unknown");
        int reserve_branch = prefs.getInt("RESERVE_BRANCH", 0);
        int reserve_time = prefs.getInt("RESERVE_TIME", 0);


        final int branch_id;
        String branch_name;
        String branch_address;

        Bundle bundle;
        if (reserve_branch > 0 && reserve_time > 0) {
            this.reserve_time = reserve_time;
            branch_id = reserve_branch;
            super_bid = reserve_branch;
            //search db
            BranchReaderDbHelper dbHelper = new BranchReaderDbHelper(Reserve.this);
            Branch branch;
            branch = dbHelper.getBranchItem(branch_id);
            branch_name = branch.getBranch_name();
            branch_address = branch.getBranch_address();
        } else {
            bundle = this.getIntent().getExtras();
            branch_id = bundle.getInt("branch_id");
            branch_name = bundle.getString("branch_name");
            branch_address = bundle.getString("branch_address");
            super_bid = branch_id;
        }
        setContentView(R.layout.activity_reserve);
        cidTextView = (TextView) findViewById(R.id.reserve_cid_tv);
        bNameTextView = (TextView) findViewById(R.id.reserve_bid_tv);
        addressTextView = (TextView) findViewById(R.id.reserve_address_tv);
        timeTextView = (TextView) findViewById(R.id.reserve_ask_tv);
        infoTextView = (TextView) findViewById(R.id.reserve_info_tv);
        hourEditText = (EditText) findViewById(R.id.reserve_hour_et);
        mimEditText = (EditText) findViewById(R.id.reserve_mim_et);
        reserveBtn = (Button) findViewById(R.id.reserve_reserve_btn);
        cutInLineBtn = (Button) findViewById(R.id.reserve_cutinline_btn);
        timeLinearLayout = (LinearLayout) findViewById(R.id.reserve_ll);
        cidTextView.setText(username);
        bNameTextView.setText(branch_name);
        addressTextView.setText(branch_address);
        timeTextView.setText("What time do you want to reserve:");
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = hourEditText.getText().toString();
                String mim = mimEditText.getText().toString();
                if ("".equals(hour) || "".equals(mim)) {
                    infoTextView.setText("Error: Hour or mim or sec can't be empty!");
                } else {
                    if (activityStatus == 3) {
                        Intent intent = new Intent();
                        intent.setClass(Reserve.this, Personal.class);
                        startActivity(intent);
                        Reserve.this.finish();
                    }
                    OkHttpThread mThread = new OkHttpThread();
                    mThread.setAID(username);
                    mThread.setBID(branch_id);
                    mThread.setType(ATTRIBUTES.RESERVE);
                    int temp= Integer.parseInt(hour)*100 + Integer.parseInt(mim);
                    String time =String.valueOf(temp);
                    mThread.setTime(time);
                    mThread.start();
                    if (activityStatus == 0 || activityStatus == 1) {
                        infoTextView.setText("Reserve ing!");
                    } else if (activityStatus == 2) {
                        infoTextView.setText("Check in ing!");
                    } else {
                    }
                }
            }
        });
        cutInLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityStatus == 0) {
                    OkHttpThread mThread = new OkHttpThread();
                    mThread.setAID(username);
                    mThread.setBID(branch_id);
                    mThread.setType(ATTRIBUTES.CUT);
                    mThread.start();
                } else if (activityStatus == 1) {
                    Intent intent = new Intent();
                    intent.setClass(Reserve.this, BranchList.class);
                    startActivity(intent);
                    Reserve.this.finish();
                } else if (activityStatus == 2) {
                    infoTextView.setText("取消預約中！");
                    OkHttpThread mThread = new OkHttpThread();
                    mThread.setAID(username);
                    mThread.setBID(branch_id);
                    mThread.setType(ATTRIBUTES.CANCEL);
                    mThread.start();
                }
            }
        });

        if (reserve_branch > 0 && reserve_time > 0) {

            timeTextView.setText("您預約到的時間為:");
            hourEditText.setText(String.valueOf(this.reserve_time / 100));
            mimEditText.setText(String.valueOf(this.reserve_time % 100));
            hourEditText.setFocusable(false);
            mimEditText.setFocusable(false);
            reserveBtn.setBackgroundResource(R.drawable.check);
            cutInLineBtn.setBackgroundResource(R.drawable.cancel);
            infoTextView.setText("Success!");
            activityStatus = 2;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent();
            intent.setClass(Reserve.this, BranchList.class);
            startActivity(intent);
            Reserve.this.finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    class OkHttpThread extends Thread {
        private int bid = 0;
        private String aid = "";
        private String time = "";
        private int type = ATTRIBUTES.RESERVE;

        public void setBID(int bid) {
            this.bid = bid;
        }

        public void setAID(String aid) {
            this.aid = aid;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            Log.d(TAG, "HttpThread");
            String response = "";
            String json = "";
            String url = "";
            if (type == ATTRIBUTES.RESERVE) {
                if (activityStatus == 0) {
                    url = URL_RESERVE;
                    json = "{\"aid\":\"" + aid + "\",\"bid\":\"" + String.valueOf(bid) + "\",\"time\":\"" + time + "\"" + ",\"ask\":\"1\"}";
                } else if (activityStatus == 1) {
                    url = URL_RESERVE;
                    json = "{\"aid\":\"" + aid + "\",\"bid\":\"" + String.valueOf(bid) + "\",\"time\":\"" + time + "\"" + ",\"ask\":\"2\"}";
                } else {
                    url = URL_CHECKIN;
                    json = "{\"user\":\"" + aid + "\"}";
                }
            } else if (type == ATTRIBUTES.CUT) {
                url = URL_CUT;
                json = "?id=" + aid + "&branch=" + bid;
            } else if (type == ATTRIBUTES.CANCEL) {
                url = URL_CANCEL;
                json = "{\"aid\":\"" + aid + "\",\"bid\":\"" + String.valueOf(bid) + "\"}";
            } else {
                interrupt();
            }
            Log.d(TAG, url);
            Log.d(TAG, json);
            try {
                Message msg = new Message();
                if (type == ATTRIBUTES.RESERVE) {
                    PostExample example = new PostExample();
                    response = example.post(url, json);
                    if (activityStatus == 0) {
                        msg.what = ATTRIBUTES.RESERVEONE;
                    } else if (activityStatus == 1) {
                        msg.what = ATTRIBUTES.RESERVETWO;
                    } else {
                        msg.what = ATTRIBUTES.RESERVETHREE;
                    }
                } else if (type == ATTRIBUTES.CUT) {
                    String urlFinal = url + json;
                    GetExample getExample = new GetExample();
                    response = getExample.run(urlFinal);
                    msg.what = ATTRIBUTES.CUT;
                } else if (type == ATTRIBUTES.CANCEL) {
                    PostExample example = new PostExample();
                    response = example.post(url, json);
                    msg.what = ATTRIBUTES.CANCEL;
                } else {
                    interrupt();
                }
                response = Html.fromHtml(response).toString();
                Log.d(TAG, response);
                Bundle bundle = new Bundle();
                bundle.putString("response", response);

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
}
