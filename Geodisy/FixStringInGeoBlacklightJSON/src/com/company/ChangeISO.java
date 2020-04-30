package com.company;

import com.sun.xml.internal.bind.api.impl.NameConverter;
import sun.misc.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ChangeISO {

    public void alterXMLFiles(File folder){
        for(File f: folder.listFiles()){
            System.out.println("File to process " + f.getPath());
            if(f.isDirectory())
                alterXMLFiles(f);
            else{
                if(f.getName().equals("iso19115.xml")){
                    String fullpath = f.getPath();
                    String path = fullpath.substring(0,fullpath.indexOf(f.getName()));
                    File newName = new File(path+"iso19139.xml");
                    f.renameTo(newName);
                    createZip(newName);
                    break;
                }
        }
    }
}

    private boolean createZip(File file) {
        byte[] buf = new byte[1024];
        System.out.println("Zipping " + file.getPath());
        try {
            String path = file.getPath();
            FileOutputStream fos = new FileOutputStream(path.substring(0,path.indexOf("xml"))+"zip");
            ZipOutputStream zipOS = new ZipOutputStream(fos);
            FileInputStream in  = new FileInputStream(file.getPath());
            zipOS.putNextEntry(new ZipEntry("iso19139.xml"));
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
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    public void alterJSON(File folder){
        for(File f: folder.listFiles()){
            if(f.isDirectory())
                alterJSON(f);
            else if(f.getName().equals("geoblacklight.json"))
                fixJSON(f);
        }
    }

    private void fixJSON(File f) {
        Path path = Paths.get(f.getPath());
        Charset charset = StandardCharsets.UTF_8;

        try {
            String content = new String(Files.readAllBytes(path),charset);
            content = content.replace("iso19115.zip","iso19139.zip");
            Files.write(path,content.getBytes(charset));
        } catch (IOException e) {
            System.out.println("Something went wrong trying to update the json in path " + path);
        }
    }
}
