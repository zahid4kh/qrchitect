# QRchitect

A desktop application for creating and managing QR codes.

## Installation

### Method 1: Direct Download (Easiest)

1. Download the `.deb` package from [GitHub Releases](https://github.com/zahid4kh/qrchitect/releases)
2. Install it using:
   ```bash
   sudo dpkg -i qrchitect_1.0.0_amd64.deb
   sudo apt install -f
   ```


### Method 2: Using the APT Repository

1. Add the repository key:

    ```bash
    wget -qO - https://raw.githubusercontent.com/zahid4kh/qrchitect/main/repo/public-key.asc | sudo apt-key add -
    ```


2. Add the repository:
    ```bash
    echo "deb [trusted=yes] https://raw.githubusercontent.com/zahid4kh/qrchitect/main/repo/dists/stable/main" | sudo tee /etc/apt/sources.list.d/qrchitect.list
    ```


3. Update package list:
    ```bash
    sudo apt update
    ```


4. Install QRchitect:
    ```bash
    sudo apt install qrchitect
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

