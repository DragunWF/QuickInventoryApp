package com.example.inventoryquickcount;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.inventoryquickcount.data.Item;
import com.example.inventoryquickcount.helpers.DatabaseHelper;
import com.example.inventoryquickcount.helpers.Utils;
import com.example.inventoryquickcount.services.ItemService;

public class AddEditItem extends AppCompatActivity {
    private EditText titleText, descriptionText;
    private Button backBtn, saveBtn, deleteBtn;
    private Spinner categorySpinner;

    private int viewedItemId;
    private boolean isEditForm;
    private String selectedCategory = "";

    private final int CHAR_LIMIT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        try {
            titleText = findViewById(R.id.titleText);
            descriptionText = findViewById(R.id.descriptionText);
            categorySpinner = findViewById(R.id.categorySpinner);

            backBtn = findViewById(R.id.backBtn);
            saveBtn = findViewById(R.id.saveBtn);
            deleteBtn = findViewById(R.id.deleteBtn);

            viewedItemId = getIntent().getIntExtra("itemId", -1);
            isEditForm = viewedItemId != 1;

            setClickListeners();
            setSpinner();
        } catch (Exception err) {
            err.printStackTrace();
            Utils.longToast(err.getMessage(), this);
        }
    }

    private void setClickListeners() {
        backBtn.setOnClickListener(v -> {
            finish();
        });
        saveBtn.setOnClickListener(v -> {
            String title = Utils.getText(titleText);
            String description = Utils.getText(descriptionText);

            if (title.isEmpty() || description.isEmpty()) {
                Utils.longToast("All fields are required", AddEditItem.this);
                return;
            }
            if (title.length() > CHAR_LIMIT || description.length() > CHAR_LIMIT) {
                Utils.longToast("Title and description must be less than or equal to " + CHAR_LIMIT + " characters", AddEditItem.this);
                return;
            }

            if (isEditForm) {
                Item item = DatabaseHelper.getItemBank().get(viewedItemId);
                item.setTitle(title);
                item.setDescription(description);
                item.setCategory(selectedCategory);
                ItemService.edit(item);
            } else {
                ItemService.add(new Item(title, description, selectedCategory));
                Utils.longToast("Item has been added", AddEditItem.this);
            }

            finish();
        });
        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditItem.this);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ItemService.delete(viewedItemId);
                    Utils.longToast("Items has been deleted!", AddEditItem.this);
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
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categories_array_form,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}