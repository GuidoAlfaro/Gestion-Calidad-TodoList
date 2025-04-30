pipeline {
    agent any

    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'https://github.com/GuidoAlfaro/Gestion-Calidad-TodoList.git',
                credentialsId: 'github-token'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizado'
        }
    }
}
