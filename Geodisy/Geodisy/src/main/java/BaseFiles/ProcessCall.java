package BaseFiles;

import GeoServer.HTTPCallerGeosever;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ProcessCall {
    ExecutorService executorService;
    GeoLogger logger;


    public ProcessCall() {
        executorService = Executors.newSingleThreadExecutor();

    }
    public String runProcess(String s, int time, TimeUnit unit, GeoLogger logger) throws TimeoutException,FileNotFoundException, ExecutionException, InterruptedException  {
        List<String> args = new LinkedList<>();
        args.add("/usr/bin/bash");
        args.add("-c");
        return runProcess(s, time, unit, args, logger);
    }


    public String runProcess(String s, int time, TimeUnit unit,  List<String> args, GeoLogger logger) throws TimeoutException,FileNotFoundException, ExecutionException, InterruptedException  {
        this.logger = logger;
        SubProcess process = new SubProcess(s, args, logger);
        Future<String> future = executorService.submit(process);
        executorService.shutdown();
        if (!executorService.awaitTermination(time, unit)) {
            throw new TimeoutException();
        }
        String answer = future.get();
        return future.get();
    }

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
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    result.append(line);
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
            System.out.println("Got something");
            return result.toString();
        }
    }
}
