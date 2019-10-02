package tests;

import Crosswalking.XML.XMLTools.JGit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.FS;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;


public class GitTest {

    private String TEST_REMOTE_REPO = "https://github.com/pdante-ubc/GeodisyTestGit.git";
    private String TEST_LOCAL_REPO = "XMLFilesTest/GeodisyTestGit/";
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
        File gitFile = (File) repoFile.listFiles()[0].listFiles()[0];
        if (gitFile.exists()) {
            // Open an existing repository
            FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
            repositoryBuilder.setMustExist( true );
            repositoryBuilder.setGitDir(gitFile);
            repo = repositoryBuilder.build();
        } else {
            // Create a new repository
            repo = FileRepositoryBuilder.create(
                    new File(repoPath));
            repo.create(true);
        }
        System.out.println(repo.getDirectory());
        return repo;
    }
}
