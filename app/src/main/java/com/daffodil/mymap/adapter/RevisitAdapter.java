package com.daffodil.mymap.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.daffodil.mymap.R;
import com.daffodil.mymap.activity.UpdateActivity;
import com.daffodil.mymap.model.ClientListModel;

import java.util.List;

/**
 * Created by Daffodil on 11/7/2019.
 */

public class RevisitAdapter implements ListAdapter {
    List<ClientListModel> arrayList;
    Context context;
    String user_id;
    public RevisitAdapter(Context context, List<ClientListModel> arrayList, String user_id) {
        this.arrayList=arrayList;
        this.context=context;
        this.user_id=user_id;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ClientListModel subjectData = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row_revisit, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, UpdateActivity.class);
                    intent.putExtra("Name",subjectData.getName());
                    intent.putExtra("Image",subjectData.getImage());
                    intent.putExtra("Client_id",subjectData.getClient_id());
                    intent.putExtra("Client_id",subjectData.getClient_id());
                    intent.putExtra("ClientListActivity","CustomAdapter");
                    intent.putExtra("user_id",user_id);
                   // Toast.makeText(context, ""+subjectData.SubjectName, Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                }
            });
            TextView tittle = convertView.findViewById(R.id.product_name);
            TextView imag = convertView.findViewById(R.id.list_image);
            tittle.setText(subjectData.getName());
            imag.setText("Update");

        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }


}
