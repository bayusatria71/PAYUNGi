package com.example.jphone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class BankAdapter extends BaseExpandableListAdapter {

    Context c;
    String[] descriptions;
    String[] bankNames;
    int[] imageLogos;

    public BankAdapter(Context c, String[] bankNames, String[] descriptions, int[] imageLogos)
    {
        this.c = c;
        this.bankNames = bankNames;
        this.descriptions = descriptions;
        this.imageLogos = imageLogos;
    }

    @Override
    public int getGroupCount() {
        return bankNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return bankNames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return descriptions[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String bank = (String) getGroup(groupPosition);
        int image = imageLogos[groupPosition];

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bank_list, parent, false);
        }

        TextView bankName = convertView.findViewById(R.id.bankName);
        ImageView bankLogo = convertView.findViewById(R.id.bankLogo);
        bankName.setText(bank);
        bankLogo.setImageResource(image);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String desc = descriptions[groupPosition];

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bank_description, parent, false);
        }

        TextView description = convertView.findViewById(R.id.bankDescription);
        description.setText(desc);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
