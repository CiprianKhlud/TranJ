package com.khlud.ciprian.tranj.bootstrap;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.function.Predicate;

import static java.text.MessageFormat.format;

/**
 *
 * @author Ciprian
 */
public class BootStrapMain {
    public static void main(String[] args)
            throws IOException {

        //downloadGithubBranch("dmlloyd", "openjdk", "jdk9/jdk9", "repo.zip");
        Predicate<String> isJava = (f)->f.endsWith(".java")&&!f.endsWith("module-info.java") ;
        UnzipUtility.unzip("bootstrap/repo.zip", "bootstrap/repo", isJava);
    }

    static final String OUTPUT_FOLDER = "bootstrap/extracted";

    public static void downloadGithubBranch(String userGitHub, String projectGitHub, String branch, String destinationName) throws IOException {
        String urlFormatted = format("https://github.com/{0}/{1}/archive/{2}.zip", userGitHub, projectGitHub, branch);
        URL url = new URL(urlFormatted);
        File bootstrap = new File("bootstrap");
        bootstrap.mkdir();
        File file = new File(bootstrap.getAbsoluteFile() + "/"+ destinationName);
        FileUtils.copyURLToFile(url, file);
    }

}
