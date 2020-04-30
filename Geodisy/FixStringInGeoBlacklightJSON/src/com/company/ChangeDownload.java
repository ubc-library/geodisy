package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChangeDownload {
    public void alterJSON(File folder, String start, String end){
        for(File f: folder.listFiles()){
            if(f.isDirectory())
                alterJSON(f, start, end);
            else if(f.getName().equals("geoblacklight.json"))
                fixJSON(f,start, end);
        }
    }

    private void fixJSON(File f, String start, String end) {
        Path path = Paths.get(f.getPath());
        Charset charset = StandardCharsets.UTF_8;

        try {
            String content = new String(Files.readAllBytes(path),charset);
            content = content.replace(start,end);
            Files.write(path,content.getBytes(charset));
        } catch (IOException e) {
            System.out.println("Something went wrong trying to update the json in path " + path);
        }
    }
}
