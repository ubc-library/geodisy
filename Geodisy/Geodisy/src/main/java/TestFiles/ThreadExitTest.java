package TestFiles;

import java.util.concurrent.*;

public class ThreadExitTest implements Test{
    public void run() {
        Funct funct = new Funct();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Boolean> fuBoo = executorService.submit(funct);
        executorService.shutdown();

        try {
            System.out.println(fuBoo.get(1, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        try{
            if(!executorService.awaitTermination(2000, TimeUnit.MILLISECONDS)){
                System.out.println("ERROR: Stopping going to next step");
            }
        }catch (InterruptedException e){
            System.out.println("Exited incorrectly");
        }
        System.out.println("Exited without interrupt");
    }
    private class Funct implements Callable<Boolean> {
        public Boolean booli;
        private void funct() {
            int x = 1;
            while (x == 1) {
            }
        }

        @Override
        public Boolean call() {
            int x = 1;
            while(x==1){}
            return booli;
        }
    }


}
