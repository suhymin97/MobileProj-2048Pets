package dsa.hcmiu.a2048pets.entities.model;

public class ShopItem {

    private String name;
    private int id;
    private int picture;
    private long price;
    private boolean purchase;

    public ShopItem(String name, int id, int picture, long price) {
        this.name = name;
        this.id = id;
        this.picture = picture;
        this.price = price;
        purchase = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isPurchase() {
        return purchase;
    }

    public void setPurchased() {
        purchase = true;
        price = 0;
    }

    public void setPurchase(boolean purchase) {
        this.purchase = purchase;
    }

    public void returnDefault(){ //Duc
        purchase = false;
    }
}
