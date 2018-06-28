package com.test.ecommerceft;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.ecommerceft.room.cart.Cart;
import com.test.ecommerceft.room.cart.CartDB;
import com.test.ecommerceft.room.items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    private static ArrayList<Item> items;
    private RecyclerView mRecyclerView;
    private static String mPhoneNumber;
    private static CartDB mCartDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Cart");
        items = Objects.requireNonNull(this.getIntent().getExtras()).getParcelableArrayList("cart");
        mPhoneNumber = Objects.requireNonNull(this.getIntent().getExtras().getString("phone"));

        mRecyclerView = findViewById(R.id.cart_list);
        mCartDb = Room.databaseBuilder(getApplicationContext(), CartDB.class, "sample-db").build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAdapter adapter = new ItemsAdapter(items);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.done) {
            new InsertCartItem().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    private static class InsertCartItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (Item i: items) {
                Cart c = new Cart();
                c.setPhoneNumber(mPhoneNumber);
                c.setName(i.getName());
                c.setCount(i.getCount());
                c.setInventory(i.getInventory());
                c.setPrice(i.getPrice());
                c.setVariant(i.getVariant());

                mCartDb.getCartDao().insert(c);
            }
            return null;
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
        private List<Item> item;

        ItemsAdapter(List<Item> itemList) {
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
            final Item itm = item.get(position);
            holder.name.setText(itm.getName());
            holder.price.setText(String.valueOf(itm.getPrice()));
            holder.quantity.setText(itm.getVariant());
            holder.count.setText(String.valueOf(itm.getCount()));

            holder.add.setOnClickListener(v -> {
                itm.setCount(itm.getCount() + 1);
                holder.count.setText(String.valueOf(itm.getCount()));
            });

            holder.remove.setOnClickListener(v -> {
                if (!(itm.getCount() <= 0))
                    itm.setCount(itm.getCount() - 1);
                if (itm.getCount() == 0) {
                    item.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                } else holder.count.setText(String.valueOf(itm.getCount()));
            });

        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, price, quantity, count;
            private ImageView add, remove;


            public ViewHolder(View itemView) {
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
