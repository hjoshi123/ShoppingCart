package com.test.ecommerceft;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.ecommerceft.room.Item;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ArrayList<Item> items;
    private RecyclerView mRecyclerView;
    private List<Item> selected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setTitle("My Cart");
        items = this.getIntent().getExtras().getParcelableArrayList("cart");

        mRecyclerView = findViewById(R.id.cart_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter adapter = new ItemsAdapter(items);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private List<Item> item;

        ItemsAdapter(List<Item> itemList) {
            item = new ArrayList<>();
            selected = new ArrayList<>();
            this.item = itemList;
        }

        @NonNull
        @Override
        public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new ItemsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, int position) {
            final Item itm = item.get(position);
            holder.name.setText(itm.getName());
            holder.price.setText(String.valueOf(itm.getPrice()));
            holder.quantity.setText(itm.getVariant());
            holder.count.setText(String.valueOf(itm.getCount()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selected.contains(itm)) {
                        selected.remove(itm);
                        unhighlightView(holder);
                    } else {
                        selected.add(itm);
                        highlightView(holder);
                    }
                }

                private void highlightView(ItemsAdapter.ViewHolder holder) {
                    holder.itemView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                            android.R.color.holo_green_light));
                }

                private void unhighlightView(ItemsAdapter.ViewHolder holder) {
                    holder.itemView.setBackgroundColor
                            (ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
                }
            });
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, price, quantity, count;
            public ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.from_name);
                price = itemView.findViewById(R.id.price_text);
                quantity = itemView.findViewById(R.id.weight_text);
                count = itemView.findViewById(R.id.cart_product_quantity_tv);
            }
        }
    }

}
