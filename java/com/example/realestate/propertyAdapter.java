package com.example.realestate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class propertyAdapter extends RecyclerView.Adapter<propertyAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<ProjectModel> propertyList;
    private String phone;

    public void filterList(ArrayList<ProjectModel> filteredList) {
        propertyList = filteredList;
        notifyDataSetChanged();
    }

    public propertyAdapter(Context context, ArrayList<ProjectModel> propertyList, String phone) {
        this.context = context;
        this.propertyList = propertyList;
        this.phone = phone;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProjectModel model = propertyList.get(position);
        Picasso.get().load(model.getPropertyImage()).placeholder(R.drawable.img3).into(holder.itemImage);
        holder.itemHeadline.setText(model.getHeadline());
        holder.itemDescription.setText(model.getSurface());
        holder.itemPrice.setText(model.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, singleProperty.class);
                intent.putExtra("singleid", model.getid());
                intent.putExtra("singleImage", model.getPropertyImage());
                intent.putExtra("singleHeadline", model.getHeadline());
                intent.putExtra("singlePrice", model.getPrice());
                intent.putExtra("singleSurface", model.getSurface());
                intent.putExtra("singleDiscription", model.getDiscription());
                intent.putExtra("singleRooms", model.getRooms());
                intent.putExtra("singleLocation", model.getLocation());
                intent.putExtra("singlePin", model.getPin());
                intent.putExtra("singlePof", model.getPof());
                intent.putExtra("singleSeller", model.getSeller());
                intent.putExtra("singleSellerPhone", model.getSellerPhone());
                intent.putExtra("phone", phone);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemHeadline, itemDescription, itemPrice;
        ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemHeadline = itemView.findViewById(R.id.itemHeadLine);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
