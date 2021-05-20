package BaseFiles;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;

public class ProcessCallError extends ProcessCall{
    class SubProcess implements Callable<String> {
        String call;
        ProcessBuilder processBuilder;
        List<String> args;
        GeoLogger logger;

        public SubProcess(String call, List<String> args, GeoLogger logger) {
            this.call = call;
            processBuilder = new ProcessBuilder();
            this.args = args;
            args.add(call);
            this.logger = logger;
        }
        @Override
        public String call() throws FileNotFoundException, IOException  {
            processBuilder.command(args);
            Process p = processBuilder.start();
            StringBuilder result = new StringBuilder();
            try{
                BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line;
                while ((line = error.readLine()) != null) {
                    System.out.println(line);
                    result.append(line).append("\n");
                }
                p.waitFor();
                p.destroy();
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFound");
                throw new FileNotFoundException();
            } catch (InterruptedException e) {
                System.out.println("IOException");
                throw new IOException();
            }
            return result.toString();
        }
    }
}
