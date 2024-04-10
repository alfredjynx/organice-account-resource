pipeline {
    agent any
    stages {
        stage('Jenkins Account') {
            steps {
                echo 'Jenkins Account Resource'
            }
        }
        stage('Build') { 
            steps {
                sh 'mvn clean package'
            }
        }      
    }
}