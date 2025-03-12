package com.example.inventoryquickcount.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inventoryquickcount.data.Item;
import com.example.inventoryquickcount.services.ItemService;

public class DatabaseHelper {
    private static final String FILE_KEY = "db";

    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    private static ModelBank<Item> itemBank;

    public static void initialize(Context context) {
        sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        itemBank = new ModelBank<>(sharedPref, editor, "items", Item.class);
    }

    public static ModelBank<Item> getItemBank() {
        return itemBank;
    }

    public static void addDummyData() {
        if (itemBank.getAll().isEmpty()) {
            String[] categories = { "Neccessities", "Supplies", "Materials", "Tools", "Products", "Others" };

            ItemService.add(new Item("Reusable Water Bottle", "Stay hydrated on the go with this eco-friendly reusable water bottle. Made from BPA-free materials, it keeps your beverages fresh and at the perfect temperature.", categories[0]));
            ItemService.add(new Item("Stationery Set", "This stationery set includes pens, pencils, erasers, and notebooks, making it perfect for students and professionals alike.", categories[1]));
            ItemService.add(new Item("Fabric Roll", "High-quality fabric roll suitable for tailoring, crafting, and upholstery projects. Durable and available in various colors and patterns.", categories[2]));
            ItemService.add(new Item("Multi-Tool Kit", "A compact and versatile multi-tool featuring pliers, screwdrivers, and a knife. Ideal for DIY projects and emergency fixes.", categories[3]));
            ItemService.add(new Item("Organic Soap Bar", "Handmade organic soap with natural ingredients to keep your skin fresh and moisturized. Perfect for daily skincare routines.", categories[4]));
            ItemService.add(new Item("Mystery Surprise Box", "A fun surprise box containing random items from different categories. A great gift idea for those who love surprises.", categories[5]));
        }
    }

    public static void clear() {
        itemBank.clear();
    }
}
