# Script to copy test changes from testing_exam_webapp to testing_exam_webapp_orgclone
# Run this from the parent directory containing both folders

$sourceDir = "testing_exam_webapp\testing_exam_webapp"
$destDir = "testing_exam_webapp_orgclone\testing_exam_webapp"

# Test files directory
$testDir = "src\test\java\com\testing_exam_webapp"

# Files we modified/created in this session
$testFiles = @(
    # Controller tests (new)
    "$testDir\controller\AuthControllerTest.java",
    "$testDir\controller\SurgeryControllerTest.java",
    "$testDir\controller\DiagnosisControllerTest.java",
    "$testDir\controller\HospitalControllerTest.java",
    "$testDir\controller\MedicationControllerTest.java",
    "$testDir\controller\PrescriptionControllerTest.java",
    "$testDir\controller\NurseControllerTest.java",
    "$testDir\controller\TimeControllerTest.java",
    "$testDir\controller\WeatherControllerTest.java",
    "$testDir\controller\PatientControllerTest.java",
    
    # Exception handler test (new)
    "$testDir\exception\GlobalExceptionHandlerTest.java",
    
    # Service tests (modified - branch coverage improvements)
    "$testDir\service\SurgeryServiceTest.java",
    "$testDir\service\PatientServiceTest.java",
    "$testDir\service\DoctorServiceTest.java",
    "$testDir\service\AppointmentServiceTest.java",
    "$testDir\service\HospitalServiceTest.java",
    "$testDir\service\MedicationServiceTest.java",
    "$testDir\service\PrescriptionServiceTest.java",
    "$testDir\service\DiagnosisServiceTest.java"
)

Write-Host "Copying test files from $sourceDir to $destDir..." -ForegroundColor Cyan

$copied = 0
$failed = 0

foreach ($file in $testFiles) {
    $sourcePath = Join-Path $sourceDir $file
    $destPath = Join-Path $destDir $file
    
    if (Test-Path $sourcePath) {
        # Create destination directory if it doesn't exist
        $destFolder = Split-Path $destPath -Parent
        if (-not (Test-Path $destFolder)) {
            New-Item -ItemType Directory -Path $destFolder -Force | Out-Null
        }
        
        # Copy the file
        Copy-Item -Path $sourcePath -Destination $destPath -Force
        Write-Host "  [OK] Copied: $file" -ForegroundColor Green
        $copied++
    } else {
        Write-Host "  [SKIP] Not found: $file" -ForegroundColor Yellow
        $failed++
    }
}

Write-Host "`nSummary:" -ForegroundColor Cyan
Write-Host "  Copied: $copied files" -ForegroundColor Green
Write-Host "  Skipped: $failed files" -ForegroundColor Yellow
Write-Host "`nDone! Test files have been copied to $destDir" -ForegroundColor Cyan

