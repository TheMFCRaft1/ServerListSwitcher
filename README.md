# Server List Switcher

A multi-version Fabric mod for Minecraft that allows switching between server lists.

## Supported Minecraft Versions

This mod supports the following Minecraft versions:

- **1.21.x**: 1.21, 1.21.1, 1.21.11
- **1.20.x**: 1.20, 1.20.1, 1.20.6  
- **1.19.x**: 1.19, 1.19.2, 1.19.4
- **1.18.x**: 1.18.2

## Requirements

### Java Version Requirements

Different Minecraft versions require different Java versions:

- **Minecraft 1.21.x**: Requires Java 21
- **Minecraft 1.18.x - 1.20.x**: Requires Java 17

### Installation

On Ubuntu/Debian systems, install the required Java versions:

```bash
# For Java 21 (Minecraft 1.21.x)
sudo apt install openjdk-21-jdk

# For Java 17 (Minecraft 1.18.x - 1.20.x)  
sudo apt install openjdk-17-jdk
```

## Building and Running

### Method 1: Using the Convenience Script (Recommended)

Use the provided `run-client.sh` script which automatically handles Java version switching:

```bash
# Run with default version (1.21.11)
./run-client.sh

# Run with specific version
./run-client.sh 1.21.11
./run-client.sh 1.20.6
./run-client.sh 1.19.4
./run-client.sh 1.18.2
```

### Method 2: Manual Gradle Commands

```bash
# For Minecraft 1.21.x (requires Java 21)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
./gradlew runClient -Pmc=1.21.11
./gradlew runClient -Pmc=1.21.1
./gradlew runClient -Pmc=1.21

# For Minecraft 1.20.x (requires Java 17)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew runClient -Pmc=1.20.6
./gradlew runClient -Pmc=1.20.1
./gradlew runClient -Pmc=1.20

# For Minecraft 1.19.x (requires Java 17)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew runClient -Pmc=1.19.4
./gradlew runClient -Pmc=1.19.2
./gradlew runClient -Pmc=1.19

# For Minecraft 1.18.x (requires Java 17)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew runClient -Pmc=1.18.2
```

## Building the Mod

To build the mod for all supported versions:

```bash
# Build for all versions
./gradlew build

# Build for specific version
./gradlew build -Pmc=1.21.11
```

Built JAR files will be located in:
- `v1_18/build/libs/` - Minecraft 1.18.x versions
- `v1_19/build/libs/` - Minecraft 1.19.x versions  
- `v1_20/build/libs/` - Minecraft 1.20.x versions
- `v1_21/build/libs/` - Minecraft 1.21.x versions

## Project Structure

```
ServerListSwitcher/
├── common/           # Shared code across all versions
├── v1_18/           # Minecraft 1.18.x specific code
├── v1_19/           # Minecraft 1.19.x specific code
├── v1_20/           # Minecraft 1.20.x specific code
├── v1_21/           # Minecraft 1.21.x specific code
├── build.gradle     # Multi-version build configuration
├── run-client.sh    # Convenience script for running the client
└── settings.gradle  # Gradle settings
```

## Troubleshooting

### Java Version Mismatch

If you encounter errors like:
```
Mod 'Server List Switcher' requires version 17 of 'OpenJDK 64-Bit Server VM' (java), but only the wrong version is present: 21!
```

This means you're using the wrong Java version. Use the `run-client.sh` script or manually set the correct JAVA_HOME.

### Access Widener Issues

If you encounter access widener errors, the build configuration includes fixes for development environments.

### Version Compatibility

Only the versions listed in the "Supported Minecraft Versions" section are officially supported. Other versions may work but are not tested.

## Development

When adding new features:

1. Place version-independent code in `common/`
2. Place version-specific code in the appropriate `vX_XX/` directory
3. Update the `mcToYarn` mapping in `build.gradle` if adding new versions
4. Test with all supported Minecraft versions before releasing
