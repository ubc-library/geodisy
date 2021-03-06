package Crosswalking.XML.XMLTools;

import BaseFiles.GeoLogger;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static _Strings.GeodisyStrings.GEOCOMBINE;
import static _Strings.GeodisyStrings.GITCALL;
import static _Strings.XMLStrings.*;

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

    }

    public void updateRemoteMetadata(){
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("/bin/bash");
        cmdList.add("-c");
        cmdList.add(GITCALL);
        cmdList.add(OPEN_METADATA_LOCAL_REPO);
        cmdList.add(String.valueOf(ZonedDateTime.now(ZoneId.of("Canada/Pacific"))));
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process p = null;
        try{
            System.out.println("Pushing Metadata to OpenGeoMetaData");
            processBuilder.command(cmdList);
            processBuilder.redirectErrorStream(true);
            p = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
                continue;
            p.waitFor();
            p.destroy();
        } catch (IOException | InterruptedException e) {
            logger.error("Something went wrong pushing metadata to OpenGeoMetaData: " + e);
        }
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
