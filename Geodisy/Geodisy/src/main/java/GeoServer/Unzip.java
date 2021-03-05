package GeoServer;

import BaseFiles.GeoLogger;
import _Strings.GeodisyStrings;

import Dataverse.DataverseJavaObject;
import Dataverse.DataverseRecordFile;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.LinkedList;

import java.util.zip.ZipEntry;

/**
 * Class for unzipping a file right before uploading things to Geosever
 * and then deleting the unzipped files after the uploadVector process is finished
 */
public class Unzip {
    GeoLogger logger;

    public Unzip() {
        logger = new GeoLogger(this.getClass());
    }

    public LinkedList<FileInfo> unzipFunction(String zipfilePath, String destpath){
        if(!destpath.endsWith(GeodisyStrings.replaceSlashes("/")))
            destpath = destpath + GeodisyStrings.replaceSlashes("/");
        LinkedList<FileInfo> answer = new LinkedList<>();
        String basename = zipfilePath.substring(zipfilePath.lastIndexOf(GeodisyStrings.replaceSlashes("/"))+1,zipfilePath.lastIndexOf("."));

        try {
            //create output directory is not exists
            File folder = new File(destpath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipArchiveInputStream zis = new ZipArchiveInputStream(new FileInputStream(zipfilePath));
            //get the zipped file list entry
            ZipArchiveEntry ze = zis.getNextZipEntry();

            while (ze != null) {
                String fileName = ze.getName();
                if (ze.isDirectory()||GeodisyStrings.fileTypesToIgnore(fileName.toLowerCase())) {
                    ze = zis.getNextZipEntry();
                    continue;
                }
                String newFileName = fileName.substring(0,fileName.lastIndexOf("."));
                if(!newFileName.equals(basename)) {
                    fileName = basename + "___" + fileName;
                }
                String filepath = destpath + fileName;
                if(!ze.isDirectory()){
                    extractFile(zis,filepath);
                } else{
                    File dir = new File(filepath);
                    dir.mkdirs();
                }
                if(fileName.toLowerCase().endsWith(".zip")) {
                    answer.addAll(unzipFunction(destpath + fileName, destpath));
                }else{
                    if(GeodisyStrings.fileToAllow(fileName))
                        answer.add(new FileInfo(new File(filepath),basename+".zip"));
                }
                ze = zis.getNextZipEntry();
            }

            zis.close();
            Files.deleteIfExists(Paths.get(zipfilePath));

        } catch (IOException | IllegalArgumentException ex) {
            logger.error("Something went wrong trying to parse: " + zipfilePath + " Stack:" + ex.toString());
        }
        return answer;
    }

    private void extractFile(ZipArchiveInputStream zis, String filepath) throws IOException {
        File entryDestination =  new File(filepath);
        try (OutputStream out = new FileOutputStream(entryDestination)) {
            IOUtils.copy(zis, out, 8024);
        }
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
                try {
                    Files.deleteIfExists(myFile.toPath());
                } catch (IOException e) {
                    logger.error("Something went wrong deleting folder " + myFile.getAbsolutePath());
                }
            }
        }
    }
}
