CODEQUEST — COMPLETE MAVEN ECLIPSE WAR PROJECT (JAVA 21 / TOMCAT 10.1)
===================================================================
This package is the Maven version of the complete CodeQuest dynamic web application.
It includes 4 SQL tables, 6 sample questions, 16 Java classes, JSP pages, styles,
and 6 illustrative site preview images. JSP uses scriptlets, without JSTL/EL.

IMPORTANT: Use FILE > IMPORT > MAVEN > EXISTING MAVEN PROJECTS, not New Maven Project.
This avoids the Maven wizard failure previously experienced. Do NOT separately
create a new CodeQuest Maven project before importing.

1. Extract the ZIP, e.g. C:\CodeQuestProjects\CodeQuest_Maven_Java21_Tomcat10.
2. Eclipse > File > Switch Workspace > Other; use a clean workspace if an old
   CodeQuest project conflicts (do NOT delete your old project files).
3. Eclipse > File > Import > Maven > Existing Maven Projects > Next.
   Browse to the extracted CodeQuest_Maven_Java21_Tomcat10 folder.
   Confirm pom.xml appears under Projects > Finish.
4. Allow Maven downloads. Eclipse > Window > Preferences > Maven > ensure Offline
   is UNCHECKED. Right-click project > Maven > Update Project > Force Update > OK.
5. Ensure Java 21 is selected in Eclipse > Preferences > Java > Installed JREs,
   and that project Properties > Java Compiler has level 21.
6. Right-click project > Properties > Project Facets: Java 21,
   Dynamic Web Module 6.0. Under Targeted Runtimes choose Tomcat 10.1.
   If Eclipse does not detect this as a web project, use
   Properties > Project Facets > Convert to faceted form, enable Dynamic Web
   Module 6.0, then configure Tomcat 10.1 as a targeted runtime.
7. Execute database/codequest_db.sql in MySQL Workbench; verify four tables.
8. Edit src/main/resources/database.properties: set db.password and verify port.
   This file contains local credentials; never upload it to GitHub.
9. Right-click project > Run As > Maven build..., set Goals = clean package,
   click Run. The result should be target/CodeQuest.war.
10. Right-click project > Run As > Run on Server > Tomcat v10.1.
    Open http://localhost:8080/CodeQuest/.
11. Locally only, visit http://localhost:8080/CodeQuest/setup-admin to create
    the first admin. Setup is restricted to localhost and one-time only.
12. Test Register > Login > Missions > Answer > XP/Level > Leaderboard;
    admin Login > Add questions > View questions > Logout.

DO NOT manually add a MySQL JDBC JAR to this Maven project. Maven manages
com.mysql:mysql-connector-j:8.4.0, and Tomcat supplies Jakarta Servlet 6.

For errors: Eclipse > Window > Show View > Problems; provide FIRST RED ERROR.
- Missing artifact: internet/Maven repository access; Maven Update with Force Update.
- Java 21 errors: verify Installed JREs; JAVA_HOME; Project Facets.
- 404: verify Tomcat 10.1 server, context CodeQuest, deployed module.
- Database connection: check MySQL running, port, user, password.
- cookie header warnings in Eclipse's Error Log generally do not by
  themselves explain a build failure.

Security: delete or disable /setup-admin before public deployment;
use HTTPS and least-privilege DB credentials for production. PREVIEWS are
illustrative design screenshots with sample user data, not live screenshots.

Build verification note: this environment lacks the Maven executable and
cannot run your local Tomcat/MySQL. Inspect the Eclipse Problems tab and the
Maven build log on your Windows laptop to verify successful deployment.
