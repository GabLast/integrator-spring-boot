pipeline {
    agent any
//     agent {
//         docker {
//             image 'bitnami/kubectl:latest' //installing kubectl through docker image
//         }
//     }

    tools {
        // 'maven-3.8.6' should correspond to a configured Maven installation name in Jenkins
        maven 'maven-3.9.11' //needs to be added in Tools in Manage Jenkins
        // jdk 'jdk21' // Optional: if you also need a specific JDK
    }

    environment {
        DOCKER_IMAGE = "integrator-spring-boot"
    }

    stages {

        stage('Repo checkout') {
            steps {
                script {
                    checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/GabLast/integrator-spring-boot']])
                }
            }
        }

        stage('Read Maven Version') {
            steps {
                script {
                    // Read the pom.xml and store its contents in a variable
                    def pom = readMavenPom file: 'pom.xml'

                    // Access the version property
                    def projectVersion = pom.version

                    // Store the version as an environment variable for later use in the pipeline
                    env.APP_VERSION = projectVersion

                    //Print the version (optional)
                    echo "Maven Project Version: ${env.APP_VERSION}"
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    bat "mvn test"
                }
            }
        }

        //IMPORTANT:
        //For windows: For scripts, use bat. For Linux use sh
        //For windows: use %VARIABLE_NAME%. For Linux use echo $VARIABLE_NAME
        //Powershell: Set: $var = "hola"  echo: $var

        stage('Build Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKER_CREDENTIALS', passwordVariable: 'password', usernameVariable: 'username')]) {
                        bat """
                        echo "env.APP_VERSION 2: ${env.APP_VERSION}"
                        docker build -t $username/${DOCKER_IMAGE}:${env.APP_VERSION} .
                        """
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // withCredentials([usernamePassword(credentialsId: 'testUsername', passwordVariable: 'var2', usernameVariable: 'var1')]) {
                    //     bat 'echo %var1% %var2%'
                    // }
                    withCredentials([string(credentialsId: 'DOCKER-TOKEN', variable: 'token'), usernamePassword(credentialsId: 'DOCKER_CREDENTIALS', passwordVariable: 'password', usernameVariable: 'username')]) {
                        bat 'echo %token% | docker login -u %username% --password-stdin'
                        bat """
                        docker tag %username%/${DOCKER_IMAGE}:${env.APP_VERSION} %username%/${DOCKER_IMAGE}:latest
                        """
                        bat """
                        docker push %username%/${DOCKER_IMAGE}:${env.APP_VERSION}
                        """
                    }
                }
            }
        }

        //this can be used on linux
//         stage('Install Kubectl') {
//             steps {
//                 script {
//                     sh """
//                     curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
//                     chmod +x kubectl
//                     mv kubectl /usr/local/bin/kubectl
//                     """
//                 }
//             }
//         }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    kubeconfig(caCertificate: 'LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tTUlJREJqQ0NBZTZnQXdJQkFnSUJBVEFOQmdrcWhraUc5dzBCQVFzRkFEQVZNUk13RVFZRFZRUURFd3B0YVc1cGEzVmlaVU5CTUI0WERUSTFNRGN5T0RJd01EUXhNRm9YRFRNMU1EY3lOekl3TURReE1Gb3dGVEVUTUJFR0ExVUVBeE1LYldsdWFXdDFZbVZEUVRDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRGdnRVBBRENDQVFvQ2dnRUJBTkdJY3l1bkljUnRmQ2xTMEhVbVFpVldSUVBKQnNZTmdkOTRtaWFTMmJKTk5zTUp4cENPckZydmtiaCtDV0F3NjRSYlpJZE5HNVFJMDZPOFQxUCtDUUJoV3pPY1pMT3FTcytHd2laNU5nZEJDRS8zczgvSm91QUhzang1Uk9GcHorZGNKMVFQUFUwenlJUmI3L3pMV0xHVEpSd1JNbzcxU1ZXUUJNZnZUWkZ1V3hyYWpDUXpRWTJqUThXak5PMVNBeENDbCtXbEl4eTJGSW9HUjdpdnp3QzRDcmdBc09xdGNxQzdnVThGbDMwcVJIMkRWTklScFM5VnV4NHpyY21sUWpCRDF3RHVpWUNoN1NEdHBVcTl5UzB6YTZqMDFaSHIxNkJYU0hBNE1JNVBtSWNoUDhCVW1PZXNiV3FNc0JvbmRLTzZQejYvc3laOTRjTTJUWEFVQnFjQ0F3RUFBYU5oTUY4d0RnWURWUjBQQVFIL0JBUURBZ0trTUIwR0ExVWRKUVFXTUJRR0NDc0dBUVVGQndNQ0JnZ3JCZ0VGQlFjREFUQVBCZ05WSFJNQkFmOEVCVEFEQVFIL01CMEdBMVVkRGdRV0JCUUxnUDdPdTZJSEdXZGpLeDdKWFNySWg3T280ekFOQmdrcWhraUc5dzBCQVFzRkFBT0NBUUVBSy9BNEtRa2M3KzN4SzNiVHV5K1lWR1dkTFEvRDNVM3FGUjhYUzc0d0JxSHVYWmZLNkk4bSs4RTN4aTJ1aUhsd3NhYkVwYVpoRjd2Nk13cDRaYS9ERTVqenFKTHZWTm02RElLejN3OCtRMS9NQU45OHRpWUltVHpqZks4UitBSkVZK3NZTm9rYnR1UFVIeHBsWHFWSkhSYUhCbWxsVEFCSHRrK3FjWFBKOXFHLzVJS01HUnRLVk8xMjNtZEEvb21sNnBXeTd1aEZvbEtHMHNud1IzU04rZTlMTWRhMjg0L2NxTG1BUlg0UG8yckt1VWMrY3p6V3ZTOXA3L1hROFJhZmxPZnhaZzRaYVhVNE9ZYzdqWE9qeXF2VFFYQm9TVFN1bzRxZmFoMFZkMldDeEFScHgvaFA2d3MrNThRWVpmNDNwTWNCTGRRR2ZjM1BWYkdMZnV5eDRnPT0tLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0t', credentialsId: 'kubeconfig', serverUrl: '') {
                        bat """
                        kubectl apply -f k8s/deployment.yaml
                        """
                    }
                }
            }
        }
    }

    post {

        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Deployment failed.'
        }
    }
}
