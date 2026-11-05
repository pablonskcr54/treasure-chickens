# ===========================================================
# Upload Treasure Chickens version to Modrinth via API
#
# The project must exist! If not, create it at
# https://modrinth.com/upload (slug: treasure-chickens)
# then run this script.
#
# Requirements:
#   1. Modrinth token (https://modrinth.com/settings/account -> Development)
#      with create_version permission.
#   2. Run:
#      $env:MODRINTH_TOKEN="your_token"
#      powershell -ExecutionPolicy Bypass -File publish.ps1
# ===========================================================

$ErrorActionPreference = "Stop"

$token = $env:MODRINTH_TOKEN
if ([string]::IsNullOrEmpty($token)) {
    Write-Error "No token. Set MODRINTH_TOKEN env var."
    exit 1
}

$scriptRoot    = $PSScriptRoot
$projectSlug   = "treasure-chickens"
$loaders       = @("neoforge")
$gameVersions  = @("1.21.1")
$versionNumber = "1.0.0"
$versionTitle  = "Treasure Chickens 1.0.0"
$jarPath       = Join-Path $scriptRoot "build\libs\treasure_chickens-1.0.0.jar"
$changelogPath = Join-Path $scriptRoot "CHANGELOG.md"
$metaPath      = Join-Path $env:TEMP "tc_version_meta.json"

if (-not (Test-Path $jarPath)) {
    Write-Error "JAR not found: $jarPath. Build first (gradlew build)."
    exit 1
}

$changelog = if (Test-Path $changelogPath) { Get-Content $changelogPath -Raw } else { "Initial release." }

$apiBase = "https://api.modrinth.com"

# --- Find project id by slug ---
Write-Host "Looking up project $projectSlug ..."
try {
    $proj = Invoke-RestMethod -Uri "$apiBase/v2/project/$projectSlug" -Method Get
    $projectId = $proj.id
    Write-Host "Project found (id: $projectId, title: $($proj.title))."
} catch {
    Write-Error "Project '$projectSlug' not found. Create it at https://modrinth.com/upload (slug 'treasure-chickens'), then run again."
    exit 1
}

# --- Build version metadata JSON (no BOM) ---
$metadata = @{
    name           = $versionTitle
    version_number = $versionNumber
    version_type   = "release"
    game_versions  = $gameVersions
    loaders        = $loaders
    featured       = $true
    changelog      = $changelog
} | ConvertTo-Json -Depth 6

[System.IO.File]::WriteAllText($metaPath, $metadata, (New-Object System.Text.UTF8Encoding($false)))

# --- Upload version via curl multipart ---
Write-Host "Uploading version $versionNumber ..."
$result = & curl.exe -s -X POST "$apiBase/v2/project/$projectId/version" `
    -H "Authorization: Bearer $token" `
    -H "User-Agent: treasure-chickens-publisher/1.0.0" `
    -F "data=<$metaPath" `
    -F "file=@$jarPath" 2>&1

Write-Host $result
$parsed = $null
try { $parsed = $result | ConvertFrom-Json } catch { $parsed = $null }
if ($parsed -and $parsed.id) {
    Write-Host ""
    Write-Host "Done! Version $($parsed.version_number) available: https://modrinth.com/mod/$projectSlug"
} else {
    Write-Host ""
    Write-Host "Upload failed. Check the response above."
    exit 1
}