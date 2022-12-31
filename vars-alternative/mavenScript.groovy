//Stages Maven mavenScript.groovy

	def call(String selectStage = '') {

		switch (selectStage) {

			case 'compile':
			stage('Compile') {
				println 'Compile Maven';
				env.stage = "${env.STAGE_NAME}";
				sh 'mvn clean compile -e';
			}
			break;

			case 'unit':
			stage('Unit') {
				env.stage = "${env.STAGE_NAME}";
				sh 'mvn clean test -e';
			}
			break;

			case 'jar':
			stage('Jar') {
				env.stage = "${env.STAGE_NAME}";
				sh 'mvn clean package -e';
			}
			break;

			case 'sonar':
			stage('Sonar') {
				env.stage = "${env.STAGE_NAME}";
				script {
				withSonarQubeEnv('sonar') {
					sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar';
		  			}
				}
			}
			break;

			case 'runjar':
			stage('RunJar') {
						withMaven {
								sh 'nohup bash mvnw spring-boot:run &'
					}
				}
			break;

			case 'rest':
			stage('Rest') {
					sleep 20
					sh 'curl http://localhost:8082/rest/mscovid/test?msg=testing'
				}
			break;

			case 'nexusci':
		  stage('NexusCI') {
		 		env.stage = "${env.STAGE_NAME}";
				nexusPublisher nexusInstanceId: 'nexus',
				nexusRepositoryId: 'test-nexus',
				packages:
					[[$class: 'MavenPackage',
					mavenAssetList:
						[[classifier: '',
						extension: '',
						filePath: 'build/DevOpsUsach2020-0.0.1.jar']],
					mavenCoordinate:
						[artifactId: 'DevOpsUsach2020',
						groupId: 'com.devopsusach2020',
						packaging: 'jar',
						version: '0.0.1']]];
					}
			break;

			case 'gitcreaterelease':
			stage('gitCreateRelease') {
				env.stage = "${env.STAGE_NAME}";
				sh ('git checkout release');
			}
			break

		}
	}
	return this;
