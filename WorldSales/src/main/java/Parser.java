
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    public static ArrayList<Sale> parser() throws IOException {
        ArrayList<Sale> dataSales = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader("Продажа продуктов в мире.csv"))) {
            String[] fileline;
            boolean isHeadline = true;

            // Чтение файла построчно
            while ((fileline = reader.readNext()) != null) {
                if (isHeadline) {
                    isHeadline = false;
                    continue;
                }
                dataSales.add(new Sale(fileline[0], fileline[1], fileline[2], fileline[3],
                        fileline[4], fileline[5],
                        Integer.parseInt(fileline[6]), Double.parseDouble(fileline[7])));
            }
        } catch (CsvException e) {
            throw new RuntimeException("Ошибка при чтении CSV файла", e);
        }

        return dataSales;
    }
}



