# Deploy SkyRoute to Azure

SkyRoute is deployed as one executable Spring Boot JAR. The production Maven
profile builds Angular, copies it into the JAR, and packages the REST API with
the UI. Azure Database for MySQL stores flights and bookings.

## 1. Create a resource group

In the Azure portal, search for **Resource groups**, select **Create**, and use:

- Resource group: `skyroute-rg`
- Region: a region close to you

Use the same region for the database and App Service when possible.

## 2. Create Azure Database for MySQL

Search for **Azure Database for MySQL flexible servers** and select **Create**.

- Resource group: `skyroute-rg`
- Server name: a globally unique name, such as `skyroute-mysql-<your-name>`
- Workload type: Development
- Authentication: MySQL authentication
- Administrator username: choose an admin username
- Password: create a strong password

For a learning deployment, enable public access under **Networking**, add your
current client IP, and allow connections from Azure services. For production,
use private networking instead.

After the server is ready, connect with MySQL Workbench:

- Host: `<server-name>.mysql.database.azure.com`
- Port: `3306`
- Username: the administrator username
- SSL mode: Required

Create the application database:

```sql
CREATE DATABASE skyroute;
```

## 3. Create the App Service

Search for **App Services**, select **Create > Web App**, and use:

- Resource group: `skyroute-rg`
- Name: a globally unique name, such as `skyroute-<your-name>`
- Publish: Code
- Runtime stack: Java 21
- Java web server: Java SE
- Operating system: Linux
- Region: the same region as MySQL
- Pricing plan: the lowest development tier that meets your needs

## 4. Configure application settings

In the Web App, open **Settings > Environment variables** and add:

```text
SPRING_DATASOURCE_URL=jdbc:mysql://<server-name>.mysql.database.azure.com:3306/skyroute?sslMode=REQUIRED
SPRING_DATASOURCE_USERNAME=<mysql-admin-username>
SPRING_DATASOURCE_PASSWORD=<mysql-admin-password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

Save the settings. App Service restarts after configuration changes.

After the first successful deployment creates the tables, change
`SPRING_JPA_HIBERNATE_DDL_AUTO` from `update` to `validate`.

## 5. Configure GitHub Actions

From the App Service **Overview** page, download the publish profile.

In the GitHub repository, open **Settings > Secrets and variables > Actions**:

1. Create a repository secret named `AZURE_WEBAPP_PUBLISH_PROFILE`.
2. Paste the complete downloaded publish-profile XML as its value.
3. Open the **Variables** tab.
4. Create `AZURE_WEBAPP_NAME` with the exact App Service name.

If downloading the profile is disabled, enable SCM basic authentication in the
App Service configuration, download the profile, and disable it again after
moving to a stronger deployment identity.

## 6. Deploy

Commit and push the deployment files:

```powershell
git add pom.xml src/main/resources/application.properties `
  src/main/java/com/learn/skyroute/config/WebConfig.java `
  .github/workflows/azure-deploy.yml AZURE_DEPLOYMENT.md
git commit -m "Prepare SkyRoute for Azure deployment"
git push
```

Open the GitHub repository's **Actions** page and watch
**Build and deploy SkyRoute to Azure**.

## 7. Verify

Replace `<app-name>` with the App Service name:

```text
https://<app-name>.azurewebsites.net/
https://<app-name>.azurewebsites.net/api/health
https://<app-name>.azurewebsites.net/bookings
```

The first application startup generates the monthly flight schedule. Search for
flights, create a booking, and confirm that the booking appears in Azure MySQL.

## Local development after externalizing the password

Set the password in the backend terminal before starting Spring Boot:

```powershell
$env:SPRING_DATASOURCE_PASSWORD="your-local-mysql-password"
.\mvnw.cmd spring-boot:run
```

The frontend development command remains:

```powershell
cd frontend
npm start
```

To produce the same combined JAR locally:

```powershell
.\mvnw.cmd -Pproduction clean package
```
