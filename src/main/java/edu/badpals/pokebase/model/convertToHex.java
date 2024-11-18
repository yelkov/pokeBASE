package edu.badpals.pokebase.model;

public class convertToHex {
    public static String toHexadecimal(String input) {
        StringBuilder hex = new StringBuilder();
        for (char c : input.toCharArray()) {
            hex.append(String.format("%02x", (int) c));
        }
        return hex.toString();
    }
}
