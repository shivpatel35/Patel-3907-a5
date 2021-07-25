/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Shiv Patel
 */


package ucf.assignments;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

//file operations class for different file types
public class FileOperations {

    //load TSV data function
    public List<Inventory> loadTSVData(File file) {

        //if function to return null if file is null
        if (file == null) {
            return null;
        }

        //new array list decleration based on inventory class
        List<Inventory> result = new ArrayList<>();

        //try catch statement to scan file
        try (Scanner scanner = new Scanner(file)) {
            //delimmitter to split file
            scanner.useDelimiter("\n");
            final String delimiter = "\t";
            int row = 0;

            //reads file line by lin
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //skip header
                if (row > 0) {
                    String[] values = line.split(delimiter);
                    if (values.length == 3) {
                        result.add(new Inventory(values[0].substring(1), values[1], values[2]));
                    }
                }
                //increments row counter
                row++;
            }

            return result;
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    //saveTsv function boolean
    public boolean saveTCVData(File file, List<Inventory> inventories) {

        //if file equals null, inventories = null, and is empty returns false
        if (file == null || inventories == null || inventories.isEmpty()) {
            return false;
        }

        //printwriter
        PrintWriter printWriter = null;

        //try catch statement to print TSV file
        try {

            //printWriter declaration
            printWriter = new PrintWriter(file);

            //iterator declaration based on Inventory class
            final Iterator<Inventory> iterator = inventories.iterator();

            //header creation
            String header = "Value" + "\t" + "Serial Number" + "\t" + "Name" + "\n";

            //print head
            printWriter.print(header);

            //iterator prints line by line
            while (iterator.hasNext()) {
                Inventory inventory = iterator.next();
                printWriter.print(inventory.toFileFormat());
            }

            //Catch exception prints message if it can not create save data and returns false
        } catch (Exception e) {
            System.out.println("Can not create save data:" + e.getMessage());
            return false;
        } finally {
            //close printwriter if null
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return true;
    }

    //boolean saveJson function
    public boolean saveJSONData(File file, List<Inventory> inventories) {

        //if file equals null, inventories = null, and is empty returns false
        if (file == null || inventories == null || inventories.isEmpty()) {
            return false;
        }


        PrintWriter printWriter = null;

        //try catch statement to print JSON file
        try {

            //printwriter declaration
            printWriter = new PrintWriter(file);

            //Objectmapper declaration
            final ObjectMapper mapper = new ObjectMapper();

            //print writer writes based on string and Objectmapper
            printWriter.print(mapper.writeValueAsString(inventories));

        } catch (Exception e) {
            //throws exception if save data cannot be created
            System.out.println("Can not create save data:" + e.getMessage());
            return false;
        } finally {
            if (printWriter != null) {
                //printwriter closes if null
                printWriter.close();
            }
        }
        return true;
    }

    //JSON data loader function
    public List<Inventory> loadJSONData(File file) {

        //Stringbuilder declaration
        StringBuilder sb = new StringBuilder();

        //try catch statement to load data based on string stream
        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            //stream catcher
            stream.forEach(s -> sb.append(s).append("\n"));

            //object mapper declaration
            final ObjectMapper mapper = new ObjectMapper();

            //returns mapper read value based on mapper
            return mapper.readValue(sb.toString(), new TypeReference<>() {
            });
        } catch (IOException e) {

            //prints stack track if error is found
            e.printStackTrace();
        }

        return null;

    }


    //saveHTML data function
    public boolean saveHTMLData(File file, List<Inventory> inventories) {
        try {

            //if file is not found and/or empty function returns false
            if (file == null || inventories == null || inventories.isEmpty()) {
                return false;
            }

            //printwriter
            PrintWriter printWriter = null;

            //try catch statement to save html data
            try {

                //printwriter declaration
                printWriter = new PrintWriter(file);

                //final iterator declaration based on inventory class
                final Iterator<Inventory> iterator = inventories.iterator();

                //header setup
                String header = "<html><body><table border=\"1\"><thead>" +
                        "<tr><td>Value</td><td>Serial Number</td>" +
                        "<td>Name</td></tr></thead><tbody>\n";

                //prints header to file
                printWriter.print(header);

                //while statement for iterator
                while (iterator.hasNext()) {
                    //print writer writes line by line from table view
                    Inventory inventory = iterator.next();
                    printWriter.print("<tr><td>" + inventory.getValue() + "</td><td>" + inventory.getSerialNumber() + "</td><td>" + inventory.getName() + "</td></tr>\n");
                }

                //footer printwriting
                printWriter.write("</tbody></table></body></html>");

                //catch exception if error is found
            } catch (Exception e) {
                System.out.println("Can not create save data:" + e.getMessage());
                return false;

                //closes print writer
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }
            }
            return true;

//error found returns false
        } catch (Exception e) {
            return false;
        }
    }

    //html data loader
    public List<Inventory> loadHTMLData(File file) {

        //stringbuilder declaration
        StringBuilder sb = new StringBuilder();

        //arraylist declaration based on inventory class
        List<Inventory> result = new ArrayList<>();

        //try catch statement to load data from file
        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {

            //splits data
            stream.forEach(s -> sb.append(s).append("\n"));
            String data = sb.toString();

            //start and declaration
            int start = data.indexOf("<tbody>");
            int end = data.indexOf("</tbody>");
            data = data.substring(start + 7, end);

            //replaces data in html format to ""
            data = data.replaceAll("<tr><td>", "").replaceAll("</td><td>", "\t").replaceAll("</td></tr>", "");
            System.out.println(data);

            //splits data into array rows
            String[] rows = data.split("\n");

            for (String row : rows) {
                if (!row.isEmpty()) {
                    String[] values = row.split("\t");
                    if (values.length == 3) {
                        result.add(new Inventory(values[0].substring(1), values[1], values[2]));
                    }
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}