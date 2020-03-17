package tests;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import static BaseFiles.GeodisyStrings.GIT_PASSWORD;


public class GitTest {

    private static String TEST_REMOTE_REPO = "https://github.com/pdante-ubc/GeodisyTestGit.git";
    private static String TEST_LOCAL_REPO = "XMLFilesTest/GeodisyTestGit/";
    private Repository repo;

    @BeforeEach
    void setUp() {
        try {
            getRepo(TEST_LOCAL_REPO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GitTest() {
    }

    @Test
    void name() {
        System.out.println("Created repository:");
    }

    public static Repository getRepo(String repoPath) throws IOException {
        Repository repo;
        File repoFile = new File(repoPath);
        File gitFile = repoFile.listFiles()[0].listFiles()[0];
        if (gitFile.exists()) {
            // Open an existing repository
            FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
            repositoryBuilder.setMustExist( true );
            repositoryBuilder.addCeilingDirectory(new File(repoPath));
            repositoryBuilder.findGitDir(new File(repoPath + "GeodisyTestGit/"));
            repo = repositoryBuilder.build();

        } else {
            // Create a new repository
            repo = FileRepositoryBuilder.create(
                    new File(repoPath));
            repo.create(true);
        }
        System.out.println(repo.getDirectory());
        Git git = new Git(repo);
        AddCommand add = git.add();
        add.addFilepattern("XMLFilesTest/GeodisyTestGit/GeodisyTestGit/Temp.txt");
        CommitCommand commit = git.commit();

        try {
            commit.setMessage("test:" + new Date()).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        RemoteAddCommand remote = git.remoteAdd();
        remote.setName("origin");
        try {
            remote.setUri(new URIish(TEST_REMOTE_REPO));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            remote.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        PushCommand push = git.push();
        try {
            push.setCredentialsProvider(new UsernamePasswordCredentialsProvider("pdante-ubc", GIT_PASSWORD));
            push.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return repo;
    }

}
