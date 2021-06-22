package BaseFiles;

import Dataverse.DataverseRecordFile;
import GeoServer.FileInfo;
import GeoServer.Unzip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.*;

public class ProcessCallUnzip extends ProcessCall{
    public LinkedList<FileInfo> unzipFile(String filePath, String destination, GeoLogger logger) throws TimeoutException, InterruptedException, ExecutionException {
        SubProcess process = new SubProcess(logger, filePath, destination);
        Future<LinkedList<FileInfo>> future = executorService.submit(process);
        executorService.shutdown();
        if (!executorService.awaitTermination(20, TimeUnit.MINUTES)) {
            throw new TimeoutException();
        }
        return future.get();
    }
    class SubProcess implements Callable<LinkedList<FileInfo>> {
        GeoLogger logger;
        String filePath, destination;

        public SubProcess(GeoLogger logger, String filePath, String destination) {
            this.logger = logger;
            this.filePath = filePath;
            this.destination = destination;
        }

        @Override
        public LinkedList<FileInfo> call() throws FileNotFoundException, IOException {
            Unzip unzip = new Unzip();
            return unzip.unzipFunction(filePath,destination);
        }
    }
}
