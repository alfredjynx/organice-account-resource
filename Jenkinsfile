pipeline {
    agent any
    stages {
        stage('Jenkins Account') {
            steps {
                echo 'Account Service'
            }
        }
        stage('Build Interface') { 
            steps {
                build job: 'organice-account', wait: true
            }
        }     
        stage('Build') { 
            steps {
                sh 'mvn clean package'
            }
        }      
        stage('Build Image') { 
            steps {
                script {
                    account = docker.build("alfredjynx/account:${env.BUILD_ID}", "-f Dockerfile .")
                }
            }
        } 
        stage('Push Image') { 
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub'){
                        account.push('${env.BUILD_ID}')
                        account.push('latest')
                    }
                }
            }
        }      
    }
}