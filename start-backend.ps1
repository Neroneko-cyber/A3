# ═══════════════════════════════════════════════════════════════════════════════
# start-backend.ps1 — Otaku E-Commerce Backend Launcher
# Menangani konflik port secara otomatis sebelum menjalankan Spring Boot
# ═══════════════════════════════════════════════════════════════════════════════

param(
    [int]$Port = 8321
)

$BACKEND_DIR = Join-Path $PSScriptRoot "backend"

Write-Host ""
Write-Host "═══════════════════════════════════════════" -ForegroundColor DarkCyan
Write-Host "  🚀 Otaku E-Commerce Backend Launcher" -ForegroundColor Cyan
Write-Host "═══════════════════════════════════════════" -ForegroundColor DarkCyan
Write-Host ""

# ── 1. Cek apakah SQL Server berjalan ─────────────────────────────────────────
Write-Host "🔍 [1/3] Memeriksa SQL Server..." -ForegroundColor Cyan
$sqlService = Get-Service -Name 'MSSQLSERVER' -ErrorAction SilentlyContinue
if ($null -eq $sqlService) {
    # Coba nama instance default lain (SQL Server Express)
    $sqlService = Get-Service -Name 'MSSQL$SQLEXPRESS' -ErrorAction SilentlyContinue
}

if ($null -eq $sqlService) {
    Write-Host "  ⚠️  SQL Server service tidak ditemukan. Pastikan SQL Server terinstall." -ForegroundColor Yellow
    Write-Host "  ℹ️  Melanjutkan... (aplikasi mungkin gagal connect ke database)" -ForegroundColor Gray
} elseif ($sqlService.Status -ne 'Running') {
    Write-Host "  ⚠️  SQL Server tidak berjalan. Mencoba memulai..." -ForegroundColor Yellow
    try {
        Start-Service $sqlService.Name
        Start-Sleep -Seconds 3
        Write-Host "  ✅ SQL Server berhasil dijalankan." -ForegroundColor Green
    } catch {
        Write-Host "  ❌ Gagal memulai SQL Server: $_" -ForegroundColor Red
        Write-Host "  ℹ️  Jalankan manual: Start-Service MSSQLSERVER" -ForegroundColor Gray
    }
} else {
    Write-Host "  ✅ SQL Server berjalan." -ForegroundColor Green
}

Write-Host ""

# ── 2. Bersihkan Port jika sudah digunakan ─────────────────────────────────────
Write-Host "🔍 [2/3] Memeriksa port $Port..." -ForegroundColor Cyan

$netstatOutput = netstat -ano 2>$null | Select-String ":$Port\s" | Where-Object { $_ -match "LISTENING" }

if ($netstatOutput) {
    # Ambil PID dari output netstat
    $pidValue = ($netstatOutput.ToString().Trim() -split '\s+')[-1]
    
    if ($pidValue -match '^\d+$') {
        Write-Host "  ⚠️  Port $Port sedang digunakan oleh proses PID $pidValue." -ForegroundColor Yellow
        
        # Cek nama proses
        $processName = (Get-Process -Id $pidValue -ErrorAction SilentlyContinue).ProcessName
        if ($processName) {
            Write-Host "  ℹ️  Proses: $processName (PID: $pidValue)" -ForegroundColor Gray
        }
        
        Write-Host "  🔧 Menghentikan proses..." -ForegroundColor Yellow
        try {
            taskkill /PID $pidValue /F 2>$null | Out-Null
            Start-Sleep -Seconds 2
            Write-Host "  ✅ Port $Port berhasil dibebaskan." -ForegroundColor Green
        } catch {
            Write-Host "  ❌ Gagal menghentikan proses: $_" -ForegroundColor Red
            Write-Host "  ℹ️  Coba jalankan manual: taskkill /PID $pidValue /F" -ForegroundColor Gray
            exit 1
        }
    } else {
        Write-Host "  ⚠️  Tidak bisa mengambil PID dari output netstat." -ForegroundColor Yellow
    }
} else {
    Write-Host "  ✅ Port $Port tersedia." -ForegroundColor Green
}

Write-Host ""

# ── 3. Jalankan Spring Boot ───────────────────────────────────────────────────
Write-Host "🚀 [3/3] Menjalankan Spring Boot di port $Port..." -ForegroundColor Cyan
Write-Host "  ℹ️  Tekan Ctrl+C untuk menghentikan server." -ForegroundColor Gray
Write-Host ""

if (-not (Test-Path $BACKEND_DIR)) {
    Write-Host "❌ Folder backend tidak ditemukan: $BACKEND_DIR" -ForegroundColor Red
    exit 1
}

Set-Location $BACKEND_DIR

# Set port via ENV variable agar override application.yml
$env:SERVER_PORT = $Port

# Jalankan Maven
mvn spring-boot:run

# Reset env variable setelah selesai
Remove-Item Env:SERVER_PORT -ErrorAction SilentlyContinue
