import java.io.*;

public class Switcher {
    public void updateFile(String baseLocation, String startingText, String endingText){
        File directory = new File(baseLocation);
        if(directory.isDirectory()){
            for(File f:directory.listFiles()){
                updateFile(f.getAbsolutePath(),startingText,endingText);
            }
        }else{
            if(directory.getName().equals("geoblacklight.json")) {
                File fileToBeModified = new File(baseLocation);
                String oldContent = "";
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified));
                    String line = reader.readLine();

                    while (line != null) {
                        oldContent = oldContent + line + System.lineSeparator();
                        line = reader.readLine();
                    }
                    String newContent = oldContent.replaceAll(startingText, endingText);
                    FileWriter writer = new FileWriter(fileToBeModified);
                    writer.write(newContent);
                    reader.close();
                    writer.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Couldn't find file:" + baseLocation);
                } catch (IOException e) {
                    System.out.println("Something went wrong reading or writing " + baseLocation);
                }
            }
        }
    }
}
