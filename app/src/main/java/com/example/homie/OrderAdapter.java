package com.example.homie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context c;
    Ordermodel ordermodel;
    LayoutInflater inflater;
    ArrayList<Ordermodel> orderlist;

    public OrderAdapter(Context c, ArrayList<Ordermodel> orderlist) {
        this.c = c;
        this.orderlist = orderlist;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater=LayoutInflater.from(c);
        View view=inflater.inflate(R.layout.orderrow,parent,false);
        OrderAdapter.ViewHolder holder=new OrderAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, final int position) {
        holder.title.setText(orderlist.get(position).getTitle());
        holder.category.setText(orderlist.get(position).getCategory());
        holder.desc.setText(orderlist.get(position).getDesc_());
        holder.quantity.setText(orderlist.get(position).getQuan());
        holder.ordernumber.setText(orderlist.get(position).getOrderreqnumber());
        holder.days.setText(orderlist.get(position).getDays());
        holder.price.setText(orderlist.get(position).getPrice());
        holder.date.setText(orderlist.get(position).getDatee());
        Picasso.get()
                .load(orderlist.get(position).getImage())
                .into(holder.imageView);
        holder.Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c.getApplicationContext(),OrderLocation.class);
                intent.putExtra("langlat",orderlist.get(position).getLoc());
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,desc,category,days,price,date,quantity,ordernumber;
        ImageView imageView;
        Button Loc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.TVtitle);
            desc=(TextView) itemView.findViewById(R.id.TVdesc_);
            ordernumber=(TextView) itemView.findViewById(R.id.TVorderreqnum);
            quantity=(TextView) itemView.findViewById(R.id.TVQuan);
            category=(TextView) itemView.findViewById(R.id.TVcategory);
            days=(TextView) itemView.findViewById(R.id.TVdays);
            price=(TextView) itemView.findViewById(R.id.TVprice);
            date=(TextView) itemView.findViewById(R.id.TVdatee);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            Loc =(Button)itemView.findViewById(R.id.Loc);
        }
    }
}
