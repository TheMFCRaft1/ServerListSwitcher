#!/bin/bash

# Script to run Minecraft client with correct Java version for each MC version
# Usage: ./run-client.sh [version]
# Examples: ./run-client.sh 1.18.2, ./run-client.sh 1.19.4, ./run-client.sh 1.21.11

VERSION=${1:-1.21.11}

echo "=== Server List Switcher Client Launcher ==="
echo "Target Minecraft version: $VERSION"

# Determine Java version requirements
case $VERSION in
    1.18.*)
        JAVA_VERSION="17"
        ;;
    1.19.*|1.20.*)
        JAVA_VERSION="17"
        ;;
    1.21.*)
        JAVA_VERSION="21"
        ;;
    *)
        echo "Unknown version: $VERSION"
        echo "Supported versions: 1.18.2, 1.19, 1.19.2, 1.19.4, 1.20, 1.20.1, 1.20.6, 1.21, 1.21.1, 1.21.11"
        exit 1
        ;;
esac

echo "Required Java version: $JAVA_VERSION"

# Check if correct Java version is available
if [ -d "/usr/lib/jvm/java-$JAVA_VERSION-openjdk-amd64" ]; then
    export JAVA_HOME="/usr/lib/jvm/java-$JAVA_VERSION-openjdk-amd64"
    echo "Using Java: $JAVA_HOME"
elif command -v java &> /dev/null; then
    CURRENT_JAVA=$(java -version 2>&1 | head -n1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$CURRENT_JAVA" = "$JAVA_VERSION" ]; then
        echo "Using current Java version: $CURRENT_JAVA"
    else
        echo "Warning: Java $JAVA_VERSION is required but Java $CURRENT_JAVA is currently active."
        echo "Please install Java $JAVA_VERSION or switch to it manually."
        echo "On Ubuntu/Debian: sudo apt install openjdk-$JAVA_VERSION-jdk"
        exit 1
    fi
else
    echo "Java is not installed. Please install Java $JAVA_VERSION"
    exit 1
fi

# Run the client
echo "Launching Minecraft $VERSION..."

# For now, use Java 21 for everything (some older versions might work)
export JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
./gradlew runClient -Pmc=$VERSION
