package Crosswalking.XML.XMLTools;

import BaseFiles.GeoLogger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.URIish;


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
import java.util.Set;

import static Crosswalking.XML.XMLTools.XMLStrings.*;

/**
 * Class for dealing with creating XML files from XML Docs, deleting files, and keeping Github up to date
 */
public class JGit {
    private Git git;
    private Repository xmlRepo;
    private GeoLogger logger;
    private String localRepoLocation;

    public JGit() {
        //Make the directory if it doesn't already exist
        localRepoLocation = OPEN_METADATA_LOCAL_REPO;
        secondaryPartOfConstructor();
    }

    public JGit(String localRepoLocation){
        this.localRepoLocation = localRepoLocation;
        secondaryPartOfConstructor();
    }
    private void secondaryPartOfConstructor(){
        logger = new GeoLogger(this.getClass());
        File directory = new File(localRepoLocation);
        if (! directory.exists()) {
            directory.mkdir();
        }

        try {
            xmlRepo = new FileRepository(localRepoLocation);
            git = new Git(xmlRepo);

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
    public void updateXML(List<XMLDocObject> docs){
        try {
            generateNewXMLFiles(docs);
            pushToGit();
            System.out.println("Pushed from repository: " + xmlRepo.getDirectory() + " to remote repository at " + "[Insert remote repo address");
        } catch (GitAPIException | TransformerException e) {
            logger.error("Something went wrong with Git when either connecting to .");
        }

    }

    /**
     * Pushes new XML files to the OpenMetadata Repo to be then harvested by Geocombine and sent to SOLR
     */
    public void pushToGit() {
        //TODO is this really all I need?
        //TODO uncomment in production environment
        /*
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(OPEN_METADATA_REMOTE_USERNAME,OPEN_METADATA_REMOTE_PASSWORD));
        try {
            pushCommand.call();
        } catch (GitAPIException e) {
            logger.error("Soemthing went wrong trying to push to repo");
        }*/
    }

    /**
     * Used only for testing
     * @param docs
     */
    public void testgenerateNewXMLFile(LinkedList<XMLDocObject> docs){
        try {
            generateNewXMLFiles(docs);
        } catch (GitAPIException e) {
            logger.error("Something went wrong creating new XML files.");
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates XML files from the XML Docs, adds them to the git index, and pushes them to the remote repo
     * @param docs
     * @throws GitAPIException
     * @throws TransformerException
     */
    private void generateNewXMLFiles(List<XMLDocObject> docs) throws GitAPIException, TransformerException {
        for(XMLDocObject doc: docs){
            //TODO figure out if XML file name should be what is currently in xMLFileName
            String xMLFileName = getXMLLocalFilePath(doc.doi) + ".xml";
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc.getDoc());
            StreamResult streamResult = new StreamResult(new File(xMLFileName));
            transformer.transform(domSource, streamResult);
            addXMLFileToIndex(xMLFileName);
        }
        RevCommit commit = git.commit().setMessage(docs.size() + " ISO XML files created").call();
        pushToGit();
    }

    /**
     * XML files need to be added to the index in order to be git committed
     * @param fileName
     */
    public void addXMLFileToIndex(String fileName){
        try {
            DirCache index = git.add().addFilepattern(localRepoLocation + fileName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a string with the path to where the XML file should go
     * @param doiName
     * @return
     */
    public String getXMLLocalFilePath(String doiName){
        String current = doiName;
        String filePath = localRepoLocation;
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

    /**
     * Delete XML files locally and in repo if they got deleted from harvested data repository
     * @param dois
     */
    public void deleteXMLFiles(Set<String> dois){
        String badname = "";
        try {
            //add setCached(false). if files are not getting deleted from the working directory
            for(String name: dois) {
                badname = name;
                DirCache index = git.rm().addFilepattern(getXMLLocalFilePath(name) + ".xml").call();
            }
        } catch (GitAPIException e) {
            logger.error("Something went wrong trying to delete file: " + badname);
        }

        pushToGit();



    }

    /**
     * Create a directory if it doesn't already exist
     * @param filePath
     */
    private void checkDir(String filePath) {
        File directory = new File(filePath);
        if (! directory.exists()) {
            directory.mkdir();
        }
    }

}
