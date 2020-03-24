package BaseFiles;

import java.io.IOException;

public class HTTPCallerDataverse extends HTTPCaller {

    @Override
    protected void ioError(IOException e) {
        logger.error("Something went wrong accessing Dataverse " + e);
    }
}
