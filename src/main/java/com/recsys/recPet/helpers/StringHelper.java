package com.recsys.recPet.helpers;

public class StringHelper {
    public static String maskCpf(String cpf) {
        if (cpf == null) return null;

        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) return cpf;

        String part1 = digits.substring(0, 3);
        String partEnd = digits.substring(9);

        return String.format("%s.%s.%s-%s", part1, "***", "***", partEnd);
    }
}
