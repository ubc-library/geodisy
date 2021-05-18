package BaseFiles;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static _Strings.GeodisyStrings.ISO_19139_XML;

public class Zip {
    GeoLogger logger = new GeoLogger(this.getClass());

    public boolean create(File file) {
        byte[] buf = new byte[1024];
        try {
            String path = file.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(path.substring(0,path.lastIndexOf("xml"))+"zip");
            ZipOutputStream zipOS = new ZipOutputStream(fos);
            FileInputStream in  = new FileInputStream(file.getAbsolutePath());
            zipOS.putNextEntry(new ZipEntry(ISO_19139_XML));
            int len;
            while ((len = in.read(buf)) > 0) {
                zipOS.write(buf, 0, len);
            }
            // Complete the entry
            zipOS.closeEntry();
            zipOS.close();
            in.close();
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
        FileInputStream fis = new FileInputStream(aFile.getName());
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
