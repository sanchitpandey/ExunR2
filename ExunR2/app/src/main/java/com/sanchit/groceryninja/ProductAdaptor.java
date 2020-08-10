package com.sanchit.groceryninja;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.ProductViewHolder> {

    ArrayList<Product> list;
    Context context;

    public ProductAdaptor(ArrayList<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_card, parent, false);
        ProductViewHolder viewHolder = new ProductViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.itemView.setTag(list.get(position));
        final Product product = list.get(position);
        holder.name.setText(product.getName());
        holder.cost.setText("$"+product.getCost()+" per lb");
        if (!product.getPicURL().isEmpty()){
            Picasso.get().load(product.getPicURL()).into(holder.image);
        }
        holder.bshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Fruit.class);
                ArrayList<String> x = new ArrayList<>();
                x.add(product.getName());
                x.add(product.getCost());
                x.add(product.getDesc());
                x.add(product.getPicURL());
                x.add(product.getAvailable());
                i.putExtra("product", x);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Product product) {
        list.add(position, product);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Product product) {
        int position = list.indexOf(product);
        list.remove(position);
        notifyItemRemoved(position);
    }

    // View Holder
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, cost;
        ImageView image;
        LinearLayout bshop;

        ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            cost = itemView.findViewById(R.id.productCost);
            image = itemView.findViewById(R.id.productImage);
            bshop = itemView.findViewById(R.id.bShop);
        }
    }
}
