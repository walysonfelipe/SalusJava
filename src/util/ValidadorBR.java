package util;

/**
 * Utilitário de validação
 * Cobre CPF, e-mail e telefone (fixo e celular).
 */
public class ValidadorBR {

    private ValidadorBR() {}

    // ================================================================== CPF

    /**
     * Valida um CPF brasileiro.
     * Aceita com ou sem formatação: "123.456.789-09" ou "12345678909".
     */
    public static boolean validarCpf(String cpf) {
        if (cpf == null) return false;

        String digitos = cpf.replaceAll("\\D", "");

        if (digitos.length() != 11) return false;

        // Rejeita sequências homogêneas (ex: 111.111.111-11)
        if (digitos.matches("(\\d)\\1{10}")) return false;

        int d1 = calcularDigitoVerificadorCpf(digitos, 9);
        int d2 = calcularDigitoVerificadorCpf(digitos, 10);

        return digitos.charAt(9)  == Character.forDigit(d1, 10)
            && digitos.charAt(10) == Character.forDigit(d2, 10);
    }

    private static int calcularDigitoVerificadorCpf(String digitos, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += Character.getNumericValue(digitos.charAt(i)) * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return (resto == 10 || resto == 11) ? 0 : resto;
    }

    /**
     * Formata uma string de 11 dígitos no padrão "XXX.XXX.XXX-XX".
     * Retorna a string original se não puder formatar.
     */
    public static String formatarCpf(String cpf) {
        if (cpf == null) return "";
        String d = cpf.replaceAll("\\D", "");
        if (d.length() != 11) return cpf;
        return d.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    // ================================================================= EMAIL

    /**
     * Valida um endereço de e-mail.
     * Exige pelo menos um ponto no domínio e extensão mínima de 2 caracteres.
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.isBlank()) return false;

        // Proíbe espaços e exige o formato local@dominio.ext
        String regex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        return email.trim().matches(regex);
    }

    // =============================================================== TELEFONE

    /**
     * Valida telefone brasileiro — fixo (8 dígitos) ou celular (9 dígitos).
     *
     * Formatos aceitos (com ou sem formatação):
     *   (14) 3300-1234   → fixo
     *   (14) 99001-1234  → celular
     *   14933001234      → celular sem formatação
     *   1433001234       → fixo sem formatação
     *
     * DDD válido: 11–99.
     * Celular: 9º dígito obrigatoriamente 9.
     */
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) return false;

        String digitos = telefone.replaceAll("\\D", "");

        // Deve ter 10 (fixo) ou 11 (celular) dígitos
        if (digitos.length() != 10 && digitos.length() != 11) return false;

        int ddd = Integer.parseInt(digitos.substring(0, 2));
        if (ddd < 11 || ddd > 99) return false;

        if (digitos.length() == 11) {
            // Celular: nono dígito deve ser 9
            return digitos.charAt(2) == '9';
        }

        // Fixo: primeiro dígito do número não pode ser 0 nem 9
        char primeiroDigito = digitos.charAt(2);
        return primeiroDigito != '0' && primeiroDigito != '9';
    }

    /**
     * Formata um telefone no padrão "(XX) XXXXX-XXXX" (celular)
     * ou "(XX) XXXX-XXXX" (fixo).
     * Retorna a string original se não puder formatar.
     */
    public static String formatarTelefone(String telefone) {
        if (telefone == null) return "";
        String d = telefone.replaceAll("\\D", "");
        if (d.length() == 11) {
            return d.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        }
        if (d.length() == 10) {
            return d.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return telefone;
    }

    // =========================================================== MENSAGENS

    /**
     * Retorna uma mensagem de erro descritiva, ou null se válido.
     * Útil para exibir feedback ao usuário no menu.
     */
    public static String mensagemCpf(String cpf) {
        if (cpf == null || cpf.isBlank())         return "CPF não informado.";
        if (cpf.replaceAll("\\D","").length() != 11) return "CPF deve ter 11 dígitos.";
        if (!validarCpf(cpf))                      return "CPF inválido (dígitos verificadores incorretos).";
        return null;
    }

    public static String mensagemEmail(String email) {
        if (email == null || email.isBlank()) return "E-mail não informado.";
        if (!validarEmail(email))             return "E-mail inválido (formato esperado: usuario@dominio.com).";
        return null;
    }

    public static String mensagemTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) return "Telefone não informado.";
        String d = telefone.replaceAll("\\D", "");
        if (d.length() != 10 && d.length() != 11)  return "Telefone deve ter 10 (fixo) ou 11 (celular) dígitos.";
        if (!validarTelefone(telefone))             return "Telefone inválido (DDD ou formato incorreto).";
        return null;
    }
}
