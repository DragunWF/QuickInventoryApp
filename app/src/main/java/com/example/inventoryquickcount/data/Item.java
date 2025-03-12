package com.example.inventoryquickcount.data;

public class Item extends Model {
    private String title, description, category;

    public Item(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", id=" + id +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
