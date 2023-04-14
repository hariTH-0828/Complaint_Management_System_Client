package edu.mobile.complaint;

import java.util.UUID;

public class GeneratorComplaintId {


    public static int hexToDecimal(String hex) {
        int decimal = 0;
        int power = 0;
        for (int i = hex.length() - 1; i >= 0; i--) {
            char c = hex.charAt(i);
            int value = 0;
            if (c >= '0' && c <= '9') {
                value = c - '0';
            } else if (c >= 'a' && c <= 'f') {
                value = c - 'a' + 10;
            } else if (c >= 'A' && c <= 'F') {
                value = c - 'A' + 10;
            }
            decimal += value * Math.pow(16, power);
            power++;
        }
        return decimal;
    }

    public static String generateId() {
        UUID uuid = UUID.randomUUID();
        String[] hexaValues = uuid.toString().split("-");

        int base = hexToDecimal(hexaValues[1]);
        int ceil = hexToDecimal(hexaValues[2]);

        int key = hexToDecimal(hexaValues[4]);
        base %= key;
        ceil %= key;

        return String.valueOf(base)+String.valueOf(ceil);
    }
}
