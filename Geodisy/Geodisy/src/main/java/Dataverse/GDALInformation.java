package Dataverse;

public class GDALInformation {
    private String fullGdalString = "";
    private String projection = "";
    private String nativeCRS = "";
    private String minX = "";
    private String maxX = "";
    private String minY = "";
    private String maxY = "";

    private void extractCRS(boolean isRaster){
        try{
            if(isRaster) {
                    int start = fullGdalString.indexOf("PROJCS[");
                    String temp = fullGdalString.substring(start);
                    int count = 1;
                    int location = 0;
                    int end = temp.indexOf("Origin = (");
                    if (end != -1 && temp.charAt(end - 1) == ']')
                        nativeCRS = temp.substring(0, end);
                    else {
                        temp = temp.substring(7);
                        int size = temp.length();
                        while (location < size && count > 0) {
                            if (temp.charAt(location) == '[')
                                count++;
                            if (temp.charAt(location) == ']')
                                count--;
                            location++;
                        }
                        if (count == 0) ;
                        nativeCRS = fullGdalString.substring(start, location + start + 7);
                    }

            } else {

                int start = fullGdalString.indexOf("GEOGCS[");
                String temp = fullGdalString.substring(start);
                int count = 1;
                int location = 0;
                int end = temp.indexOf("FID Column =");
                if (end != -1 && temp.charAt(end - 1) == ']')
                    nativeCRS = temp.substring(0, end);
                else {
                    temp = temp.substring(7);
                    int size = temp.length();
                    while (location < size && count > 0) {
                        if (temp.charAt(location) == '[')
                            count++;
                        if (temp.charAt(location) == ']')
                            count--;
                        location++;
                    }
                    if (count == 0) ;
                    nativeCRS = fullGdalString.substring(start, location + start + 7);
                }
            }
        }catch (IndexOutOfBoundsException e){
            return;
        }
    }

    public String getFullGdalString() {
        return fullGdalString;
    }

    public void setFullGdalString(String fullGdalString, boolean raster) {
        this.fullGdalString = fullGdalString;
        extractCRS(raster);
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getNativeCRS() {
        return nativeCRS;
    }

    public void setNativeCRS(String nativeCRS) {
        this.nativeCRS = nativeCRS;
    }

    public String getMinX() {
        return minX;
    }

    public void setMinX(String minX) {
        this.minX = minX;
    }

    public String getMaxX() {
        return maxX;
    }

    public void setMaxX(String maxX) {
        this.maxX = maxX;
    }

    public String getMinY() {
        return minY;
    }

    public void setMinY(String minY) {
        this.minY = minY;
    }

    public String getMaxY() {
        return maxY;
    }

    public void setMaxY(String maxY) {
        this.maxY = maxY;
    }
}
