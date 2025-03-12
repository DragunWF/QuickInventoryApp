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
import android.widget.Spinner;

import com.example.inventoryquickcount.adapters.ItemAdapter;
import com.example.inventoryquickcount.helpers.DatabaseHelper;
import com.example.inventoryquickcount.helpers.Utils;
import com.example.inventoryquickcount.services.ItemService;

public class MainActivity extends AppCompatActivity {
    private Button clearBtn, addBtn;

    private RecyclerView itemRecycler;
    private ItemAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
            DatabaseHelper.addDummyData();

            categorySpinner = findViewById(R.id.itemCategorySpinner);
            itemRecycler = findViewById(R.id.itemRecycler);
            clearBtn = findViewById(R.id.clearBtn);
            addBtn = findViewById(R.id.addBtn);

            setClickListeners();
            setRecycler();
            setSpinner();
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

        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedCategory = adapter.getItem(pos).toString();
            }
        });
    }
}
