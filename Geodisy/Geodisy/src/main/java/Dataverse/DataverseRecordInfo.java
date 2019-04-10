package Dataverse;

import java.io.Serializable;

public class DataverseRecordInfo implements Serializable {
    private int major;
    private int minor;
    private String doi;
    private String server;

    public DataverseRecordInfo(String server) {
        this.server = server;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof DataverseRecordInfo))
            return false;
        if (obj == this)
            return true;
        Boolean equals = this.getMajor()==((DataverseRecordInfo) obj).getMajor()
                && this.getMinor()==((DataverseRecordInfo) obj).getMinor();
        return equals;
    }

    public boolean younger(Object obj) {
        if (obj == null) return true;
        if (obj == this)
            return false;
        Boolean younger = this.getMajor() >((DataverseRecordInfo) obj).getMajor() || (this.getMajor()==((DataverseRecordInfo) obj).getMajor() && this.getMinor() >((DataverseRecordInfo) obj).getMinor());

        return younger;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getDoi() {
        return doi;
    }

    public String getServer() {
        return server;
    }
    @Override
    public int hashCode()
    {
        int prime = 31;
        return prime + (doi == null ? 0 : doi.hashCode());
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
