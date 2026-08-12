#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}==========================================${NC}"
echo -e "      Language Demonstration Script"
echo -e "${BLUE}==========================================${NC}"

echo -e "Which language(s) would you like to demonstrate? (python, java, both)"
read -p "Choice: " choice

case "$choice" in
    python)
        echo -e "\n${GREEN}[1] Demonstrating Python secure code examples...${NC}"
        if command -v python3 &> /dev/null; then
            python3 python/run_all.py
        else
            echo "Python 3 is not installed."
        fi
        ;;
    java)
        echo -e "\n${GREEN}[1] Demonstrating Java secure code examples...${NC}"
        if command -v java &> /dev/null; then
            chmod +x java/run_all.sh
            ./java/run_all.sh
        else
            echo "Java is not installed."
        fi
        ;;
    both)
        echo -e "\n${GREEN}[1] Demonstrating Python secure code examples...${NC}"
        if command -v python3 &> /dev/null; then
            python3 python/run_all.py
        else
            echo "Python 3 is not installed."
        fi

        echo -e "\n${GREEN}[2] Demonstrating Java secure code examples...${NC}"
        if command -v java &> /dev/null; then
            chmod +x java/run_all.sh
            ./java/run_all.sh
        else
            echo "Java is not installed."
        fi
        ;;
    *)
        echo "Invalid choice. Please run the script again and choose python, java, or both."
        ;;
esac

echo -e "\n${BLUE}==========================================${NC}"
echo -e "            Demonstration Complete"
echo -e "${BLUE}==========================================${NC}"
