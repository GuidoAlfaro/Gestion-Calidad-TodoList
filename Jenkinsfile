pipeline {
    agent any

    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/GuidoAlfaro/Gestion-Calidad-TodoList'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean install' // o 'mvn clean install' si no usas wrapper
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizado'
        }
    }
}
