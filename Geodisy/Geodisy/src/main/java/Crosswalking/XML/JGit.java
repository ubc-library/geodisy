package Crosswalking.XML;

import BaseFiles.GeoLogger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static Crosswalking.XML.XMLStrings.*;

public class JGit {
    private Git git;
    private Repository xmlRepo;
    private GeoLogger logger;

    public JGit() {
        logger = new GeoLogger(this.getClass());
        //Make the directory if it doesn't already exist
        File directory = new File(OPEN_METADATA_LOCAL_REPO);
        if (! directory.exists()) {
            directory.mkdir();
        }

        try {
            Repository localRepo = new FileRepository(OPEN_METADATA_LOCAL_REPO);
            Git git = new Git(localRepo);

            //add remote repo
            RemoteAddCommand remoteAddCommand = git.remoteAdd();
            remoteAddCommand.setName("origin");
            remoteAddCommand.setUri(new URIish(OPEN_METADATA_REMOTE_REPO));
            // you can add more settings here if needed
            remoteAddCommand.call();
        } catch(IOException e){
            logger.error("Something happened when trying to connect to the XML repository (local or remote");
        } catch (GitAPIException e) {
            logger.error("Something went wrong trying to add the remote repo");
        } catch (URISyntaxException e) {
            logger.error("Something wrong with the URI");
        }
    }

    /**
     * Creates new ISO XML files, saves them to the local OpenMetadata repo, commits the changes, and pushes to the remote OpenMetadataRepo
     * @param docs = XML document objects that need to be used to create actual XML files
     */
    public void updateXML(List<XMLDocument> docs){
        try {
            generateNewXMLFiles(docs);
            pushXMLToGit();
            System.out.println("Pushed from repository: " + xmlRepo.getDirectory() + " to remote repository at " + "[Insert remote repo address");
        } catch (GitAPIException | TransformerException e) {
            logger.error("Something went wrong with Git when either connecting to .");
        }

    }

    /**
     * Pushes new XML files to the OpenMetadata Repo to be then harvested by Geocombine and sent to SOLR
     */
    private void pushXMLToGit() throws GitAPIException {
        //TODO is this really all I need?
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(OPEN_METADATA_REMOTE_USERNAME,OPEN_METADATA_REMOTE_PASSWORD));
        pushCommand.call();
    }
    public void testgenerateNewXMLFile(LinkedList<XMLDocument> docs){
        try {
            generateNewXMLFiles(docs);
        } catch (GitAPIException e) {
            logger.error("Something went wrong creating new XML files.");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    private void generateNewXMLFiles(List<XMLDocument> docs) throws GitAPIException, TransformerException {
        for(XMLDocument doc: docs){
            //TODO figure out if XML file name should be what is currently in xMLFileName
            String xMLFileName =getFilePath(doc.doi) + ".xml";
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
            DirCache index = git.add().addFilepattern(OPEN_METADATA_LOCAL_REPO + fileName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
    public String getFilePath(String doiName){
        String current = doiName;
        String filePath = OPEN_METADATA_LOCAL_REPO;
        filePath += "doi/";
        checkDir(filePath);
        Queue<String> pathQueue = new LinkedList<>();
        pathQueue.add("doi/");
        current = current.substring(current.indexOf("doi.org" + 16));
        String temp = current.substring(0,current.indexOf("/")+1);
        checkDir(temp);
        pathQueue.add(temp);
        current = current.substring(current.indexOf("/")+1);
        temp = current.substring(0,current.indexOf("/")+1);
        checkDir(temp);
        pathQueue.add(temp);
        current = current.substring(current.indexOf("/")+1);
        checkDir(current+"/");
        pathQueue.add(current+"/");

        while(!pathQueue.isEmpty()){
            filePath+= pathQueue.poll();
        }
        if(filePath.isEmpty())
            logger.error("something is wrong with the doi in filename: " + doiName);
        return filePath;
    }

    private void checkDir(String filePath) {
        File directory = new File(filePath);
        if (! directory.exists()) {
            directory.mkdir();
        }
    }
}
