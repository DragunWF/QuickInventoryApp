package com.example.inventoryquickcount.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryquickcount.AddEditItem;
import com.example.inventoryquickcount.MainActivity;
import com.example.inventoryquickcount.R;
import com.example.inventoryquickcount.data.Item;
import com.example.inventoryquickcount.helpers.DatabaseHelper;
import com.example.inventoryquickcount.helpers.Utils;
import com.example.inventoryquickcount.services.ItemService;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> localDataSet;
    private Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText, descriptionText, categoryText;
        private final Button editBtn, deleteBtn;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            titleText = view.findViewById(R.id.titleText);
            descriptionText = view.findViewById(R.id.descriptionText);
            categoryText = view.findViewById(R.id.categoryText);

            editBtn = view.findViewById(R.id.editBtn);
            deleteBtn = view.findViewById(R.id.deleteBtn);
        }

        public TextView getTitleText() {
            return titleText;
        }

        public TextView getDescriptionText() {
            return descriptionText;
        }

        public TextView getCategoryText() {
            return categoryText;
        }

        public Button getEditBtn() {
            return editBtn;
        }

        public Button getDeleteBtn() {
            return deleteBtn;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public ItemAdapter(List<Item> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        // viewHolder.getTextView().setText(localDataSet[position]);
        Item item = localDataSet.get(position);
        viewHolder.getTitleText().setText(item.getTitle());
        viewHolder.getDescriptionText().setText(item.getDescription());
        viewHolder.getCategoryText().setText(item.getCategory());
        viewHolder.getEditBtn().setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditItem.class);
            intent.putExtra("itemId", item.getId());
            context.startActivity(intent);
        });
        viewHolder.getDeleteBtn().setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ItemService.clear();
                    Utils.longToast("Items have been cleared!", context);
                    updateDataSet();
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

    public void updateDataSet() {
        List<Item> items = DatabaseHelper.getItemBank().getAll();
        updateDataSet(items);
    }

    public void updateDataSet(List<Item> dataSet) {
        localDataSet.clear();
        for (Item item : dataSet) {
            localDataSet.add(item);
        }
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}