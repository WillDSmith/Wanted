package com.xtremeware.wanted;

/**
 * Created by Shakey on 12/8/13.
 */
public class Item {

    private long itemId;
    private long listId;
    private String quantity;
    private String name;
    private String notes;
    private String size;
    private String deletedDate;
    private String hidden;

    public static final String TRUE = "1";
    public static final String FALSE = "0";

    public Item() {
        quantity = "";
        name = "";
        notes = "";
        size = "";
        deletedDate = FALSE;
        hidden = FALSE;
    }

    public Item(int listId, String quantity, String name, String notes, String size, String deleted, String hidden) {
        this.listId = listId;
        this.quantity = quantity;
        this.name = name;
        this.notes = notes;
        this.size = size;
        this.deletedDate = deleted;
        this.hidden = hidden;
    }

    public Item(int itemId, int listId, String quantity, String name, String notes, String size, String deleted, String hidden) {
        this.itemId = itemId;
        this.listId = listId;
        this.quantity = quantity;
        this.name = name;
        this.notes = notes;
        this.size = size;
        this.deletedDate = deleted;
        this.hidden = hidden;
    }

    public long getId() {
        return itemId;
    }

    public void setId(long itemId) {
        this.itemId = itemId;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public long getDeletedDateMillis() {
        return Long.parseLong(deletedDate);
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public void setDeletedDate(long millis) {
        this.deletedDate = Long.toString(millis);
    }

    public String getHidden() {
        return hidden;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }
}
