package GUI.Controller.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CurrencyReader {
    public static Map<String, String> readCurrencyFromFile(String filePath) {
        Map<String, String> currencyMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String country = parts[0].trim();
                    String currency = parts[1].trim();
                    currencyMap.put(country, currency);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currencyMap;
    }
}
