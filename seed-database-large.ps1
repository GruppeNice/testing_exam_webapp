# PowerShell script to seed the database with large dataset (500 of each)
# Usage: .\seed-database-large.ps1

$baseUrl = "http://localhost:8080"
$username = "admin"
$password = "admin"

Write-Host "Logging in as admin..." -ForegroundColor Yellow

# Login and get token
$loginBody = @{
    username = $username
    password = $password
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginBody

    $token = $loginResponse.token
    Write-Host "Login successful!" -ForegroundColor Green
    Write-Host "Seeding database with large dataset (500 of each)..." -ForegroundColor Yellow
    Write-Host "This may take a few minutes..." -ForegroundColor Yellow

    # Seed the database
    $seedResponse = Invoke-RestMethod -Uri "$baseUrl/admin/seed/large" `
        -Method POST `
        -Headers @{
            "Authorization" = "Bearer $token"
            "Content-Type" = "application/json"
        }

    Write-Host "`nDatabase seeded successfully!" -ForegroundColor Green
    Write-Host "Results:" -ForegroundColor Cyan
    $seedResponse | ConvertTo-Json -Depth 3

} catch {
    Write-Host "Error: $_" -ForegroundColor Red
    exit 1
}

