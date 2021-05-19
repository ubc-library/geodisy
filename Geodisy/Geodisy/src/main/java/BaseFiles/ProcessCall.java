package BaseFiles;

import GeoServer.HTTPCallerGeosever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.*;

public class ProcessCall {
    ExecutorService executorService;
    GeoLogger logger;


    public ProcessCall() {
        executorService = Executors.newSingleThreadExecutor();

    }

    public String runProcess(String s, int time, TimeUnit unit, String message, String timeout, String generalError, List<String> args, GeoLogger logger){
        this.logger = logger;
        SubProcess proccess = new SubProcess(s, message, args, logger);
        Future<String> future = executorService.submit(proccess);
        executorService.shutdown();
        try{
            if(!executorService.awaitTermination(30, TimeUnit.SECONDS)){
                logger.warn(timeout);
                return "falure";
            }
        }catch (InterruptedException e){
            logger.error(generalError);
            executorService.shutdownNow();
            return "falure";
        }
        String success;
        try{
            success = future.get();
        } catch (InterruptedException|ExecutionException e) {
            return "falure";
        }
        return success;
    }

    class SubProcess implements Callable<String> {
        String call;
        String message;
        HTTPCallerGeosever caller;
        ProcessBuilder processBuilder;
        List<String> args;
        GeoLogger logger;

        public SubProcess(String call, String message, List<String> args, GeoLogger logger) {
            this.call = call;
            this.message = message;
            caller = new HTTPCallerGeosever();
            processBuilder = new ProcessBuilder();
            this.args = args;
            args.add(call);
            this.logger = logger;
        }

        @Override
        public String call() throws Exception {
            System.out.println(message + call);
            processBuilder.command(args);
            Process p = processBuilder.start();
            String result = "";
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                p.waitFor();
                p.destroy();
            } catch (IOException | InterruptedException e) {
                throw new Exception();
            }
            return result;
        }
    }
}
