package com.example.inventoryquickcount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.inventoryquickcount.adapters.ItemAdapter;
import com.example.inventoryquickcount.data.Item;
import com.example.inventoryquickcount.helpers.DatabaseHelper;
import com.example.inventoryquickcount.helpers.Utils;
import com.example.inventoryquickcount.services.ItemService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button clearBtn, addBtn;

    private RecyclerView itemRecycler;
    private ItemAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private SearchView searchBar;
    private Spinner categorySpinner;
    private String selectedCategory = "";

    @Override
    protected void onResume() {
        super.onResume();
        itemAdapter.updateDataSet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            DatabaseHelper.initialize(this);
            DatabaseHelper.clear();
            DatabaseHelper.addDummyData();

            searchBar = findViewById(R.id.searchBar);
            categorySpinner = findViewById(R.id.itemCategorySpinner);
            itemRecycler = findViewById(R.id.itemRecycler);
            clearBtn = findViewById(R.id.clearBtn);
            addBtn = findViewById(R.id.addBtn);

            setClickListeners();
            setRecycler();
            setSpinner();
            setSearch();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void setClickListeners() {
        clearBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ItemService.clear();
                    Utils.longToast("Items have been cleared!", MainActivity.this);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                }
            });
            builder.setMessage("Are you sure you want to clear all items!");

            builder.create().show();
        });
        addBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddEditItem.class));
        });
    }

    private void setRecycler() {
        itemRecycler.setHasFixedSize(false);

        itemAdapter = new ItemAdapter(DatabaseHelper.getItemBank().getAll(), this);
        itemRecycler.setAdapter(itemAdapter);

        layoutManager = new LinearLayoutManager(this);
        itemRecycler.setLayoutManager(layoutManager);
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCategory = adapterView.getItemAtPosition(i).toString();
                if (selectedCategory.equals("Any")) {
                    itemAdapter.updateDataSet();
                    return;
                }

                List<Item> results = new ArrayList<>();
                for (Item item : DatabaseHelper.getItemBank().getAll()) {
                    if (item.getCategory().equals(selectedCategory)) {
                        results.add(item);
                    }
                }
                itemAdapter.updateDataSet(results);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setSearch() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                update(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                update(s);
                return false;
            }

            public void update(String query) {
                List<Item> results = new ArrayList<>();

                query = query.toLowerCase();
                for (Item item : DatabaseHelper.getItemBank().getAll()) {
                    String itemTitle = item.getTitle().toLowerCase();
                    if (itemTitle.contains(query)) {
                        results.add(item);
                    }
                }

                itemAdapter.updateDataSet(results);
            }
        });
    }
}
