package BaseFiles;

import java.io.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessCallFileDownload extends ProcessCall{

    public void downloadFile(String fileURL, String fileName, String path, GeoLogger logger, int time, TimeUnit unit) throws TimeoutException, InterruptedException {
        SubProcess process = new SubProcess(fileURL,fileName,path,logger);
        executorService.submit(process);
        executorService.shutdown();
        if (!executorService.awaitTermination(time, unit)) {
            throw new TimeoutException();
        }
    }
    class SubProcess implements Callable<String[]> {
        GeoLogger logger;
        String fileName, fileURL, path;

        public SubProcess(String fileURL, String fileName, String path, GeoLogger logger) {
            this.fileName = fileName;
            this.fileURL = fileURL;
            this.path = path;
            this.logger = logger;
        }

        @Override
        public String[] call() throws FileNotFoundException, IOException {
            HTTPGetCall httpGetCall = new HTTPGetCall();
            httpGetCall.getFile(fileURL, fileName, path);
            return new String[0];
        }
    }
}
