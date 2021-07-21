package ucf.assignments;

public class Inventory{
        //String declaration for Value,serial_number, name, and nameDate
        private String Value;
        private String serial_number;
        private String name;

        //constructor
        public Inventory() {
            Value = "";
            serial_number ="";
            name = "";
        }


        //setter and getter type method for class to recieve and return strings
        public Inventory(String Value,String serial_number, String name, String nameDate) {
            this.Value=Value;
            this.serial_number=serial_number;
            this.name=name;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }

        public void setserial_number(String serial_number) {
            this.serial_number = serial_number;
        }

        public void setname(String name) {
            this.name = name;
        }
        

        public String getValue() {
            return this.Value;
        }

        public String getserial_number() {
            return this.serial_number;
        }

        public String getname() {
            return this.name;
        }



        //creates a string for the save file
        public String getSaveString() {
            String str;
            str = this.Value + "\n"
                    + this.serial_number + "\n"
                    + this.name + "\n";
            return str;
        }

        //creates a formatted return string of item components
        public String getPrintString() {
            String desc = this.Value;
            if(desc.length() > 20) {
                desc = desc.substring(0, 19) + "...";
            }
            String str = String.format("|%-2d|%-23s|%-10s|%-20s|",desc, this.serial_number,
                    this.name);
            return str;
        }


}


