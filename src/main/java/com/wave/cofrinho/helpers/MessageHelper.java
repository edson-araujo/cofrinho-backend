package com.wave.cofrinho.helpers;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageHelper {

    private static final String BASE_NAME = "messages"; // Nome base dos seus arquivos de propriedades

    private MessageHelper() {
        // Construtor privado para evitar instanciação
    }

    public static String getMessage(String key, Locale locale) {
        ResourceBundle messages = ResourceBundle.getBundle(BASE_NAME, locale);
        return messages.getString(key);
    }

    // Sobrecarga para usar o idioma padrão da requisição (se disponível)
    public static String getMessage(String key) {
        // Implemente a lógica para obter o idioma da requisição aqui (ex: LocaleContextHolder.getLocale() no Spring)
        Locale locale = new Locale("pt", "BR"); // Substitua pela lógica real
        return getMessage(key, locale);
    }
}
