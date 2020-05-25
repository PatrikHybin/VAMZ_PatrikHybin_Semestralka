package com.patrikhybin.nakupnyzoznam;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * aktivita, ktorá nám ukazuje list všetkých položiek, pridanie položky, prehladanie všetkých položiek, zvýraznenie položky, implementuje listenery pre rôzne typy onClick metód
 */
public class MainActivity extends AppCompatActivity implements MyViewAdapter.OnItemListener, View.OnClickListener, ItemQuantityDialog.ItemQuantityDialogListener {

    public static final int NEW_ITEM = 100;
    public static final int ITEM_NOTE = 200;
    public static ItemViewModel itemViewModel;
    public static final int request = 1;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyViewAdapter myAdapter;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton addbutton;
    private int quantity;
    private int position;

    /**
     * metóda, v ktorej sa nám vytvorí inicializuje viewModel, adapter, recyclerView, a priradia sa tlačidla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAdapter = new MyViewAdapter(this);

        //itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            /**
             * nastaví adaptéru položky a viewModel
             */
            public void onChanged(List<Item> items) {
                myAdapter.setItems(items);
                myAdapter.setItemViewModel(itemViewModel);

            }
        });


        bottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(this);

        addbutton = findViewById(R.id.add_item_to_list_button_id);
        addbutton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycle_view_id);
        recyclerView.setAdapter(myAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        deleteOnSwipe();

    }

    /**
     * metóda, ktorá nám spracuváva menu a searchView
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);

        SearchView searchView = findViewById(R.id.search_view_id);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            /**
             * metóda, ktorá sleduje či sa zmenil text v searchView
             * @param newText
             * @return
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                (recyclerView.getAdapter()).notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    /**
     * metóda, ktorá na základe tlačídla v menu vykoná prislúchajúcu úlohu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.sort_app_bar_id:
                Collections.sort(itemViewModel.getAllItems().getValue(), new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.getShop().compareTo(o2.getShop());
                    }
                });
                myAdapter.setItems(itemViewModel.getAllItems().getValue());
                myAdapter.notifyDataSetChanged();

                break;
            case R.id.highlight_app_bar_id:
                for (Item item1: itemViewModel.getAllItems().getValue()) {
                    if (item1.getChecked()) {
                        if (item1.getColor() == Color.YELLOW) {
                            item1.highlight(false);
                            item1.setColor(Color.LTGRAY);
                        } else {
                            item1.highlight(true);
                            item1.setColor(Color.YELLOW);
                        }
                        itemViewModel.update(item1);
                        myAdapter.notifyDataSetChanged();
                    }
                }
            default:
                break;
        }
        return true;
    }

    /**
     * metóda pozorujúca stačenie jednotlivých častí v aplikácii
     * @param v
     */
    @Override
    public void onClick(View v) {
        System.out.println(v.getId());
        switch (v.getId()) {
            case R.id.add_item_to_list_button_id:
                Intent intent = new Intent(MainActivity.this, AddItemToListActivity.class);
                startActivityForResult(intent, NEW_ITEM);
                break;
            //Navigation button id
            case -1:
                for (int i = 0; i < itemViewModel.getAllItems().getValue().size(); i++) {
                    if (myAdapter.getItemAt(i).getChecked()) {

                        if ((myAdapter.getItemAt(i).getBuying()) >= myAdapter.getItemAt(i).getCount()) {

                            ContextWrapper cw = new ContextWrapper(getApplicationContext());
                            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);;
                            File file = new File(directory, myAdapter.getItemAt(i).getId() + ".jpg");

                            itemViewModel.delete(myAdapter.getItemAt(i));
                            file.delete();
                        } else {
                            itemViewModel.getAllItems().getValue().get(i).setCount(myAdapter.getItemAt(i).getCount() - myAdapter.getItemAt(i).getBuying());
                        }
                    }
                    itemViewModel.update(itemViewModel.getAllItems().getValue().get(i));
                }
                myAdapter.notifyDataSetChanged();

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == NEW_ITEM) {
            int count = data.getIntExtra("Count",0);
            String nameItem = data.getStringExtra("Name");
            String shopItem = data.getStringExtra("Shop");
            Item item = new Item(nameItem, shopItem);
            item.setCount(count);
            itemViewModel.insert(item);
            myAdapter.notifyDataSetChanged();
        }
        if (resultCode == Activity.RESULT_OK && requestCode == ITEM_NOTE) {
            String description = data.getStringExtra("Description");
            String date = data.getStringExtra("Date");
            int position = data.getIntExtra("Position", 0);
            Item item = myAdapter.getItemAt(position);
            item.setDate(date);
            item.setDescription(description);
            itemViewModel.update(item);
        }
    }

    /**
     * metóda bez funkčnost
     * @param hasCapture
     */
    /*@Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/


    /**
     * metóda obsahujúca ItemTouchHelper, ktorý nám vymaže položku z listu ak ju posunieme doľava alebo doprava na obrazovke
     */
    public void deleteOnSwipe() {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Item item = myAdapter.getItemAt(viewHolder.getAdapterPosition());

                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);;
                File file = new File(directory, item.getId() + ".jpg");

                myAdapter.getItemAt(viewHolder.getAdapterPosition()).setBuying(0);
                itemViewModel.delete(item);
                myAdapter.notifyDataSetChanged();

                final boolean delete = file.delete();
            }
        }).attachToRecyclerView(recyclerView);
    }

    /**
     * metóda, ktorá nám otvorí položku, na ktorú sme klikli
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,ItemInfo.class);
        Item item = myAdapter.getItemAt(position);
        System.out.println(item);
        intent.putExtra("Item info", item);
        intent.putExtra("Position", position);
        startActivityForResult(intent, ITEM_NOTE);

    }

    /**
     * metóda, ktorá nám otvorí dialog, do ktorého zadáme koľko z danej položky chceme kúpiť
     * @param position
     */
    @Override
    public void onItemQuantityClick(int position) {
        this.position = position;
        ItemQuantityDialog itemQuantityDialog = new ItemQuantityDialog();
        itemQuantityDialog.show(getSupportFragmentManager(), "itemQuantityDialog");

    }

    /**
     * metóda, ktorá kontroluje či kvantita je typu int a následne updatne daný item
     * @param quantity
     */
    @Override
    public void applyQuantity(String quantity) {
        if (!quantity.equals("")) {
            this.quantity = Integer.parseInt(quantity);
            itemViewModel.getAllItems().getValue().get(position).setBuying(this.quantity);
            itemViewModel.update(itemViewModel.getAllItems().getValue().get(position));
            myAdapter.notifyDataSetChanged();
        }

    }



}