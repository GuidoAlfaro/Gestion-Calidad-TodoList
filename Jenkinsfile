pipeline {
    agent any

    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }

    triggers {
            githubPush()
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

        stage('Docker Build & Run') {
            steps {
                echo 'Construyendo imagen Docker...'
                sh 'docker build -t todolist-backend .'

                echo 'Parando contenedor anterior si existe...'
                sh 'docker rm -f todolist-backend || true'

                echo 'Iniciando nuevo contenedor...'
                sh 'docker run -d --name todolist-backend -p 8081:8081 todolist-backend'
            }
        }
    }

    post {
        success {
            echo 'Deploy exitoso con Docker. El contenedor está corriendo en el puerto 8081. '
        }
        failure {
            echo 'Error en el pipeline.'
        }
    }
}
