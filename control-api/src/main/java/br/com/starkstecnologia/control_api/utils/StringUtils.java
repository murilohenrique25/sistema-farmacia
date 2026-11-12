package br.com.starkstecnologia.control_api.utils;

import br.com.starkstecnologia.control_api.jwt.JwtUserDetails;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class StringUtils {

    public static LocalDateTime retornaDataInicioAtual() {
        return LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    public static LocalDateTime retornaDataFinalAtual() {

        return LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }

    public static String formatarParaMoedaBrasileira(double valor) {
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatador.format(valor);
    }

    public static String convertSecondsToHHMMSS(Double seconds) {
        if (seconds == null) {
            return "";
        }
        long hours = seconds.longValue() / 3600;
        long minutes = (seconds.longValue() % 3600) / 60;
        long secs = seconds.longValue() % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    public static String converterFormatoBrasileiro(LocalDateTime data) {
        if (data == null) {
            return "";
        }
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return data.format(formato);
    }

    public static String extrairDoisPrimeirosNomes(String nomeCompleto) {
        if (nomeCompleto == null) {
            return "";
        }
        // Dividir a string em um array de nomes
        String[] nomes = nomeCompleto.trim().split(" ");

        // Verificar se existem pelo menos dois nomes
        if (nomes.length <= 2) {
            return nomeCompleto.trim(); // Retorna o nome completo se tiver 2 ou menos nomes
        }

        // Retornar os dois primeiros nomes
        return nomes[0] + " " + nomes[1];
    }

    public static LocalDateTime parseStringToLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static Long idMatrizUsuarioAutenticado() {
        Object objectUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JwtUserDetails userDetails = (JwtUserDetails) objectUser;
        return userDetails.getIdMatriz();
    }
}
