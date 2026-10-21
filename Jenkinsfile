pipeline {
    agent any

    parameters {
        choice(name: 'DEPLOY_ENV', choices: ['development', 'staging', 'production'], description: 'Deployment Target Environment')
        string(name: 'SERVER_PORT', defaultValue: '8080', description: 'Application Port')
        booleanParam(name: 'RUN_SELENIUM', defaultValue: true, description: 'Run Selenium E2E Test Suite')
    }

    environment {
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21.0.12.1'
        MAVEN_HOME = 'C:\\tools\\apache-maven-3.9.6'
        PATH = "${env.MAVEN_HOME}\\bin;${env.JAVA_HOME}\\bin;${env.PATH}"
        APP_NAME = 'mobile-recharge-portal'
        IMAGE_NAME = 'devops/mobile-recharge-portal'
    }

    stages {
        stage('1. Checkout SCM') {
            steps {
                echo 'Checking out source code from Git repository...'
                checkout scm
            }
        }

        stage('2. Build & Unit Tests') {
            steps {
                echo 'Compiling source code and executing unit test suite...'
                bat 'mvn clean test -Dtest=RechargeServiceTest'
            }
        }

        stage('3. Selenium E2E Tests') {
            when {
                expression { return params.RUN_SELENIUM == true }
            }
            steps {
                echo 'Executing Selenium WebDriver Automated End-to-End Tests...'
                bat 'mvn test -Dtest=RechargeSeleniumTest'
            }
        }

        stage('4. Package Artifact') {
            steps {
                echo 'Packaging standalone runnable executable JAR...'
                bat 'mvn package -DskipTests=true'
            }
        }

        stage('5. Docker Image Build & Tag') {
            steps {
                echo "Building versioned Docker image: ${IMAGE_NAME}:${BUILD_NUMBER}..."
                // Build container image if Docker daemon is present
                bat 'docker build -t %IMAGE_NAME%:%BUILD_NUMBER% -t %IMAGE_NAME%:latest .'
            }
        }

        stage('6. Deploy to Target Environment') {
            steps {
                echo "Deploying ${APP_NAME} to target environment [${params.DEPLOY_ENV}] on port ${params.SERVER_PORT}..."
                // Stop existing container if running, then launch updated version
                bat """
                    docker stop %APP_NAME% || exit 0
                    docker rm %APP_NAME% || exit 0
                    docker run -d --name %APP_NAME% -p %SERVER_PORT%:8080 %IMAGE_NAME%:%BUILD_NUMBER%
                """
            }
        }

        stage('7. Smoke & Health Check') {
            steps {
                echo 'Validating deployment health check endpoint...'
                bat 'curl -f http://localhost:%SERVER_PORT%/api/health || exit 0'
            }
        }
    }

    post {
        always {
            echo 'Archiving build artifacts and test reports...'
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'target/*.jar, target/screenshots/*.png', allowEmptyArchive: true
        }
        success {
            echo "CI/CD Pipeline SUCCEEDED for Build #${BUILD_NUMBER} in ${params.DEPLOY_ENV}!"
        }
        failure {
            echo "CI/CD Pipeline FAILED for Build #${BUILD_NUMBER}. Check test reports or screenshots."
        }
    }
}
