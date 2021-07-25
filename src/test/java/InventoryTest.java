import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucf.assignments.Inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    public void init() {
        inventory = new Inventory("200.00", "ASDFRH7895", "SAMSUNG TV");
    }

    @Test
    public void getValue() {

        assertEquals("$200.00", inventory.getValue());
    }

    @Test
    public void setValue() {
        inventory.setValue("250");
        System.out.println(inventory.getValue());
        assertEquals("$250.00", inventory.getValue());
    }

    @Test
    public void getSerialNumber() {

        assertEquals("ASDFRH7895", inventory.getSerialNumber());
    }


    @Test
    public void setSerialNumber() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inventory.setSerialNumber("aesrf");
        });


    }

    @Test
    public void getName() {
        assertEquals("SAMSUNG TV", inventory.getName());
    }

    @Test
    public void setName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inventory.setName("a");
        });
    }

    @Test
    public void getPrice() {

        assertEquals(200d,inventory.getPrice());
    }

    @Test
    public void toFileFormat() {
        String expected = "$200.00\tASDFRH7895\tSAMSUNG TV\n";
        assertEquals(expected,inventory.toFileFormat());
    }
}