package com.watchdog.services;


import com.watchdog.dao.video.VideoDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by renee on 11/15/2016.
 */
public class VideoInsertDeleteService {

    private static final int MAX_ALLOWED_AGE = 3;

    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
    VideoDao videoeDao = ctx.getBean("videoDaoImpl", VideoDao.class);

     public ArrayList<File> getFiles(File folder) {
         ArrayList<File> fileList = new ArrayList<File>();

         for (final File fileEntry : folder.listFiles()) {
             if (fileEntry.isFile()) {
                 System.out.println("File:  " + fileEntry.getName());
                 fileList.add(fileEntry);
             } else if (fileEntry.isDirectory()) {
                 System.out.println("Directory:  " + fileEntry.getName());
             }
         }
         return fileList;
     }

    public void insertVideoIntoDb() {

    }

    public void deleteFile(File file) {

        videoeDao.deleteByName(file.getName());
        file.delete();
    }

    public void deleteVideoFromDb(String fileName) {

    }

    public boolean overMaxAllowedAge(File file) {

        int days = parseDays(file.getName());

        if (days >= MAX_ALLOWED_AGE) {
            return true;
        } else {
            return false;
        }
    }

    public int parseDays(String fileName) {

        String fileYear = fileName.substring(3,7);
        String fileMonth = fileName.substring(7,9);
        String fileDay = fileName.substring(9,11);

        String dateCreatedStr = fileMonth + "-" + fileDay  + "-" + fileYear;
        Date now = new Date();

        int days = 0;


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

            Date dateCreated = dateFormat.parse(dateCreatedStr);
            String nowStr = dateFormat.format(now);
            Date currentDate = dateFormat.parse(nowStr);
            days = daysBetween(dateCreated, currentDate);

            System.out.println("\nDate video created: " + dateCreated + "  Current Date: " +
                            currentDate + "\nDays between dates: " + days);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    private static int daysBetween(Date one, Date two) {
        long difference = (one.getTime()-two.getTime())/86400000;
        return (int)Math.abs(difference);
    }

    public boolean fileExists(String fileName, File directory) {

        List<File> fileList = getFiles(directory);

        for (final File file : fileList) {
            if (file.getName().equals(fileName)) {
                System.out.println(file.getName() + " already exists in folder.");
                return true;
            }
            else // remove else after testing
                System.out.println(file.getName() + " does NOT exist in folder.");
        }
        return false;
    }


}

