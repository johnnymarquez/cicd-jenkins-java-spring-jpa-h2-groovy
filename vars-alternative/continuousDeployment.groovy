//Script de Despliegue Continuo continuousDeployment.groovy

    def call() {

      stage('downloadNexus') {
        println 'Stage Nexus Download';
        sh 'ls && curl -X GET -u admin:admin http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O';
      }

      stage('runDownloadedJar') {
        println 'Stage Run Downloaded Jar';
        sh 'nohup bash java -jar ./DevOpsUsach2020-0.0.1.jar &';
      }

      stage('rest') {
    //    sleep 20;
        sh ('curl http://localhost:8083/rest/mscovid/test?msg=testing');
      }

      stage('nexusCD') {
        println 'Subir a Nexus versión 1.0.0';
        nexusPublisher nexusInstanceId: 'nexus',
        nexusRepositoryId: 'test-nexus',
        packages:
          [[$class: 'MavenPackage',
          mavenAssetList:
            [[classifier: '',
            extension: '',
            filePath: 'DevOpsUsach2020-0.0.1.jar']],
          mavenCoordinate:
            [artifactId: 'DevOpsUsach2020',
            groupId: 'com.devopsusach2020',
            packaging: 'jar',
            version: '1.0.0']]]
      }


    }
    return this;
