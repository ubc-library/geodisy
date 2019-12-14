package BaseFiles;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
    GeoLogger logger = new GeoLogger(this.getClass());

    public boolean create(File file) {
        try {
            String path = file.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(path.substring(0,path.length()-3)+"zip");
            ZipOutputStream zipOS = new ZipOutputStream(fos);
            writeToZipFile(file, zipOS);
            zipOS.close();
            fos.close();

        } catch (FileNotFoundException e) {
            logger.error("Something went wrong creating a zip of the ISO XML file, the XML file couldn't be found");
            return false;
        } catch (IOException e) {
            logger.error("Something went wrong creating a zip of the ISO XML file, the zip couldn't be made");
            return false;
        }
    return true;
    }

    private void writeToZipFile(File file, ZipOutputStream zipStream) throws FileNotFoundException, IOException{
        File aFile = new File(file.getAbsolutePath());
        FileInputStream fis = new FileInputStream(aFile);
        ZipEntry zipEntry = new ZipEntry(file.getAbsolutePath());
        zipStream.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipStream.write(bytes, 0, length);
        }

        zipStream.closeEntry();
        fis.close();
    }
}
