package ua.tarastom.newsaggregator.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#ffeead")),
                    new ColorDrawable(Color.parseColor("#93cfb3")),
                    new ColorDrawable(Color.parseColor("#fd7a7a")),
                    new ColorDrawable(Color.parseColor("#faca5f")),
                    new ColorDrawable(Color.parseColor("#1ba798")),
                    new ColorDrawable(Color.parseColor("#6aa9ae")),
                    new ColorDrawable(Color.parseColor("#ffbf27")),
                    new ColorDrawable(Color.parseColor("#d93947"))
            };

    public static ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public static String DateToTimeFormat(String stringDate) {
        String isTime = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            LocalDateTime localDate = LocalDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(stringDate));
//            ZonedDateTime zdt = localDate.atZone(ZoneId.systemDefault());
//            Date output = Date.from(zdt.toInstant());
//            PrettyTime p = new PrettyTime(new Locale(getCountry()));
//            isTime = p.format(output);
//        } else {
        PrettyTime p = new PrettyTime(new Locale(getCountry()));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            Date date = sdf.parse(stringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTime;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String DateFormat(String stringDate) {
        String newDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", new Locale(getCountry()));
        newDate = dateFormat.format(getDate(stringDate));
        return newDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Date getDate(String stringDate) {
        LocalDateTime ldt = LocalDateTime.parse(stringDate);
//        Date in = new Date();
//        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
//        Date date = null;
//        try {
//            date = sdf.parse(stringDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return date;
    }

    public static String getCountry() {
        Locale locale = Locale.getDefault();
        return locale.getCountry().toLowerCase();
    }

    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage();
    }
}