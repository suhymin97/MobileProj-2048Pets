package dsa.hcmiu.a2048pets.entities.model;

public class Pets {
    private int value;
    private int pic;
    private int id;

    public Pets(int value) {
        this.value = value;
    }
    public Pets(Pets p) {
        id = p.id;
        value = p.getValue();
        pic = p.getPic();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

}
