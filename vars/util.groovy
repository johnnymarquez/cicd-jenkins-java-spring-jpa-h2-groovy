import com.util.Constants

def validStages(pipeline_type) {
    def valid_stages

    switch(pipeline_type) {
        case Constants.IC:
            valid_stages = Constants.IC_STAGES
            break
        case Constants.RELEASE:
            valid_stages = Constants.RELEASE_STAGES
            break
    }

    return valid_stages
}

def validateStages(stages){

    def valid_stages = this.validStages(env.PIPELINE_TYPE)

    if(stages.trim() == ''){
        println "Stages a ejecutar [TODOS]"
    }else{
        println "Stages a ejecutar [${stages}]"

        def stage_list = stages.split(Constants.SPLIT_SYMBOL);

        for(String value in stage_list){
            if (!valid_stages.contains(value.trim())){
                env.STG_NAME = "${value} (no valido)"
                error "Stage no valido: ${value}"
            }
        }
    }
}

def baseOS(){
    def os = ''

    if(isUnix()){
        os = 'Unix/Linux/MacOS'    
        env.BATCH_COMMAND = 'sh'
    } else {
        os = 'Windows'
        env.BATCH_COMMAND = 'bat'
    }

    println "Jenkins OS [${os}]"
}

def buildTool(){
    def tool = ''

    def file = new File("build.gradle")
    
    if (file.exists()){
        tool = Constants.GRADLE;
    }
    else {
        file = new File("pom.xml")
        tool = Constants.MAVEN;
    }

    //println "Build Tool [${tool}]"

    figlet "${tool}"

    return tool
}

def pipelineType(branch_name){
    def pipeline_type = ''

    if(branch_name ==~ /develop/ || branch_name ==~ /feature-.*/){
        pipeline_type = Constants.IC
        figlet "Integracion Continua"
    } else if(branch_name ==~ /^release-v\d{1,}-\d{1,}-\d{1,}$/){
        pipeline_type = Constants.RELEASE
        figlet "Despliegue Continuo"
    }

    //println "Pipeline Type [${pipeline_type}]"

    return pipeline_type
}

def isDevelopBranch(branch_name){
    if(branch_name ==~ /develop/)
        return true
    else{
        return false
    }
}

def getRepoName(url){
    def repoNameGit = url.split("/")[-1]
    def repoName = repoNameGit.split("\\.")[0]

    return repoName
}

def setTag(branch_name){ //release-v3-0-3
    def tagName = branch_name.split("v")[1]

    return "v${tagName}"
}