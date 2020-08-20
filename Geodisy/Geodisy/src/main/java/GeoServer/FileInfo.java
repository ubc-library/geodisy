package GeoServer;

import java.io.File;

public class FileInfo {
     File file;
     String origName;

    public FileInfo(File file, String origName) {
        this.file = file;
        this.origName = origName;
    }

    public File getFile() {
        return file;
    }

    public String getOrigName() {
        return origName;
    }

    public String getFileName(){
        return file.getName();
    }
}
