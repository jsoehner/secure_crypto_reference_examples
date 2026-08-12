#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}==========================================${NC}"
echo -e "      Language Demonstration Script"
echo -e "${BLUE}==========================================${NC}"

# --- Python Demonstration ---
echo -e "\n${GREEN}[1] Demonstrating Python...${NC}"
if command -v python3 &> /dev/null; then
    python3 -c "
import sys
print(f'Hello from Python {sys.version.split()[0]}!')
print('Simple math: 2^10 =', 2**10)
"
else
    echo "Python 3 is not installed."
fi

# --- Java Demonstration ---
echo -e "\n${GREEN}[2] Demonstrating Java...${NC}"
if command -v java &> /dev/null; then
    # Create a temporary Java file
    JAVA_FILE="HelloWorld.java"
    cat <<EOF > "$JAVA_FILE"
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello from Java!");
        System.out.println("Simple math: 2^10 = " + Math.pow(2, 10));
    }
}
EOF

    # Compile and Run
    if javac "$JAVA_FILE" &> /dev/null; then
        java HelloWorld
        rm "$JAVA_FILE"
        rm HelloWorld.class
    else
        echo "Failed to compile Java code."
        rm -f "$JAVA_FILE" HelloWorld.class
    fi
else
    echo "Java is not installed."
fi

echo -e "\n${BLUE}==========================================${NC}"
echo -e "            Demonstration Complete"
echo -e "${BLUE}==========================================${NC}"
