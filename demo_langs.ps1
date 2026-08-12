# Colors for output
$GREEN = "`e[0;32m"
$BLUE = "`e[0;34m"
$NC = "`e[0m" # No Color

Write-Host "${BLUE}==========================================${NC}"
Write-Host "      Language Demonstration Script"
Write-Host "${BLUE}==========================================${NC}"

# --- Python Demonstration ---
Write-Host "`n${GREEN}[1] Demonstrating Python...${NC}"
if (Get-Command python3 -ErrorAction SilentlyContinue) {
    python3 -c "
import sys
print(f'Hello from Python {sys.version.split()[0]}!')
print('Simple math: 2^10 =', 2**10)
"
} else {
    Write-Host "Python 3 is not installed."
}

# --- Java Demonstration ---
Write-Host "`n${GREEN}[2] Demonstrating Java...${NC}"
if (Get-Command java -ErrorAction SilentlyContinue) {
    # Create a temporary Java file
    $JAVA_FILE = "HelloWorld.java"
    $JAVA_CONTENT = @"
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello from Java!");
        System.out.println("Simple math: 2^10 = " + Math.pow(2, 10));
    }
}
"@
    $JAVA_CONTENT | Out-File -FilePath $JAVA_FILE -Encoding utf8

    # Compile and Run
    javac $JAVA_FILE
    if ($LASTEXITCODE -eq 0) {
        java HelloWorld
        Remove-Item $JAVA_FILE
        Remove-Item HelloWorld.class
    } else {
        Write-Host "Failed to compile Java code."
        if (Test-Path $JAVA_FILE) { Remove-Item $JAVA_FILE }
        if (Test-Path HelloWorld.class) { Remove-Item HelloWorld.class }
    }
} else {
    Write-Host "Java is not installed."
}

Write-Host "`n${BLUE}==========================================${NC}"
Write-Host "            Demonstration Complete"
Write-Host "${BLUE}==========================================${NC}"
