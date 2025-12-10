# AeroOps – Setup și Workflow 

## 1. Instalare

### 1.1. Instalați următoarele aplicații:
- **WSL2** – din *Company Portal*
- **Rancher Desktop** – din browser (înlocuitor pentru Docker Desktop)
- **pgAdmin** – din *Company Portal* (opțional)

### 1.2. Configurare Rancher Desktop
După instalare:
- În **Preferences → Kubernetes** → dezactivați opțiunea **Enable Kubernetes**
- În **Preferences → Container Engine** → selectați **dockerd (moby)**

La final, bara de stare (jos, dreapta) trebuie să arate aproximativ așa:

```
Version: <versiune> | Network status: online | Kubernetes: deactivated | CE: moby
```

---

## 2. Pornirea proiectului local

1. Clonați repository-ul:
   ```bash
   git clone https://github.com/StefuAlexandru/AeroOps.git
   cd AeroOps
   ```
   *(Alternativ: descărcați manual proiectul, dezarhivați-l și deschideți-l în IntelliJ.)*

2. Porniți **Rancher Desktop**.

3. Din terminalul proiectului, porniți baza de date:
   ```bash
   docker compose up -d
   ```

4. Verificați dacă containerele rulează:
   ```bash
   docker ps
   ```
   Rezultatul ar trebui să arate similar cu:
   ```
   CONTAINER ID   NAME          PORTS
   xxxxxxx        aeroops-db    0.0.0.0:5433->5432/tcp
   xxxxxxx        pgadmin       0.0.0.0:5050->80/tcp
   ```

---

## 3. Conectarea la baza de date

### Variante posibile:

#### 3.1. pgAdmin (aplicația instalată local)
- **Name:** AeroOps
- **Host:** localhost
- **Port:** 5433
- **Database:** flights-db
- **Username:** appuser
- **Password:** secret

#### 3.2. pgAdmin în browser (containerul)
Accesați: [http://localhost:5050](http://localhost:5050)

- **Name:** AeroOps
- **Host:** aeroops-db
- **Port:** 5432
- **Database:** flights-db
- **Username:** appuser
- **Password:** secret

Dacă vă puteți conecta – setup-ul este corect.

---

## 4. Configurarea IntelliJ

În IntelliJ:
- Mergeți la **Run → Edit Configurations**
- La **Active Profile** setați `dev`

Alternativ, puteți rula aplicația direct din terminal:
```bash
.\mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
```

---

## 5. Workflow Git – Reguli pentru echipă

Toată munca se face pe branchul `develop`.
Branchul `master` este folosit **doar pentru release-uri** și nu se modifică direct.

### 5.1. Înainte de a începe lucrul
Actualizați proiectul local:
```bash
git checkout develop
git pull origin develop
```

### 5.2. Crearea unui branch nou pentru taskul tău
Branchurile se fac doar din `develop`.

```bash
git checkout -b <prefix>/<descriere-task>
```

Exemple:
```
feat/add-airport-crud
fix/fix-database-connection
chore/update-ci-workflow
```

**Convenție pentru prefixe:**
| Prefix | Descriere |
|--------|------------|
| feat/ | feature nou |
| fix/ | bug fix |
| chore/ | modificări tehnice / CI |
| docs/ | documentație |
| refactor/ | modificare cod fără schimbare funcțională |

### 5.3. Lucrul local și commitul
După ce ai terminat modificările:
```bash
git status
git add .
git commit -m "Mesaj clar"
```

**Reguli pentru mesaje de commit:**
- Scrie la persoana a treia („implement”, nu „am implementat”)
- Mesajul trebuie să aibă sens și să descrie clar schimbarea

### 5.4. Urcarea modificărilor pe GitHub
```bash
git push -u origin <prefix>/<descriere-task>
```

După push, pe GitHub va apărea butonul **“Compare & pull request”** – apasă pe el.

### 5.5. Crearea Pull Request-ului (PR)
1. Verifică:
    - **Base branch:** `develop`
    - **Compare branch:** `<prefix>/<descriere-task>`
2. Adaugă un titlu și o descriere clară
3. Selectează reviewer-ul principal (`@StefuAlexandru`)
4. Așteaptă ca pipeline-ul **CI / build** să ruleze cu succes
5. După aprobarea PR-ului, apasă **“Squash and merge”**

> Nu face PR către `master`.

### 5.6. După ce PR-ul a fost făcut merge
Actualizează branchul `develop` local:
```bash
git checkout develop
git pull origin develop
```


### 5.7. Dacă vrei să lucrezi la alt task
```bash
git checkout develop
git pull origin develop
git checkout -b feat/<noul-task>
```

---

## 6. Politică de merge

- Pentru toate PR-urile către `develop`: se folosește **Squash and merge**
- Pentru release-uri (`develop → master`): se folosește **Merge commit**

---

## 7. Reguli generale

1. Nu se face commit direct pe `develop` sau `master`
2. Toate modificările se fac prin branchuri și PR-uri
3. `develop` trebuie să fie mereu stabil și buildabil
4. Commiturile trebuie să fie mici și clare
5. Nu faceți merge local manual între branchuri – folosiți PR
6. Înainte de a lucra, faceți întotdeauna `git pull origin develop`
7. Dacă aveți conflicte, le rezolvați local, apoi commit + push

---

