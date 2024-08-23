package com.example.appdoacao;

public class cpfValidacao {
    public static boolean cpfValidar(String CPF) {
        if (CPF == null || CPF.length() != 11 ||
                CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999")) {
            return false;
        }

        int soma = 0;
        for(int i = 0; i < 9; i ++){
            soma += (CPF.charAt(i) - '0') * (10 - i);
        }

        int resto = 11 - (soma % 11);
        int digitoVerificador1 = (resto == 10 || resto == 11) ? 0 : resto;

        if(digitoVerificador1 != CPF.charAt(9) - '0'){
            return false;
        }

        soma = 0;
        for(int i = 0; i < 10; i++){
            soma += (CPF.charAt(i) - '0') * (11 - i);
        }
        resto = 11 - (soma % 11);
        int digitoVerificador2 = (resto == 10 || resto == 11) ? 0 : resto;
        return digitoVerificador2 == CPF.charAt(10) - '0';
    }
}
