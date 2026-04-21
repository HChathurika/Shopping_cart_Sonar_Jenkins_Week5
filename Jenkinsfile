pipeline {
    agent any
    tools {
        maven 'maven3'
    }

    environment {
        PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21'
        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_TOKEN = 'squ_3427581372949391a5286d4988eca46ddd10e5e6'
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKERHUB_REPO = 'hathadura/shopping_cart_jenkins_sonarqube_week5'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/HChathurika/Shopping_cart_Sonar_Jenkins_Week5.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    bat """
                    ${tool 'SonarScanner'}\\bin\\sonar-scanner ^
                    -Dsonar.projectKey=shopping-cart ^
                    -Dsonar.projectName=shopping-cart ^
                    -Dsonar.sources=src/main/java ^
                    -Dsonar.tests=src/test/java ^
                    -Dsonar.host.url=http://localhost:9000 ^
                    -Dsonar.token=${env.SONAR_TOKEN} ^
                    -Dsonar.java.binaries=target/classes
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
}