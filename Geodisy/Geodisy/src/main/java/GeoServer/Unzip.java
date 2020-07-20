package GeoServer;

import BaseFiles.GeoLogger;
import _Strings.GeodisyStrings;

import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;

import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.LinkedList;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * Class for unzipping a file right before uploading things to Geosever
 * and then deleting the unzipped files after the uploadVector process is finished
 */
public class Unzip {
    GeoLogger logger;

    public Unzip() {
        logger = new GeoLogger(this.getClass());
    }

    private LinkedList<File> unzipFunction(String filePath, String destpath){
        String basename = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));
        byte[] buffer = new byte[1024];

        try {
            //create output directory is not exists
            File folder = new File(destpath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(filePath));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                if (ze.isDirectory()||GeodisyStrings.fileTypesToIgnore(fileName.toLowerCase())) {
                    ze = zis.getNextEntry();
                    continue;
                }

                fileName = new File(fileName).getName();
                if(!fileName.equals(basename)) {
                    fileName = basename + "9_9" + fileName;
                }
                File newFile = new File(destpath + GeodisyStrings.replaceSlashes(File.separator) + fileName);


                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        LinkedList<File> answer = new LinkedList<>();
        File orig = new File(filePath);
        orig.delete();
        File destination = new File(destpath);


        for(File f:destination.listFiles()){
            if(f.getName().toLowerCase().endsWith(".zip")) {
                answer.addAll(unzipFunction(destpath + f.getName(), destpath));
                f.delete();
            }else
                answer.add(f);
        }
        return answer;
    }


    public LinkedList<DataverseRecordFile> unzip(String filePath, String destPath, DataverseRecordFile dRF, DataverseJavaObject djo ) throws NullPointerException{
        LinkedList<File> files = new LinkedList<>();
        File destDir = new File(destPath);
        destDir.mkdirs();
        Path path = Paths.get(destPath);
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();
        files = unzipFunction(filePath,destPath);
        for(File f: files){
            DataverseRecordFile temp = new DataverseRecordFile(dRF);
            temp.setTranslatedTitle(f.getName());
            drfs.add(temp);
        }
        return drfs;
    }


    private File newFile(File destDir, ZipEntry zipEntry) {
        File destFile = new File(destDir, zipEntry.getName());

        String destDirPath = null;
        try {
            destDirPath = destDir.getCanonicalPath();
            String destFilePath = destFile.getCanonicalPath();

            if (!destFilePath.startsWith(destDirPath + File.separator)) {
                throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
            }
        } catch (IOException e) {
            logger.error("Something went wrong create file while unzipping " + destDir);
        }
        return destFile;
    }

    public void deleteUnzippedFiles(String filePath){
        File f = new File(filePath.substring(0,filePath.length()-4));
        deleteDir(f);
    }
    private void deleteDir(File f) {
        File[] files = f.listFiles();
        if(files != null) {
            for (File myFile : files) {
                if (myFile.isDirectory()) {
                    deleteDir(myFile);
                }
                myFile.delete();
            }
        }
    }
}
