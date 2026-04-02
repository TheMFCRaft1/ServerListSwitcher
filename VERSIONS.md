# Supported Minecraft Versions

Your Server List Switcher mod is now configured to support the following Minecraft versions:

## ✅ Working Versions

### Minecraft 1.21.x
- **1.21.11** - Latest supported version
- **1.21.1** - Stable release
- **1.21** - Base version

### Minecraft 1.20.x  
- **1.20.6** - Latest 1.20 version
- **1.20.1** - Stable release
- **1.20** - Base version

### Minecraft 1.19.x
- **1.19.4** - Latest 1.19 version
- **1.19.2** - Stable release  
- **1.19** - Base version

### Minecraft 1.18.x
- **1.18.2** - Latest 1.18 version (recommended)

## 🚀 Quick Start Commands

### Using the Convenience Script (Recommended)
```bash
# Launch with default version (1.21.11)
./run-client.sh

# Launch specific versions
./run-client.sh 1.21.11
./run-client.sh 1.20.6
./run-client.sh 1.19.4
./run-client.sh 1.18.2
```

### Manual Gradle Commands
```bash
# Minecraft 1.21.x (uses Java 21)
./gradlew runClient -Pmc=1.21.11

# Minecraft 1.20.x (uses Java 17)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew runClient -Pmc=1.20.6

# Minecraft 1.19.x (uses Java 17)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew runClient -Pmc=1.19.4

# Minecraft 1.18.x (uses Java 17)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew runClient -Pmc=1.18.2
```

## 📋 Build Commands

```bash
# Build for all versions
./gradlew build

# Build specific version
./gradlew build -Pmc=1.21.11
```

## 🔧 Configuration Details

### Java Requirements
- **Java 21**: Required for Minecraft 1.21.x and Gradle build system
- **Java 17**: Required for Minecraft 1.18.x - 1.20.x runtime

### Fabric Loader Versions
- **Minecraft 1.21.11**: Fabric Loader 0.17.3
- **Other versions**: Fabric Loader 0.16.0

### Fabric API Versions
- **1.21.11**: 0.141.3+1.21.11
- **1.21.x**: 0.102.1+1.21.1
- **1.20.x**: 0.100.1+1.20.6
- **1.19.x**: 0.83.0+1.19.4
- **1.18.x**: 0.77.0+1.18.2

## 📁 Output Locations

Built JAR files are located in:
- `v1_18/build/libs/` - Minecraft 1.18.x
- `v1_19/build/libs/` - Minecraft 1.19.x
- `v1_20/build/libs/` - Minecraft 1.20.x
- `v1_21/build/libs/` - Minecraft 1.21.x

## ⚠️ Notes

- The mod successfully loads and runs on all supported versions
- Some access widener warnings may appear during startup but don't affect functionality
- For development, Java 21 is used for the build system regardless of target Minecraft version
- The `run-client.sh` script handles Java version switching automatically
