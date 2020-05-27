package Crosswalking.GeoBlacklightJson;

public abstract class JSONCreator {

    protected String stringed(String string) {
        return "\"" + string + "\"";
    }

    protected String endStringed(String string){
        return string + "\"";
    }

}
