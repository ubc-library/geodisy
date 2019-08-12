package Crosswalking.XML;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.XML;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static Crosswalking.XML.XMLStrings.OPEN_METADATA_LOCAL_REPO;

public class JGIT {
    Git git;

    public JGIT() {
        File openMetaDataRepo = new File(OPEN_METADATA_LOCAL_REPO);
        try
                (Git git = Git.open(openMetaDataRepo)) {
        } catch (IOException e) {
            //TODO figure out error message to log
            e.printStackTrace();
        }
    }

    /**
     * Creates new ISO XML files, saves them to the local OpenMetadata repo, commits the changes, and pushes to the remote OpenMetadataRepo
     * @param docs
     */
    public void updateXML(LinkedList<XMLDocument> docs){
        try {
            generateNewXMLFile(docs);
        } catch (TransformerException e) {
            //TODO figure out error message;

        } catch (GitAPIException e) {
            //TODO figure out what this error message should be
        }
        pushXMLToGit();
    }

    /**
     * Pushes new XML files to the OpenMetadata Repo to be then harvested by Geocombine and sent to SOLR
     */
    private void pushXMLToGit() {
        //TODO write this code


    }

    //TODO
    private void generateNewXMLFile(LinkedList<XMLDocument> docs) throws TransformerException, GitAPIException {
        for(XMLDocument doc: docs){
            //TODO figure out if XML file name should be what is currently in xMLFileName
            String xMLFileName =OPEN_METADATA_LOCAL_REPO + "/" + doc.doi + ".xml";
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc.getDoc());
            StreamResult streamResult = new StreamResult(new File(xMLFileName));
            transformer.transform(domSource, streamResult);
            addXMLFileToIndex(xMLFileName);
        }
        RevCommit commit = git.commit().setMessage(docs.size() + " ISO XML files created").call();
        pushXMLToGit();
    }

    private void addXMLFileToIndex(String fileName){
        try {
            DirCache index = git.add().addFilepattern(OPEN_METADATA_LOCAL_REPO + "/" + fileName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

}
