package com.example.homie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myitemAdapter  extends RecyclerView.Adapter<myitemAdapter.ViewHolder>{

    Context c;
    myitemModel myitemmodel;
    LayoutInflater inflater;
    ArrayList<myitemModel> itemlist;

    public myitemAdapter(Context c, ArrayList<myitemModel> itemlist) {
        this.c = c;
        this.itemlist = itemlist;
    }

    @NonNull
    @Override
    public myitemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater=LayoutInflater.from(c);
        View view=inflater.inflate(R.layout.itemrow,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myitemAdapter.ViewHolder holder, final int position) {
        holder.title.setText(itemlist.get(position).getTitle());
        holder.category.setText(itemlist.get(position).getCategory());
        holder.days.setText(itemlist.get(position).getDays());
        holder.price.setText(itemlist.get(position).getPrice());
        holder.price.append(" RS/-");
        holder.date.setText(itemlist.get(position).getDatee());
        Picasso.get()
                .load(itemlist.get(position).getImage())
                .into(holder.imageView);
        holder.ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c,OrderRequest.class);
                intent.putExtra("title",itemlist.get(position).getTitle());
                intent.putExtra("Image",itemlist.get(position).getImage());
                intent.putExtra("Visi",itemlist.get(position).getVisibility());
                intent.putExtra("Cate",itemlist.get(position).getCategory());
                intent.putExtra("Days",itemlist.get(position).getDays());
                intent.putExtra("Price",itemlist.get(position).getPrice());
                intent.putExtra("Number",itemlist.get(position).getNumber());
                intent.putExtra("Desc",itemlist.get(position).getDesc_());
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,category,days,price,date;
        ImageView imageView;
        Button ordernow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.TVtitle);
            category=(TextView) itemView.findViewById(R.id.TVcategory);
            days=(TextView) itemView.findViewById(R.id.TVdays);
            price=(TextView) itemView.findViewById(R.id.TVprice);
            date=(TextView) itemView.findViewById(R.id.TVdatee);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            ordernow=(Button)itemView.findViewById(R.id.Order);
        }
    }
}
