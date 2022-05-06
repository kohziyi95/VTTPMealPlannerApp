package vttp2022.mealplannerapp.model;

public class ShoppingListItem {
    private int id;
    private String itemName;
    private String quantity;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    
}
