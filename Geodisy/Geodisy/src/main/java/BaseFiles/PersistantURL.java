package BaseFiles;

public class PersistantURL {
    String origValue;
    String fullStub;
    String type;
    String stubMinusType;
    String fullBase;
    String authority = "";

    public PersistantURL(String origValue) {
        this.origValue = origValue;
        int start = origValue.contains("http://")? 7 : -1;
        fullStub = start!= -1? origValue.substring(start):origValue;
        int end = origValue.length();
        if(origValue.contains("doi:")||origValue.contains("doi.org")) {
            type = "doi";
            if(origValue.contains("doi:")) {
                end = origValue.indexOf("doi:") + 4;
                stubMinusType = origValue.substring(end);
            }
            else {
                end = origValue.indexOf("doi.org") + 8;
                stubMinusType = origValue.substring(fullStub.indexOf("doi.org") + 8);
            }
        }
        else if(origValue.contains("handle:")||origValue.contains("handle.net")) {
            type = "handle";
            if(origValue.contains("handle:")) {
                end = origValue.indexOf("handle:") + 7;
                stubMinusType = origValue.substring(end);
            }
            else {
                end = origValue.indexOf("handle.net") + 11;
                stubMinusType = origValue.substring(origValue.indexOf("handle.net") + 11);
            }
        }
        else {
            type = "other";
            stubMinusType = fullStub;
        }
        fullBase = origValue.substring(0,end);
    }

    public String getId(){
        return stubMinusType.replace("\\.","_").replace("/","_");
    }

    public String getPId(){
        return stubMinusType;
    }

    public String getPUrl(){
        return origValue;
    }

    public void addAuthority(String authority){
        this.authority = authority;
    }
}
