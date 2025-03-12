package com.example.inventoryquickcount.services;

import android.view.Display;

import com.example.inventoryquickcount.data.Item;
import com.example.inventoryquickcount.helpers.DatabaseHelper;
import com.example.inventoryquickcount.helpers.ModelBank;

public class ItemService {
    public static void add(Item item) {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        bank.add(item);
    }

    public static void edit(Item item) {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        bank.update(item);
    }

    public static void delete(int id) {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        bank.delete(id);
    }

    public static void clear() {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        bank.clear();
    }
}
