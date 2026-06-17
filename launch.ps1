$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.11.10-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
$pinfo = New-Object System.Diagnostics.ProcessStartInfo
$pinfo.FileName = "$env:JAVA_HOME\bin\java.exe"
$pinfo.Arguments = "-jar D:\JeecgBoot-main\jeecg-boot\jeecg-module-system\jeecg-system-start\target\jeecg-system-start-3.9.2.jar --spring.profiles.active=dev"
$pinfo.UseShellExecute = $false
$pinfo.RedirectStandardOutput = $true
$pinfo.RedirectStandardError = $true
$pinfo.WorkingDirectory = "D:\JeecgBoot-main"
$p = [System.Diagnostics.Process]::Start($pinfo)
$p.Id | Out-File "D:\JeecgBoot-main\backend-pid.txt"
