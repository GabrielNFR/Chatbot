git fetch origin --prune
git checkout main
git pull --ff-only

$branches = git for-each-ref --format='%(refname:short)|%(upstream:track)' refs/heads

$orfas = $branches |
    Where-Object { $_ -match '\[gone\]' } |
    ForEach-Object { ($_ -split '\|')[0] }

if ($orfas.Count -eq 0) {
    Write-Host "Nenhuma branch local para remover."
} else {
    foreach ($branch in $orfas) {
        Write-Host "Removendo branch local (PR ja mergeada): $branch"
        git branch -D $branch
    }
}

Write-Host ""
Write-Host "Branches locais restantes:"
git branch -vv
