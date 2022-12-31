// Stages Gradle gradleScript.groovy

	def call(String selectStage = '') {

		switch (selectStage) {

			case 'buildandtest':
			stage('BuildAndTest') {
				env.stage = "${env.STAGE_NAME}";
				sh './gradlew clean build';
			}
			break;

			case 'sonar':
			stage('Sonar') {
				env.stage = "${env.STAGE_NAME}";
		   	def scannerHome = tool 'sonar';
		   	withSonarQubeEnv('sonar') {
					sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build";
				}
			}
			break;

			case 'runjar':
			stage('RunJar') {
				env.stage = "${env.STAGE_NAME}";
				sh 'nohup bash gradlew bootRun &';
				sleep 20
			}
			break;

			case 'rest':
			stage('Rest') {
				env.stage = "${env.STAGE_NAME}";
				sh 'curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing';
			}
			break;

			case 'nexusci':
			stage('NexusCI') {
				env.stage = "${env.STAGE_NAME}"
				nexusPublisher nexusInstanceId: 'nexus',
				nexusRepositoryId: 'test-nexus',
				packages:
					[[$class: 'MavenPackage',
					mavenAssetList:
						[[classifier: '',
						extension: '',
						filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']],
					mavenCoordinate:
						[artifactId: 'DevOpsUsach2020',
						groupId: 'com.devopsusach2020',
						packaging: 'jar',
						version: '0.0.1']]]
			}
			break

			case 'gitcreaterelease':
			stage('gitCreateRelease') {
				env.stage = "${env.STAGE_NAME}";
					sh ('git checkout release');
			}
			break

		}
	}
	return this;
