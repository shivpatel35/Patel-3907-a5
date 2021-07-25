import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucf.assignments.Inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;

//tests inventory class based on usage in Controller
public class InventoryTest {

    private Inventory inventory;

    //tests if values are correctly added
    @BeforeEach
    public void init() {
        inventory = new Inventory("200.00", "ASDFRH7895", "SAMSUNG TV");
    }

    //test getter method for value
    @Test
    public void getValue() {

        assertEquals("$200.00", inventory.getValue());
    }

    //test setvalue method and compares to actual function
    @Test
    public void setValue() {
        inventory.setValue("250");
        System.out.println(inventory.getValue());
        assertEquals("$250.00", inventory.getValue());
    }

    //test get serial method and compares to actual function
    @Test
    public void getSerialNumber() {

        assertEquals("ASDFRH7895", inventory.getSerialNumber());
    }


//tests set SerialNumber method
    @Test
    public void setSerialNumber() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inventory.setSerialNumber("aesrf");
        });


    }

    //test getName method
    //assert equals based on if expected value equals inventory get name function
    @Test
    public void getName() {
        assertEquals("SAMSUNG TV", inventory.getName());
    }

    //setname function test
    @Test
    public void setName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inventory.setName("a");
        });
    }

    //get price test method
    @Test
    public void getPrice() {

        //asserts equal based on getprice from inventory class and expected
        assertEquals(200d,inventory.getPrice());
    }

    //file format tester compares to expected and inventory class method
    @Test
    public void toFileFormat() {
        String expected = "$200.00\tASDFRH7895\tSAMSUNG TV\n";
        assertEquals(expected,inventory.toFileFormat());
    }
}