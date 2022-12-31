def checkIfBranchExist(String branch) {
  def output = sh (script: 'git ls-remote --heads origin ${branch}', returnStdout:true)
  def respuesta = (!output?.trim()) ? false : true
}

def isBranchUpdated(String ramaOrigen, String ramaDestino {
  sh 'git checkout ${ramaoOrigen}; git pull'
  sh 'git checkout ${ramaDestino}; git pull'

  def output = sh (script: 'git log origin/${ramaDestino}..origin/{ramaOrigen}', returnStdout: true)
  def respuesta = (!output?.trim()) ? true : false

  return respuesta
}

def deleteBranch {
  sh 'git pull; git push origin --delete ${branch}'
}

def createBranch {
  sh 'git reset --hard HEAD'
  sh 'git pull'
  sh 'git checkout ${ramaOrigen}'
  sh 'git checkout -b ${branch}'
  sh 'git push origin ${branch}'
}


git ls-remote --heads origin release-v1-0-0 => para saber si una rama existe
