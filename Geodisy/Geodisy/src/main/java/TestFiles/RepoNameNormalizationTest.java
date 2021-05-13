package TestFiles;

import _Strings.GeodisyStrings;

public class RepoNameNormalizationTest implements Test{
    @Override
    public void run() {
        String repo = "https://open.alberta.ca/opendata/9865afee-8127-40b6-8172-543006ccc5f4";
        String path = GeodisyStrings.removeHTTPSAndReplaceAuthority(repo);
        System.out.println(path);
    }
}
