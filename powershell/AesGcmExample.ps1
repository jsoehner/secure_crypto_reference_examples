# AES-256-GCM authenticated encryption with a fresh 96-bit nonce and AAD.

$key = [System.Security.Cryptography.AesGcm]::GenerateKey(32)
$nonce = [System.Security.Cryptography.RandomNumberGenerator]::GetBytes(12)
$aad = [System.Text.Encoding]::UTF8.GetBytes("app=v1;purpose=reference")
$plaintext = [System.Text.Encoding]::UTF8.GetBytes("confidential data")

$aesGcm = New-Object System.Security.Cryptography.AesGcm($key, $true)
$ciphertext = New-Object byte[] ($plaintext.Length)
$tag = New-Object byte[] 16

$aesGcm.Encrypt($nonce, $plaintext, $aad, $ciphertext, $tag)

# Combine ciphertext and tag for the envelope
$envelope = @{
    Nonce = $nonce
    CiphertextAndTag = $ciphertext + $tag
}

Write-Host "Ciphertext and Tag: $($envelope.CiphertextAndTag | ForEach-Object { $_.ToString("X2") } -join '')"

# Decrypt
$decrypted = New-Object byte[] ($ciphertext.Length)
$aesGcm.Decrypt($envelope.Nonce, $envelope.CiphertextAndTag[0..($ciphertext.Length-1)], $aad, $decrypted, $tag)

if ([System.Text.Encoding]::UTF8.GetString($decrypted) -eq "confidential data") {
    Write-Host "AesGcmExample round-trip OK"
} else {
    Write-Error "Decryption failed"
}

$aesGcm.Dispose()
