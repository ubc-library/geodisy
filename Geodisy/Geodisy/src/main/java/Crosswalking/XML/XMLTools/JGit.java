package Crosswalking.XML.XMLTools;

import BaseFiles.GeoLogger;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static Strings.XMLStrings.*;

/**
 * Class for dealing with creating XML files from XML Docs, deleting files, and keeping Github up to date
 */
public class JGit {
    private Git git;
    private Repository xmlRepo;
    private GeoLogger logger;
    private String localRepoLocation;

    public JGit() {
        logger = new GeoLogger(this.getClass());
        //Make the directory if it doesn't already exist
        localRepoLocation = OPEN_METADATA_LOCAL_REPO;
        File repo = new File(localRepoLocation);
        if(!repo.exists())
            cloneOpenGeoMetadataToLocal();
        else {
            accessLocal();
        }

    }

    public void updateRemoteMetadata(){
        String commitmessage = String.valueOf(ZonedDateTime.now(ZoneId.of("Canada/Pacific")));
        AddCommand add = git.add().addFilepattern(".");
        try {
            add.call();
        } catch (GitAPIException e) {
            logger.error("Something went wrong trying to add files to git at " + commitmessage);
        }
        CommitCommand commit = git.commit();
        commit.setMessage(commitmessage);
        pushToGit();

    }

    private void cloneOpenGeoMetadataToLocal() {
        try {
            git = Git.cloneRepository()
                    .setURI(OPEN_METADATA_REMOTE_REPO)
                    .setGitDir(new File(localRepoLocation))
                    .setCloneAllBranches(true)
                    .call();
        } catch (GitAPIException e) {
            logger.error("Something went wrong trying to clone " + OPEN_METADATA_REMOTE_REPO + " to " + localRepoLocation);
        }
    }

    private void accessLocal() {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        repositoryBuilder.setMustExist(true);
        repositoryBuilder.setGitDir(new File(localRepoLocation));
        try {
            xmlRepo = repositoryBuilder.build();
        } catch (IOException e) {
            logger.error("Something went wrong trying to access the local git repo at " + localRepoLocation);
        }
        git = new Git(xmlRepo);
    }

    public JGit(String localRepoLocation){
        this.localRepoLocation = localRepoLocation;
    }

    /**
     * Pushes new metadata files to the OpenMetadata Repo
     */
    public void pushToGit() {
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(OPEN_METADATA_REMOTE_USERNAME,OPEN_METADATA_REMOTE_PASSWORD));
        try {
            pushCommand.call();
        } catch (GitAPIException e) {
            logger.error("Something went wrong trying to push to repo");
        }
    }




    /**
     * Creates a string with the path to where the XML file should go
     * @param doiName
     * @return
     */
    public String getOpenGeoLocalFilePath(String doiName){
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
                DirCache index = git.rm().addFilepattern(getOpenGeoLocalFilePath(name) + ".xml").call();
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
