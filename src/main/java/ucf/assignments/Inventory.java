/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Shiv Patel
 */

package ucf.assignments;

//pre-processor directives
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Locale;
import java.util.Objects;

//Inventory Class
public class Inventory {

    //create strings for Value, Serial Number and Name
    //display value in format $xx.xx
    private String value;
    private String serialNumber;
    private String name;

    //used for sorting,create string for price
    @JsonIgnore
    private double price;

    //begins building format for inventory
    public Inventory(String value, String serialNumber, String name) {

        //adds dollar sign with if statement
        if (value != null && value.startsWith("$")) {
            value = value.substring(1);
        }

        //throws exception if monetary value is less than zero
        if (value == null || !value.trim().matches("(-?\\d+\\.?\\d{0,2})") || Double.parseDouble(value) < 0) {
            throw new IllegalArgumentException("Value must be monetary and greater than zero");
        }

        //sets value to format expected
        this.price = Double.parseDouble(value);
        this.value = String.format(Locale.US, "$%.2f", price);

        // throws exception if serial number is not as expected
        if (serialNumber == null || !serialNumber.trim().matches("[A-Z0-9]+") || serialNumber.trim().length() != 10) {
            throw new IllegalArgumentException("Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit");
        }
        //sets up serial number
        this.serialNumber = serialNumber;

        //throws argument if name is not as expected using if statement to compare if length entered is between 2 and 256 characters
        if (name == null || name.length() < 2 || name.length() > 256) {
            throw new IllegalArgumentException("Item shall have a name between 2 and 256 characters inclusive");

        }

        //sets up name
        this.name = name;
    }

    //getter method for value
    public String getValue() {
        return value;
    }

    //setter method for value
    public void setValue(String value) {

        if (value != null && value.startsWith("$")) {
            value = value.substring(1);
        }

        //throws exception if value is not greater than zero
        if (value == null || !value.trim().matches("(-?\\d+\\.?\\d{0,2})") || Double.parseDouble(value) < 0) {
            throw new IllegalArgumentException("Value must be monetary and greater than zero");
        }

        //sets up value to follow format expected and parses
        this.price = Double.parseDouble(value);
        this.value = String.format(Locale.US, "$%.2f", price);
    }

    //getter method for serial number
    public String getSerialNumber() {
        return serialNumber;
    }

    //setter method for serial number
    public void setSerialNumber(String serialNumber) {

        //throws argument if serial number is not correct expected format
        if (serialNumber == null || !serialNumber.trim().matches("[A-Z0-9]+") || serialNumber.trim().length() != 10) {
            throw new IllegalArgumentException("Serial number should be unique in the format XXXXXXXXXX where X can be letter or digit");
        }

        //sets serial number
        this.serialNumber = serialNumber;
    }

    //getter method for name of Item
    public String getName() {
        return name;
    }

    //setter method for Name
    public void setName(String name) {

        //throws argument exception if name is not in between 2 or 56 characters
        if (name == null || name.length() < 2 || name.length() > 256) {
            throw new IllegalArgumentException("Item shall have a name between 2 and 256 characters inclusive");
        }

        //sets name
        this.name = name;
    }

    //used again for getting value and using for sorting
    @JsonIgnore
    //getter method for value or price
    public double getPrice() {
        return price;
    }

   //used to test for equality between objects of Value,Serial Number, and Name
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(value, inventory.value) &&
                Objects.equals(serialNumber, inventory.serialNumber) &&
                Objects.equals(name, inventory.name);
    }

    //used again for creating equality between objects using hash
    @Override
    public int hashCode() {
        return Objects.hash(value, serialNumber, name);
    }

    //string formatter for value, serialNumber, and name
    @Override
    public String toString() {
        return String.format(Locale.US, "$%10s %20s %20s", value, serialNumber, name);
    }

    public String toFileFormat() {

        return value + "\t" + serialNumber + "\t" + name + "\n";
    }
}

