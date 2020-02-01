package com.daffodil.mymap.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daffodil.mymap.R;
import com.daffodil.mymap.activity.AddFollowupActivity;
import com.daffodil.mymap.activity.RevisitActivity;
import com.daffodil.mymap.model.ClientListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Daffodil on 1/7/2020.
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.MyViewHolder> {
    private static final String TAG = "FollowAdapter";
    List<ClientListModel> clientListModels = new ArrayList<>();
    Context context;

    public FollowAdapter(AddFollowupActivity addFollowupActivity, List<ClientListModel> clientListModels) {
        this.context = addFollowupActivity;
        this.clientListModels = clientListModels;

    }

   public FollowAdapter(RevisitActivity revisitActivity, List<ClientListModel> clientListModels) {
      //  this.context = revisitActivity;
        this.clientListModels = clientListModels;
   }

    @NonNull
    @Override
    public FollowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.MyViewHolder holder, int position) {
        ClientListModel listModel = clientListModels.get(position);
        // holder.title.setText(movie.getTitle());
        //  holder.genre.setText(movie.getGenre());
        // holder.year.setText(movie.getYear());
        Log.e(TAG, "onBindViewHolder: listModel.getName()"+listModel.getName() );
        holder.tittle.setText(listModel.getName());
        //   holder.imag.setText("Update");
        Picasso.with(context).load(listModel.getImage()).into(holder.imageH);

    }

    @Override
    public int getItemCount() {
        return clientListModels.size();
    }

    public void updateData(List<ClientListModel> modelList) {
        clientListModels.clear();
        clientListModels.addAll(modelList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tittle;
        ImageView imageH;

        public MyViewHolder(View itemView) {
            super(itemView);
            tittle = itemView.findViewById(R.id.product_name_follow);
            imageH = itemView.findViewById(R.id.imageH);
            //TextView imag = itemView.findViewById(R.id.list_image);


        }
    }
}
