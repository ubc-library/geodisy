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

    private LinkedList<FileInfo> unzipFunction(String filePath, String destpath){
        LinkedList<FileInfo> answer = new LinkedList<>();
        String slash = GeodisyStrings.windowsComputerType()? "\\":"/";
        String basename = filePath.substring(filePath.lastIndexOf(slash)+1,filePath.lastIndexOf("."));
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
                String newFileName = fileName.substring(0,fileName.lastIndexOf("."));
                if(!newFileName.equals(basename)) {
                    fileName = basename + "___" + fileName;
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
                if(fileName.toLowerCase().endsWith(".zip")) {
                    answer.addAll(unzipFunction(destpath + fileName, destpath));
                    newFile.delete();
                }else{
                    if(GeodisyStrings.fileToAllow(newFile.getName()))
                        answer.add(new FileInfo(newFile,basename+".zip"));
                }
            }

            zis.closeEntry();
            zis.close();

        } catch (IOException | IllegalArgumentException ex) {
            logger.error("Something went wrong trying to parse: " + filePath + " Stack:" + ex.toString());
        }
        File orig = new File(filePath);
        orig.delete();


        return answer;
    }


    public LinkedList<DataverseRecordFile> unzip(String filePath, String destPath, DataverseRecordFile dRF, DataverseJavaObject djo ) throws NullPointerException{
        LinkedList<FileInfo> files;
        File destDir = new File(destPath);
        destDir.mkdirs();
        LinkedList<DataverseRecordFile> drfs = new LinkedList<>();
        files = unzipFunction(filePath,destPath);
        for(FileInfo f: files){
            DataverseRecordFile temp = new DataverseRecordFile(dRF);
            temp.setTranslatedTitle(f.getFileName());
            temp.setOriginalTitle(f.getOrigName());
            temp.setFileIdent(f.getFile().getAbsolutePath());
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
