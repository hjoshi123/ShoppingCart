package com.test.ecommerceft;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static ItemDB mItemDB;
    private List<Item> mList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private List<Item> selected = new ArrayList<>();
    private String mPhoneNumber, mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        mPhoneNumber = Objects.requireNonNull(bundle).getString("phone");
        mName = Objects.requireNonNull(bundle.getString("name"));

        mRecyclerView = findViewById(R.id.products_list);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView navName = header.findViewById(R.id.display_name);
        TextView navPhone = header.findViewById(R.id.display_phone);
        navName.setText(mName);
        navPhone.setText(mPhoneNumber);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        mItemDB = Room.databaseBuilder(getApplicationContext(), ItemDB.class, "sample").build();

        // Database population should occur only once that is when the app is opened first
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean("firstTime", false)) {
            new DatabaseAsync().execute();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }
        new QueryDB().execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shopping) {
            Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
            intent.putExtra("phone", mPhoneNumber);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            drawer.closeDrawer(GravityCompat.START);
            moveTaskToBack(true);
        }
    }

    /**
     * Database seeding
     * Ideally done in a backend or a separate app to manage the items
     */
    private static class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            Integer hours = Integer.valueOf(sdf.format(new Date()));

            Item item = new Item();
            item.setName("Dairy Milk");
            if (hours >= 13 && hours <= 22)
                item.setPrice(10);
            else
                item.setPrice(5);
            item.setInventory(10);
            item.setVariant("10 gm");
            mItemDB.getItemDao().insert(item);

            Item item1 = new Item();
            item1.setName("KitKat");
            if (hours >= 13 && hours <= 22)
                item1.setPrice(10);
            else
                item1.setPrice(5);
            item1.setInventory(10);
            item1.setVariant("10 gm");
            mItemDB.getItemDao().insert(item1);

            Item item2 = new Item();
            item2.setName("Coke");
            if (hours >= 13 && hours <= 22)
                item2.setPrice(60);
            else
                item2.setPrice(50);
            item2.setInventory(5);
            item2.setVariant("1 L");
            mItemDB.getItemDao().insert(item2);

            Item item3 = new Item();
            item3.setName("Coke");
            if (hours >= 13 && hours <= 22)
                item3.setPrice(90);
            else
                item3.setPrice(50);
            item3.setInventory(5);
            item3.setVariant("2 L");
            mItemDB.getItemDao().insert(item3);

            Item item4 = new Item();
            item4.setName("Carrot");
            if (hours >= 13 && hours <= 22)
                item4.setPrice(20);
            else
                item4.setPrice(15);
            item4.setInventory(10);
            item4.setVariant("1 Kg");
            mItemDB.getItemDao().insert(item4);

            Item item5 = new Item();
            item5.setName("CauliFlower");
            if (hours >= 13 && hours <= 22)
                item5.setPrice(30);
            else
                item5.setPrice(15);
            item5.setInventory(3);
            item5.setVariant("1 Piece");
            mItemDB.getItemDao().insert(item5);

            return null;
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
            holder.price.setText("₹ " + String.valueOf(itm.getPrice()));
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
