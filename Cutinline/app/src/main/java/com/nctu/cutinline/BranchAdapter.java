package com.nctu.cutinline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jason on 2016/5/19.
 */
public class BranchAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Branch> branchArrayList;
    private LayoutInflater layoutInflater;

    public BranchAdapter(Context context, ArrayList<Branch> list) {
        this.context = context;
        this.branchArrayList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return branchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return branchArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Branch branch = branchArrayList.get(position);
        int branch_id = branch.getBranch_id();
        String branch_name = branch.getBranch_name();
        String branch_city = branch.getBranch_city();
        String branch_district = branch.getBranch_district();
        String branch_village = branch.getBranch_village();
        String branch_address = branch.getBranch_address();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.branch_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewBranchName = (TextView) convertView.findViewById(R.id.branch_name_tv);
            viewHolder.textViewBranchLocation = (TextView) convertView.findViewById(R.id.branch_location_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewBranchName.setText(branch_name);
        viewHolder.textViewBranchLocation.setText(String.format("%s%s%s", branch_city, branch_district, branch_village));

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewBranchName;
        TextView textViewBranchLocation;
    }
}
