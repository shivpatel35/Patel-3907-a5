package ucf.assignments;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Locale;
import java.util.Objects;


public class Inventory {

    //display value in format $xx.xx
    private String value;
    private String serialNumber;
    private String name;
    //used for sorting
    @JsonIgnore
    private double price;

    public Inventory() {
    }

    public Inventory(String value, String serialNumber, String name) {
        if (value != null && value.startsWith("$")) {
            value = value.substring(1);
        }
        if (value == null || !value.trim().matches("(-?\\d+\\.?\\d{0,2})") || Double.parseDouble(value) < 0) {
            throw new IllegalArgumentException("Value must be monetary and greater than zero");
        }
        this.price = Double.parseDouble(value);
        this.value = String.format(Locale.US, "$%.2f", price);

        if (serialNumber == null || !serialNumber.trim().matches("[A-Z0-9]+") || serialNumber.trim().length() != 10) {
            throw new IllegalArgumentException("Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit");
        }
        this.serialNumber = serialNumber;
        if (name == null || name.length() < 2 || name.length() > 256) {
            throw new IllegalArgumentException("Item shall have a name between 2 and 256 characters inclusive");

        }
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value != null && value.startsWith("$")) {
            value = value.substring(1);
        }
        if (value == null || !value.trim().matches("(-?\\d+\\.?\\d{0,2})") || Double.parseDouble(value) < 0) {
            throw new IllegalArgumentException("Value must be monetary and greater than zero");
        }
        this.price = Double.parseDouble(value);
        this.value = String.format(Locale.US, "$%.2f", price);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        if (serialNumber == null || !serialNumber.trim().matches("[A-Z0-9]+") || serialNumber.trim().length() != 10) {
            throw new IllegalArgumentException("Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit");
        }
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.length() < 2 || name.length() > 256) {
            throw new IllegalArgumentException("Item shall have a name between 2 and 256 characters inclusive");
        }
        this.name = name;
    }

    @JsonIgnore
    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(value, inventory.value) &&
                Objects.equals(serialNumber, inventory.serialNumber) &&
                Objects.equals(name, inventory.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, serialNumber, name);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "$%10s %20s %20s", value, serialNumber, name);
    }

    public String toFileFormat() {

        return value + "\t" + serialNumber + "\t" + name + "\n";
    }
}

