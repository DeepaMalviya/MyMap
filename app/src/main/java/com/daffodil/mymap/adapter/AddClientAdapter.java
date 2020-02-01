package com.daffodil.mymap.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daffodil.mymap.R;
import com.daffodil.mymap.activity.AddClientActivity;
import com.daffodil.mymap.model.ClientListModel;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Daffodil on 1/7/2020.
 */

public class AddClientAdapter extends RecyclerView.Adapter<AddClientAdapter.MyViewHolder> {
    private static final String TAG = "FollowAdapter";
    List<ClientListModel> clientListModels = new ArrayList<>();
    Context context;
    String user_id;


    public AddClientAdapter(AddClientActivity addClientActivity, List<ClientListModel> modelList, String user_id) {
        this.context = addClientActivity;
        this.clientListModels = modelList;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public AddClientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddClientAdapter.MyViewHolder holder, int position) {
        ClientListModel listModel = clientListModels.get(position);
       Log.e(TAG, "onBindViewHolder: listModel.getName()" + listModel.getName());
        holder.tittle.setText(listModel.getName());
          holder.list_image.setText("Update");
        Log.e(TAG, "onBindViewHolder: listModel.getImage()======"+listModel.getImage() );
        Picasso.with(context).load(listModel.getImage()).into(holder.imageH);

    }

    @Override
    public int getItemCount() {
        return clientListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tittle, list_image;
        ImageView imageH;

        public MyViewHolder(View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.title);
            imageH = itemView.findViewById(R.id.imageClientList);
            list_image = itemView.findViewById(R.id.list_image);


        }
    }
}
