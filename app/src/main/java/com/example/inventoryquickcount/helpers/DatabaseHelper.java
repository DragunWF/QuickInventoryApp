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

    public static void addDummyData(){
        ItemService.add(new Item("Mystery Box","This enigmatic treasure chest holds an array of random items, each with its own story and purpose. From vintage trinkets to modern gadgets, the contents of the Mystery Box remain hidden until you open it. Perfect for those who love surprises and enjoy the thrill of the unknown, this box is guaranteed to spark curiosity and excitement.","Collectibles & Curiosities"));
        ItemService.add(new Item("Handcrafted Wooden Puzzle","This intricate wooden puzzle is a true test of patience and skill. Made from high-quality hardwood, each piece is carefully cut and shaped to ensure a challenging and rewarding experience. Perfect for puzzle enthusiasts or as a unique gift, this puzzle is both beautiful and brain-teasing.","Puzzles & Brain Teasers"));
        ItemService.add(new Item("Vintage Pocket Watch","A timeless piece of craftsmanship, this vintage pocket watch exudes elegance and history. With its intricate engravings and classic design, it offers a glimpse into a bygone era. Whether as a collector's item or a stylish accessory, this pocket watch adds a touch of sophistication to any outfit.","Antiques & Collectibles"));
        ItemService.add(new Item("Solar-Powered Garden Light","Illuminate your garden with this eco-friendly solar-powered light. It harnesses the power of the sun during the day and automatically lights up at night, providing a warm and inviting glow. Ideal for pathways, patios, or garden beds, this light is both practical and environmentally conscious.","Outdoor & Garden"));
        ItemService.add(new Item("Exotic Spice Blend","Elevate your culinary creations with this exotic spice blend. Featuring a harmonious mix of spices from around the world, it adds a burst of flavor to any dish. Perfect for adventurous cooks and food enthusiasts, this blend brings a taste of global cuisine to your kitchen.","Cooking & Food"));
    }
}

