import com.util.Constants

def call(){
    switch(env.STG_NAME){
        case Constants.STAGE_COMPILE:
            stage(Constants.STAGE_COMPILE){
                if(env.BUILD_TOOL == Constants.MAVEN){
                    "${env.BATCH_COMMAND}" 'mvnw.cmd clean compile -e'
                } else {
                    "${env.BATCH_COMMAND}" 'gradle clean build'
                }
            }
            break
        case Constants.STAGE_UNITTEST:
            stage(Constants.STAGE_UNITTEST){
                if(env.BUILD_TOOL == Constants.MAVEN){
                    "${env.BATCH_COMMAND}" 'mvnw.cmd clean test -e'
                } else {
                    "${env.BATCH_COMMAND}" 'gradle test'
                }
            }
            break
        case Constants.STAGE_JAR:
            stage(Constants.STAGE_JAR){
                bat 'mvnw.cmd clean package -e'
            }
            break
        case Constants.STAGE_SONAR:
            stage(Constants.STAGE_SONAR){
                def repoName = util.getRepoName(env.URL_REPO)
                def projectName = "${repoName}-${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
                def scannerHome = tool 'sonar-scanner';

                withSonarQubeEnv('sonar') {
                    "${env.BATCH_COMMAND}" "${scannerHome}/bin/sonar-scanner -Dsonar.projectName=${projectName} -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build -Dsonar.login=75a0e9b0613f563c0e69a23174cf79eb5d4d74c7"
                }
            }
            break
        case Constants.STAGE_NEXUSUPLOAD:
            stage(Constants.STAGE_NEXUSUPLOAD){
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: 'build\\DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
            }
            break
        case Constants.STAGE_GITCREATERELEASE:
            if(util.isDevelopBranch(env.BRANCH_NAME)){
                stage(Constants.STAGE_GITCREATERELEASE){
                    def release = release_upgrade.call()
                    def releaseName = "release-v${release}"

                    println "Release Branch: ${releaseName}"

                    "${env.BATCH_COMMAND}" "git checkout -b ${releaseName}"
                    "${env.BATCH_COMMAND}" "git push origin ${releaseName}"
                }
            }
            break
        case Constants.STAGE_GITDIFF:
            stage(Constants.STAGE_GITDIFF){
                "${env.BATCH_COMMAND}" "git config --add remote.origin.fetch +refs/heads/main:refs/remotes/origin/main"
                "${env.BATCH_COMMAND}" "git fetch --no-tags"
                "${env.BATCH_COMMAND}" "git diff origin/main..origin/${env.BRANCH_NAME}"
            }
            break
        case Constants.STAGE_NEXUSDOWNLOAD:
            stage(Constants.STAGE_NEXUSDOWNLOAD){
                "${env.BATCH_COMMAND}" 'curl -X GET -u admin:P@ssw0rd2201 http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            }
            break
        case Constants.STAGE_RUN:
            stage(Constants.STAGE_RUN){
                "${env.BATCH_COMMAND}" 'start java -jar DevOpsUsach2020-0.0.1.jar'
            }
            break
        case Constants.STAGE_TEST:
            stage(Constants.STAGE_TEST){
                sleep 10
                "${env.BATCH_COMMAND}" 'curl http://localhost:8082/rest/mscovid/estadoMundial'
                "${env.BATCH_COMMAND}" 'curl http://localhost:8082/rest/mscovid/test?msg=testing'
            }
            break
        case Constants.STAGE_GITMERGEMASTER:
            stage(Constants.STAGE_GITMERGEMASTER){
                "${env.BATCH_COMMAND}" "git checkout main"
                "${env.BATCH_COMMAND}" "git fetch --all"
                "${env.BATCH_COMMAND}" "git merge origin/${env.BRANCH_NAME} --commit"
                "${env.BATCH_COMMAND}" "git push origin main"
            }
            break
        case Constants.STAGE_GITMERGEDEVELOP:
            stage(Constants.STAGE_GITMERGEDEVELOP){
                "${env.BATCH_COMMAND}" "git config --add remote.origin.fetch +refs/heads/develop:refs/remotes/origin/develop"
                "${env.BATCH_COMMAND}" "git fetch --all"
                "${env.BATCH_COMMAND}" "git checkout develop"
                "${env.BATCH_COMMAND}" "git merge origin/${env.BRANCH_NAME} --commit"
                "${env.BATCH_COMMAND}" "git push origin develop"
            }
            break
        case Constants.STAGE_GITTAGMASTER:
            stage(Constants.STAGE_GITTAGMASTER){
                def tag = util.setTag(env.BRANCH_NAME)

                println "Tag Main: ${tag}"

                "${env.BATCH_COMMAND}" "git checkout main"
                "${env.BATCH_COMMAND}" "git fetch --all"
                "${env.BATCH_COMMAND}" "git tag ${tag}"
                "${env.BATCH_COMMAND}" "git push origin ${tag}"
            }
            break
        default:
            break
    }
}

return this;