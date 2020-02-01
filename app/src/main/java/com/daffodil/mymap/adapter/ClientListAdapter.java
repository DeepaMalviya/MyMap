package com.daffodil.mymap.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daffodil.mymap.R;
import com.daffodil.mymap.activity.ClientListActivity;
import com.daffodil.mymap.model.ClientListModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Daffodil on 12/6/2019.
 */

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.MyViewHolder> {

    private List<ClientListModel> moviesList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        ImageView imageClientList;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.product_namee);
            imageClientList = view.findViewById(R.id.imageClientList);
            //genre = (TextView) view.findViewById(R.id.genre);
            //year = (TextView) view.findViewById(R.id.year);
        }
    }


    public ClientListAdapter(ClientListActivity clientListActivity, List<ClientListModel> moviesList) {
        this.moviesList = moviesList;
        this.context = clientListActivity;
    }
    public void updateData(List<ClientListModel> modelList) {
        moviesList.clear();
        moviesList.addAll(modelList);
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_client_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ClientListModel movie = moviesList.get(position);
        holder.title.setText(" " + movie.getName());

        Picasso.with(context).load(movie.getImage()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageClientList);

        //holder.genre.setText(movie.getSubjectName());
        // holder.year.setText(movie.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
