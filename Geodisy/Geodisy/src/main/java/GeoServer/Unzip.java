package GeoServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * Class for unzipping a file right before uploading things to Geosever
 * and then deleting the unzipped files after the upload process is finished
 */
public class Unzip {
    Logger logger = LogManager.getLogger(Unzip.class);
    //TODO call unzip when adding zipped files to Geoserver and then call deleteUnzippedFiles() after upload is done to save space
    public void unzip(String filePath) {
        String destPath = filePath.substring(0,filePath.length()-4);
        File destDir = new File(destPath);

        Path path = Paths.get(destPath);

        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                logger.error("Not sure how creating a directory could fail, but it did. Check " + destPath);
            }
        }

        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            logger.error("Something went wrong unzipping" + filePath + ". The file couldn't be found somehow.");
        }
        ZipEntry zipEntry = null;
        try {
            zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (FileNotFoundException e) {
            logger.error("Something went wrong creating new file (File wasn't found, somehow) " + filePath);
        }catch (IOException e) {
            logger.error("Something went wrong unzipping " + filePath);
        }
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
