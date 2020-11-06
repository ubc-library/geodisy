package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FixShpFolder {
    public FixShpFolder() {
    }
    public void fix(File directory) {
        boolean needsfixing = false;
        File badDir = new File("test");
        if (directory.isDirectory()) {
            for (File f : directory.listFiles()) {
                if(f.isDirectory()){
                    if(f.getName().endsWith(".shp")) {
                        for (File f2 : directory.listFiles()) {
                            String name = f2.getName();
                            if (f2.isFile() && shapeRelated(name) && name.equals(f.getName())) {
                                f2.delete();
                            }
                            if(f2.isDirectory()&&!name.equals(f.getName())) {
                                for(File f3:f2.listFiles()){
                                    f3.delete();
                                }
                                f2.delete();
                            }
                        }
                        needsfixing = true;
                        badDir = f;
                        break;
                    } else {
                        fix(f);
                    }
                }
            }
            if(needsfixing)
                doChange(badDir);
        }

    }

    private void doChange(File directory) {
        String pathPlusName = directory.getAbsolutePath();

        int lastSlash = pathPlusName.lastIndexOf("/");
        String path = pathPlusName.substring(0,lastSlash);
        String directoryRename = path + "/temp";
        try {
            Files.move(Paths.get(pathPlusName),Paths.get(path+"/temp"), REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        directory = new File(path+"/temp");
        for(File f: directory.listFiles()){
            String source = f.getAbsolutePath();
            String dest = path + "/" + f.getName();
            File destFile = new File(dest);
            destFile.delete();
            try {
                Files.move(Paths.get(source), Paths.get(dest));
            } catch (IOException e) {
                System.out.println("Something went wrong moving the file: source = " + source + " dest = " + dest);
            }

        }
        directory.delete();

    }

    private boolean shapeRelated(String name) {
        String[] SHAPEFILE_EXTENSIONS = {".shx", ".dbf", ".sbn",".prj", "shp"};
            for(String s : SHAPEFILE_EXTENSIONS){
                if(name.toLowerCase().endsWith(s))
                    return true;
            }
            return false;
    }
}
