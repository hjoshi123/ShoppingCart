package com.test.ecommerceft;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.ecommerceft.room.items.Item;
import com.test.ecommerceft.room.items.ItemDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ItemDB mItemDB;
    private List<Item> mList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private List<Item> selected = new ArrayList<>();
    private String mPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");
        Bundle bundle = getIntent().getExtras();
        mPhoneNumber = bundle.getString("phone");
        Log.d("MainAc", mPhoneNumber);
        mRecyclerView = findViewById(R.id.products_list);

        mItemDB = Room.databaseBuilder(getApplicationContext(), ItemDB.class, "sample").build();
        new DatabaseAsync().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Item item = new Item();
            item.setName("Dairy");
            item.setPrice(10);
            item.setInventory(10);
            item.setVariant("10 gm");

            mItemDB.getItemDao().insert(item);

            Item item1 = new Item();
            item1.setName("KitKat");
            item1.setPrice(10);
            item1.setInventory(10);
            item1.setVariant("10 gm");

            mItemDB.getItemDao().insert(item1);

            Item item2 = new Item();
            item2.setName("Coke");
            item2.setPrice(20);
            item2.setInventory(10);
            item2.setVariant("10 gm");

            mItemDB.getItemDao().insert(item2);

            Item item3 = new Item();
            item3.setName("Pepsi");
            item3.setPrice(20);
            item3.setInventory(10);
            item3.setVariant("10 gm");

            mItemDB.getItemDao().insert(item3);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new QueryDB().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart) {
            ArrayList<Item> item1 = (ArrayList<Item>) selected;
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            Bundle b = new Bundle();
            b.putParcelableArrayList("cart", item1);
            intent.putExtras(b);
            intent.putExtra("phone", mPhoneNumber);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class QueryDB extends AsyncTask<Void, Void, Void> {
        private List<Item> newList = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            newList = mItemDB.getItemDao().getAllItems();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mList = newList;

            ItemsAdapter adapter = new ItemsAdapter(mList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
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
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemsAdapter.ViewHolder holder, int position) {
            final Item itm = item.get(position);
            holder.name.setText(itm.getName());
            holder.price.setText(String.valueOf(itm.getPrice()));
            holder.quantity.setText(itm.getVariant());
            holder.count.setText(String.valueOf(itm.getCount()));

            holder.add.setOnClickListener((v) -> {
                itm.setCount(itm.getCount() + 1);
                holder.count.setText(String.valueOf(itm.getCount()));
                if (!selected.contains(itm))
                    selected.add(itm);
            });

            holder.remove.setOnClickListener(v -> {
                if (!(itm.getCount() <= 0))
                    itm.setCount(itm.getCount() - 1);
                holder.count.setText(String.valueOf(itm.getCount()));
                if (itm.getCount() == 0)
                    selected.remove(itm);
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
