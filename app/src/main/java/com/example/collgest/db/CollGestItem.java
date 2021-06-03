package com.example.collgest.db;

import java.io.Serializable;

public class CollGestItem implements Serializable {

    private String itemGUID;
    private String itemName;
    private int itemMinJoueurs;
    private int itemMaxJoueurs;
    private int itemDuration;
    private String itemTypes;
    private String itemLastPlayed;
    private String itemCheckedOut;

    public CollGestItem(String itemGUID, String itemName, int itemMinJoueurs, int itemMaxJoueurs, int itemDuration, String itemTypes, String itemLastPlayed, String itemCheckedOut) {
        this.itemGUID = itemGUID;
        this.itemName = itemName;
        this.itemMinJoueurs = itemMinJoueurs;
        this.itemMaxJoueurs = itemMaxJoueurs;
        this.itemDuration = itemDuration;
        this.itemTypes = itemTypes;
        this.itemLastPlayed = itemLastPlayed;
        this.itemCheckedOut = itemCheckedOut;
    }

    public int getItemDuration() { return itemDuration; }
    public int getItemMaxJoueurs() { return itemMaxJoueurs; }
    public int getItemMinJoueurs() { return itemMinJoueurs; }
    public String getItemGUID() { return itemGUID; }
    public String getItemName() { return itemName; }
    public String getItemTypes() { return itemTypes; }
    public String getItemLastPlayed() { return itemLastPlayed; }
    public String getItemCheckedOut() { return itemCheckedOut; }

    public void setItemDuration(int itemDuration) { this.itemDuration = itemDuration; }
    public void setItemGUID(String itemGUID) { this.itemGUID = itemGUID; }
    public void setItemMaxJoueurs(int itemMaxJoueurs) { this.itemMaxJoueurs = itemMaxJoueurs; }
    public void setItemMinJoueurs(int itemMinJoueurs) { this.itemMinJoueurs = itemMinJoueurs; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setItemTypes(String itemTypes) { this.itemTypes = itemTypes; }
    public void setItemLastPlayed(String itemLastPlayed) { this.itemLastPlayed = itemLastPlayed; }
    public void setItemCheckedOut(String itemCheckedOut) { this.itemCheckedOut = itemCheckedOut; }

    @Override
    public String toString(){
        return "GUID : " + itemGUID + "\nName : " + itemName + "\nminJoueurs : " + itemMinJoueurs + "\nmaxJoueurs ; " + itemMaxJoueurs + "\nduration : "
                + itemDuration + "\nTypes : " + itemTypes + "\n Last PLayed : " + itemLastPlayed + "\nitemCheckedOut : " + itemCheckedOut;
    }

}
