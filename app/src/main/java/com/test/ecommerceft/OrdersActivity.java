package com.test.ecommerceft;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.ecommerceft.room.cart.Cart;
import com.test.ecommerceft.room.cart.CartDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdersActivity extends AppCompatActivity {
    private String mPhoneNumber;
    private CartDB mCartDB;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getSupportActionBar().setTitle("Previous Orders");
        mPhoneNumber = Objects.requireNonNull(this.getIntent().getExtras()).getString("phone");

        mRecyclerView = findViewById(R.id.orders_list);
        mCartDB = Room.databaseBuilder(getApplicationContext(), CartDB.class, "sample-db").build();
        new QueryOrders().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class QueryOrders extends AsyncTask<Void, Void, Void> {
        private List<Cart> items;
        @Override
        protected Void doInBackground(Void... voids) {
            items = mCartDB.getCartDao().getAllItems(mPhoneNumber);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
            ItemsAdapter adapter = new ItemsAdapter(items);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private List<Cart> item;

        ItemsAdapter(List<Cart> itemList) {
            item = new ArrayList<>();
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
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, final int position) {
            final Cart itm = item.get(position);
            holder.name.setText(itm.getName());
            holder.price.setText("₹ " + String.valueOf(itm.getPrice()));
            holder.quantity.setText(itm.getVariant());
            holder.count.setText(String.valueOf(itm.getCount()));
            holder.add.setVisibility(View.GONE);
            holder.remove.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, price, quantity, count;
            private ImageView add, remove;

            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.from_name);
                price = itemView.findViewById(R.id.price_text);
                quantity = itemView.findViewById(R.id.weight_text);
                count = itemView.findViewById(R.id.cart_product_quantity_tv);
                add = itemView.findViewById(R.id.cart_plus_img);
                remove = itemView.findViewById(R.id.cart_minus_img);
            }
        }
    }

}
