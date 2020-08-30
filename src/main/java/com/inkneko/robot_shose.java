package com.inkneko;

import java.io.File;
import java.util.Date;
import java.util.Random;

/*¿Ô¿Ô*/
public class robot_shose {
    private String pictureBasePath = "C:\\artbook";
    private File baseDirectory;
    private File[] comicDirectores;

    public robot_shose(){
        baseDirectory = new File(pictureBasePath);
        comicDirectores = baseDirectory.listFiles();
    }

    public String getRandomPicturePath(){
        Date date = new Date();
        Random random = new Random(date.getTime());
        int comicOrder = random.nextInt(comicDirectores.length);
        String selectedComicPath = comicDirectores[comicOrder].getPath();
        File comicDirectory = new File(selectedComicPath);
        File[] picturePathes = comicDirectory.listFiles();
        String selectedPicture = picturePathes[random.nextInt(picturePathes.length)].getPath();
        return selectedPicture;
    }
}
