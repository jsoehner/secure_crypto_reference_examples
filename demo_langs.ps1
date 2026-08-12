# Colors for output
$GREEN = "`e[0;32m"
$BLUE = "`e[0;34m"
$NC = "`e[0m" # No Color

Write-Host "${BLUE}==========================================${NC}"
Write-Host "      Language Demonstration Script"
Write-Host "${BLUE}==========================================${NC}"

$choice = Read-Host "Which language(s) would you like to demonstrate? (python, java, both)"

switch ($choice.ToLower()) {
    "python" {
        Write-Host "`n${GREEN}[1] Demonstrating Python secure code examples...${NC}"
        if (Get-Command python3 -ErrorAction SilentlyContinue) {
            python3 python/run_all.py
        } elseif (Get-Command python -ErrorAction SilentlyContinue) {
            python python/run_all.py
        } else {
            Write-Host "Python is not installed."
        }
    }
    "java" {
        Write-Host "`n${GREEN}[1] Demonstrating Java secure code examples...${NC}"
        if (Get-Command java -ErrorAction SilentlyContinue) {
            if (Get-Command bash -ErrorAction SilentlyContinue) {
                bash java/run_all.sh
            } else {
                Write-Host "Bash is not installed to run the Java demo script."
            }
        } else {
            Write-Host "Java is not installed."
        }
    }
    "both" {
        Write-Host "`n${GREEN}[1] Demonstrating Python secure code examples...${NC}"
        if (Get-Command python3 -ErrorAction SilentlyContinue) {
            python3 python/run_all.py
        } elseif (Get-Command python -ErrorAction SilentlyContinue) {
            python python/run_all.py
        } else {
            Write-Host "Python is not installed."
        }

        Write-Host "`n${GREEN}[2] Demonstrating Java secure code examples...${NC}"
        if (Get-Command java -ErrorAction SilentlyContinue) {
            if (Get-Command bash -ErrorAction SilentlyContinue) {
                bash java/run_all.sh
            } else {
                Write-Host "Bash is not installed to run the Java demo script."
            }
        } else {
            Write-Host "Java is not installed."
        }
    }
    Default {
        Write-Host "Invalid choice. Please run the script again and choose python, java, or both."
    }
}

Write-Host "`n${BLUE}==========================================${NC}"
Write-Host "            Demonstration Complete"
Write-Host "${BLUE}==========================================${NC}"
