package com.example.shaafi.blooddonate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shaafi on 31-Jan-16, 2016.
 */
public class ListAdapter extends ArrayAdapter {

    List list = new ArrayList();


    public ListAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(MemberInfo object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View row;

        row = convertView;

        ListHolder listHolder;

        if(row == null){

            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_display, parent, false);

            listHolder = new ListHolder();
            listHolder.tvFirstName = (TextView) row.findViewById(R.id.tvFirstName);
            listHolder.tvLastName = (TextView) row.findViewById(R.id.tvLastName);
            listHolder.tvAge = (TextView) row.findViewById(R.id.tvAge);
            listHolder.tvBlood = (TextView) row.findViewById(R.id.tvBloodGrp);
            listHolder.tvPhone = (TextView) row.findViewById(R.id.tvPhoneNum);
            listHolder.tvLocation = (TextView) row.findViewById(R.id.tvLocation);
            listHolder.tvActive = (TextView) row.findViewById(R.id.tvActive);

            row.setTag(listHolder);

        }

        else
        {
            listHolder = (ListHolder) row.getTag();
        }

        MemberInfo memberInfo = (MemberInfo) this.getItem(position);

        listHolder.tvFirstName.setText(memberInfo.getUserFirstName());
        listHolder.tvLastName.setText(memberInfo.getUserLastName());
        listHolder.tvBlood.setText(memberInfo.getUserBlood());
        listHolder.tvAge.setText(memberInfo.getUserAge());
        listHolder.tvPhone.setText(memberInfo.getUserPhone());
        listHolder.tvLocation.setText(memberInfo.getUserLocation());
        listHolder.tvActive.setText(memberInfo.getUserActive());



        return row;

    }

    static class ListHolder {

        TextView tvFirstName, tvLastName, tvAge, tvBlood, tvPhone, tvLocation, tvActive;

    }
}
