package location;

public class NameArray {
    String[] data;

    public NameArray(String[] data) {
        this.data = new String[152];                                                        // 152 is how many last names there are. There are more last names than first or last
    }

    public String getData(int index) {
        return data[index];
    }
}
