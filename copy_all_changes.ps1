# Script to copy ALL uncommitted changes from testing_exam_webapp to testing_exam_webapp_orgclone
# This includes modified, deleted, and untracked files

$baseDir = "d:\Ayayqw\PROGRAMMING PROJECTS\School-stuff"
$sourceDir = Join-Path $baseDir "testing_exam_webapp\testing_exam_webapp"
$destDir = Join-Path $baseDir "testing_exam_webapp_orgclone\testing_exam_webapp"

Write-Host "Copying all uncommitted changes..." -ForegroundColor Cyan
Write-Host "Source: $sourceDir" -ForegroundColor Yellow
Write-Host "Destination: $destDir" -ForegroundColor Yellow
Write-Host ""

# Change to source directory to run git commands
Push-Location $sourceDir

try {
    # Get all modified files
    $modifiedFiles = git diff --name-only
    # Get all untracked files
    $untrackedFiles = git ls-files --others --exclude-standard
    # Get deleted files (they exist in git but are deleted in working directory)
    $deletedFiles = git diff --name-only --diff-filter=D
    
    Write-Host "Found changes:" -ForegroundColor Cyan
    Write-Host "  Modified files: $($modifiedFiles.Count)" -ForegroundColor Yellow
    Write-Host "  Untracked files: $($untrackedFiles.Count)" -ForegroundColor Yellow
    Write-Host "  Deleted files: $($deletedFiles.Count)" -ForegroundColor Yellow
    Write-Host ""
    
    $copied = 0
    $deleted = 0
    $skipped = 0
    
    # Copy modified files
    foreach ($file in $modifiedFiles) {
        if ($file) {
            $sourcePath = Join-Path $sourceDir $file
            $destPath = Join-Path $destDir $file
            
            if (Test-Path $sourcePath) {
                $destFolder = Split-Path $destPath -Parent
                if (-not (Test-Path $destFolder)) {
                    New-Item -ItemType Directory -Path $destFolder -Force | Out-Null
                }
                Copy-Item -Path $sourcePath -Destination $destPath -Force
                Write-Host "  [MODIFIED] $file" -ForegroundColor Green
                $copied++
            } else {
                Write-Host "  [SKIP] Not found: $file" -ForegroundColor Yellow
                $skipped++
            }
        }
    }
    
    # Copy untracked files
    foreach ($file in $untrackedFiles) {
        if ($file) {
            $sourcePath = Join-Path $sourceDir $file
            $destPath = Join-Path $destDir $file
            
            if (Test-Path $sourcePath) {
                $destFolder = Split-Path $destPath -Parent
                if (-not (Test-Path $destFolder)) {
                    New-Item -ItemType Directory -Path $destFolder -Force | Out-Null
                }
                
                # If it's a directory, copy recursively
                if (Test-Path $sourcePath -PathType Container) {
                    Copy-Item -Path $sourcePath -Destination $destPath -Recurse -Force
                    Write-Host "  [NEW DIR] $file" -ForegroundColor Cyan
                } else {
                    Copy-Item -Path $sourcePath -Destination $destPath -Force
                    Write-Host "  [NEW FILE] $file" -ForegroundColor Cyan
                }
                $copied++
            } else {
                Write-Host "  [SKIP] Not found: $file" -ForegroundColor Yellow
                $skipped++
            }
        }
    }
    
    # Handle deleted files - remove them from destination
    foreach ($file in $deletedFiles) {
        if ($file) {
            $destPath = Join-Path $destDir $file
            if (Test-Path $destPath) {
                Remove-Item -Path $destPath -Force
                Write-Host "  [DELETED] $file" -ForegroundColor Red
                $deleted++
            }
        }
    }
    
    Write-Host ""
    Write-Host "Summary:" -ForegroundColor Cyan
    Write-Host "  Copied: $copied files/directories" -ForegroundColor Green
    Write-Host "  Deleted: $deleted files" -ForegroundColor Red
    Write-Host "  Skipped: $skipped files" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Done! All uncommitted changes have been copied to $destDir" -ForegroundColor Cyan
    
} finally {
    Pop-Location
}

