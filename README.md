# QRchitect

A desktop application for creating and managing QR codes.

## Installation

## Direct Download

1. Download the desired `.deb` version from [GitHub Releases](https://github.com/zahid4kh/qrchitect/releases)
or by directly downloading the [latest version](https://github.com/zahid4kh/qrchitect/releases/download/v1.0.0/qrchitect_1.0.0_amd64.deb)

2. Install it using:
   ```bash
   sudo dpkg -i qrchitect_1.0.0_amd64.deb
   ```
   ```bash
   sudo apt install -f
   ```
   Then run with:
   ```bash
   qrchitect
   ```

## Building from Source

1. Clone the repository:
    ```bash
    git clone https://github.com/zahid4kh/qrchitect.git
    ```
2. Build using Gradle:
    ```bash
    ./gradlew packageReleaseUberJarForCurrentOS
    ```
