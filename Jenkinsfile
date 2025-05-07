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

        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                sh 'pkill -f "todolist-0.0.1-SNAPSHOT.jar" || true'  // Mata procesos anteriores (para reiniciar)
                sh 'java -jar target/todolist-0.0.1-SNAPSHOT.jar & echo $! > app.pid' // Ejecuta en background y guarda el PID
            }
        }
    }

    post {
        success {
            echo 'Pipeline ejecutado con éxito. La aplicación está corriendo en local.'
        }
        failure {
            echo 'Error en la ejecución del pipeline. '
        }
    }
}
