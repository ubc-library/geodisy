public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Switcher sw = new Switcher();
        String baseLocation = "/var/www/206-12-92-97.cloud.computecanada.ca/html/geodisy/";
        String startText = "https://dvtest.scholarsportal.info/";
        String endText = "https://dataverse.scholarsportal.info/";
        sw.updateFile(baseLocation, startText,endText);
    }

}