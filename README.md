# studentAdministrator (Swing, Java)





Application de gestion d'étudiants et de leurs notes (CRUD + recherche + affichage des notes) en Java SE + Swing, persistance en CSV.



**Fonctionnalités**

\- CRUD étudiants (ajout, modification, suppression).

\- Gestion des notes par matière(ajout / suppression).

\- Recherche par nom/prénom.

\- Affichage #matières et moyenne (arrondi).

\- Coloration des notes : <10 rouge, 10–12 orange, >12 vert.

\- Fichier de données : `data/students.csv` (créé auto s’il n’existe pas).



 **Architecture rapide**

\- `model/Student` : entité.

\- `repository/FileStudentRepository` : persistance CSV (+ JSON pour les notes).

\- `service/StudentService` : règles métier.

\- `ui/\*` : Swing (`LoginFrame`, `MainFrame`, `StudentPanel`, …).



**Lancer en local (IntelliJ)**

\- JDK \*\*17\*\* (ou 21).

\- Run `com.esgi.ui.AppLauncher`.



**Identifiants démo :**

\- Admin : `admin / admin` (CRUD activé)

\- Lecture seule : n’importe quel autre login



Build du JAR exécutable

Requis : Maven 3.x, JDK 17/21.



**bash**

mvn -q clean package







**## Lancer le JAR**

java -jar dist/student-admin.jar

exemple: PS C:\\Users\\ismai\\OneDrive\\Documents\\ESGI\\studentAdministrator\\student-admin> java -jar target\\student-admin.jar





