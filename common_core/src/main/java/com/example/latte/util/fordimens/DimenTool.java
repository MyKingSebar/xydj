package com.example.latte.util.fordimens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DimenTool {

    public static void gen() {

        File file = new File("./common_core/src/main/res/values/dimens.xml");
        BufferedReader reader = null;
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw800 = new StringBuilder();
        StringBuilder sw820 = new StringBuilder();
        StringBuilder sw900 = new StringBuilder();


        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {

                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    int num = Integer.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));

                    sw480.append(start).append((int) Math.round(num * 0.53)).append(end).append("\n");
                    sw600.append(start).append((int) Math.round(num * 0.67)).append(end).append("\n");
                    sw720.append(start).append((int) Math.round(num * 0.8)).append(end).append("\n");
                    sw800.append(start).append((int) Math.round(num * 0.88)).append(end).append("\n");
                    sw820.append(start).append((int) Math.round(num * 0.91)).append(end).append("\n");
                    sw900.append(start).append((int) Math.round(num * 1)).append(end).append("\n");

                } else {
                    sw480.append(tempString).append("\n");
                    sw600.append(tempString).append("\n");
                    sw720.append(tempString).append("\n");
                    sw800.append(tempString).append("\n");
                    sw820.append(tempString).append("\n");
                    sw900.append(tempString).append("\n");
                }
                line++;
            }
            reader.close();

            String sw480file = "./common_core/src/main/res/values-sw480dp-land/dimens.xml";
            String sw600file = "./common_core/src/main/res/values-sw600dp-land/dimens.xml";
            String sw720file = "./common_core/src/main/res/values-sw720dp-land/dimens.xml";
            String sw800file = "./common_core/src/main/res/values-sw800dp-land/dimens.xml";
            String sw820file = "./common_core/src/main/res/values-sw820dp-land/dimens.xml";
            String sw900file = "./common_core/src/main/res/values-sw900dp-land/dimens.xml";
            writeFile(sw480file, sw480.toString());
            writeFile(sw600file, sw600.toString());
            writeFile(sw720file, sw720.toString());
            writeFile(sw800file, sw800.toString());
            writeFile(sw820file, sw820.toString());
            writeFile(sw900file, sw900.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
//            File f=new File(file);
//            if(!f.exists()){
//                f.createNewFile();
//            }
            String filepath=file.replace("dimens.xml","");
            File f=new File(filepath);
            if (!f.exists()) {
                f.mkdirs();
            }
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.close();
    }

    public static void main(String[] args) {
        gen();
    }


}
