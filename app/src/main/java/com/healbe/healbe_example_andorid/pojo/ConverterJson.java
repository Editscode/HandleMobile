package com.healbe.healbe_example_andorid.pojo;

import android.os.Build;
import android.os.Environment;


import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healbe.healbe_example_andorid.pojo.tools.HealthPack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConverterJson {
    private static String json = "{EMPTY}";
    private static String path = Environment.getExternalStorageDirectory() + "/" + File.separator;
    private static String nameFile = "healthPack.json";
    private static String nameBonusFile = "bonus.txt";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String toJSON(HealthPack healthPack) throws IOException {
        //Удаляется доп. файл
        File bonus = new File(path + nameBonusFile);
        if(!bonus.exists())
            bonus.delete();


        File file = new File(path + nameFile);
        file.createNewFile();
        if(file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, healthPack);
            json = mapper.writeValueAsString(healthPack);
        }

        return json;
    }

    //для теста поставил public, а не private
    // читаем файл в строку с помощью класса Files
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    // Возвращает дату последнего собранного JSON
    public static String getLastTimeCreateJson() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "healthPack.json");
        long time = file.lastModified();
        Date date =new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(date);
        return str;
    }


    public static boolean checkToSubmit() {
        File bonus = new File(path + nameBonusFile);
        return bonus.exists();
    }
    public static void createBonus() {
        File bonus = new File(path + nameBonusFile);
        File noBonus = new File(path + "no" + nameBonusFile);

        try {
            if(noBonus.exists())
                noBonus.renameTo(bonus);
            else if(!bonus.exists())
                bonus.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void deleteBonus() {
        File bonus = new File(path + nameBonusFile);
        if(bonus.exists()) {
            bonus.renameTo(new File(path + "no" + nameBonusFile));
        }
    }
}
