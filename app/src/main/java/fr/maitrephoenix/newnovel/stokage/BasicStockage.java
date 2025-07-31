package fr.maitrephoenix.newnovel.stokage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BasicStockage {
    private static Activity activity;

    public static void initialiserStockage(Activity activite) {
        activity = activite;
    }

    public static boolean writeFileInterne(String nomFichier, String text) {
        try (FileOutputStream fos = activity.openFileOutput(nomFichier, activity.MODE_PRIVATE)) {
            fos.write(text.getBytes());
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String readFileInterne(String nomFichier) {
        return readFileInterne("", nomFichier);
    }

    public static String readFileInterne(String dossier, String nomFichier) {
        FileInputStream fis = null;
        File file = new File(activity.getFilesDir()+"/"+dossier, nomFichier);
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            String contents = stringBuilder.toString();
            return contents;
        }
    }

    //donne le lien concrenant les dossiers les un dans les autres et les créer si il n'existe pas
    public static String fusionLienDossier(String[] dossiers){
        File parent = activity.getFilesDir();
        String lien = "";
        for (String dossier : dossiers) {
            lien += lien.equals("") ? dossier : "/"+dossier;
            parent = new File(parent+"/"+dossier);
            if (!parent.exists()) {
                parent.mkdir();
            }
        }
        return lien;
    }

    public static boolean createFileInFolder(String dossier, String fileName, String fileContent) {
        try {
            // Créer un fichier dans le dossier
            File file = new File(activity.getFilesDir()+"/"+dossier, fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileContent.getBytes());
            outputStream.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static ArrayList<String> getFolderFromFolders(String directoryName) {
        ArrayList<String> folderNames = new ArrayList<>();
        File directory = new File(activity.getFilesDir()+"/"+directoryName);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    folderNames.add(file.getName());
                }
            }
        }
        return folderNames;
    }

    public static ArrayList<String> getFileFromFolders(String directoryName) {
        ArrayList<String> fileNames = new ArrayList<>();
        File directory = new File(activity.getFilesDir()+"/"+directoryName);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (!file.isDirectory()) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }

    //ne pas mettre d'extension au nom du fichier
    public static void saveImageInterne(String nomFichier, String dossier, Bitmap image) {
        byte[] imageByte = bitmapToByteArray(image);
        try {
            File folder = new File(activity.getFilesDir() + "/" + dossier);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(new File(folder, nomFichier+".png"));
            fos.write(imageByte);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static Bitmap getImageInterne(String dossier, String nomFichier) {
        FileInputStream fis = null;
        File file = new File(activity.getFilesDir() + "/" + dossier, nomFichier);
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }

        return BitmapFactory.decodeStream(fis);
    }

    public static void deleteDirectoryOrFile(String element) {
        File file = new File(activity.getFilesDir() + "/" + element);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File fichiers : files) {
                    deleteDirectoryOrFile(element + "/" + fichiers.getName());
                }
            }
            file.delete();
        }
        else{
            file.delete();
        }
    }
}
