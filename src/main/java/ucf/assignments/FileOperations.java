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


public class FileOperations {

    public List<Inventory> loadTSVData(File file) {
        if (file == null) {
            return null;
        }
        List<Inventory> result = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\n");
            final String delimiter = "\t";
            int row = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //skip header
                if (row > 0) {
                    String[] values = line.split(delimiter);
                    if (values.length == 3) {
                        result.add(new Inventory(values[0].substring(1), values[1], values[2]));
                    }
                }
                row++;
            }

            return result;
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    public boolean saveTCVData(File file, List<Inventory> inventories) {
        if (file == null || inventories == null || inventories.isEmpty()) {
            return false;
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
            final Iterator<Inventory> iterator = inventories.iterator();
            String header = "Value" + "\t" + "Serial Number" + "\t" + "Name" + "\n";
            printWriter.print(header);
            while (iterator.hasNext()) {
                Inventory inventory = iterator.next();
                printWriter.print(inventory.toFileFormat());
            }
        } catch (Exception e) {
            System.out.println("Can not create save data:" + e.getMessage());
            return false;
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return true;
    }

    public boolean saveJSONData(File file, List<Inventory> inventories) {
        if (file == null || inventories == null || inventories.isEmpty()) {
            return false;
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
            final ObjectMapper mapper = new ObjectMapper();
            printWriter.print(mapper.writeValueAsString(inventories));

        } catch (Exception e) {
            System.out.println("Can not create save data:" + e.getMessage());
            return false;
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
        return true;
    }

    public List<Inventory> loadJSONData(File file) {

        StringBuilder sb = new StringBuilder();

        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(s -> sb.append(s).append("\n"));
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(sb.toString(), new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public boolean saveHTMLData(File file, List<Inventory> inventories) {
        try {

            if (file == null || inventories == null || inventories.isEmpty()) {
                return false;
            }
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(file);
                final Iterator<Inventory> iterator = inventories.iterator();
                String header = "<html><body><table border=\"1\"><thead>" +
                        "<tr><td>Value</td><td>Serial Number</td>" +
                        "<td>Name</td></tr></thead><tbody>\n";
                printWriter.print(header);
                while (iterator.hasNext()) {
                    Inventory inventory = iterator.next();
                    printWriter.print("<tr><td>" + inventory.getValue() + "</td><td>" + inventory.getSerialNumber() + "</td><td>" + inventory.getName() + "</td></tr>\n");
                }
                //footer
                printWriter.write("</tbody></table></body></html>");
            } catch (Exception e) {
                System.out.println("Can not create save data:" + e.getMessage());
                return false;
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }
            }
            return true;


        } catch (Exception e) {
            return false;
        }
    }

    public List<Inventory> loadHTMLData(File file) {
        StringBuilder sb = new StringBuilder();
        List<Inventory> result = new ArrayList<>();
        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(s -> sb.append(s).append("\n"));
            String data = sb.toString();
            int start = data.indexOf("<tbody>");
            int end = data.indexOf("</tbody>");
            data = data.substring(start + 7, end);
            data = data.replaceAll("<tr><td>", "").replaceAll("</td><td>", "\t").replaceAll("</td></tr>", "");
            System.out.println(data);
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