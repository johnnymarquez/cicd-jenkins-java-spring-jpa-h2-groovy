package com.util

class Constants {
    public static final String IC = 'IC'
    public static final String RELEASE = 'RELEASE'
    public static final String MAVEN = 'MAVEN'
    public static final String GRADLE = 'GRADLE'
    public static final String SPLIT_SYMBOL = ';'

    public static final String STAGE_COMPILE = 'compile'
    public static final String STAGE_UNITTEST = 'unitTest'
    public static final String STAGE_JAR = 'jar'
    public static final String STAGE_SONAR = 'sonar'
    public static final String STAGE_NEXUSUPLOAD = 'nexusUpload'
    public static final String STAGE_GITCREATERELEASE = 'gitCreateRelease'
    public static final String STAGE_GITDIFF = 'gitDiff'
    public static final String STAGE_NEXUSDOWNLOAD = 'nexusDownload'
    public static final String STAGE_RUN = 'run'
    public static final String STAGE_TEST = 'test'
    public static final String STAGE_GITMERGEMASTER = 'gitMergeMaster'
    public static final String STAGE_GITMERGEDEVELOP = 'gitMergeDevelop'
    public static final String STAGE_GITTAGMASTER = 'gitTagMaster'

    public static final IC_STAGES = [STAGE_COMPILE, STAGE_UNITTEST, STAGE_JAR, STAGE_SONAR, STAGE_NEXUSUPLOAD, STAGE_GITCREATERELEASE]
    public static final RELEASE_STAGES = [STAGE_GITDIFF, STAGE_NEXUSDOWNLOAD, STAGE_RUN, STAGE_TEST, STAGE_GITMERGEMASTER, STAGE_GITMERGEDEVELOP, STAGE_GITTAGMASTER]
}