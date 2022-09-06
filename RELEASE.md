# Release

Il processo di release viene eseguito da Maven (https://central.sonatype.org/publish/publish-maven)

## Prerequisiti
 
* Java 1.8
* Maven 3.6.3
* Account sonatype (https://central.sonatype.org/publish/publish-guide/)
* GPG (https://central.sonatype.org/publish/requirements/gpg/)
* Maven settings.xml opportunamente configurato (vedi sotto)

### Configurazione settings.xml

Il file ~/.m2/settings.xml deve essere opportunamente configurato con le credenziali di OSSRH come descritto dalla guida https://central.sonatype.org/publish/release

Nella sezione `<servers>` devono essere presenti le credenziali per OSSRH

```xml
    <server>
      <id>ossrh</id>
      <username>myusername</username>
      <password>mypassword</password>
    </server>
```

Nella sezione `<profiles>` deve essere presente il profilo `ossrh` che contiene le informazioni per generare firma e hash con GPG

```xml
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>/path/to/executable</gpg.executable><!-- se nel path basta 'gpg' -->
        <gpg.passphrase>mypassphrase</gpg.passphrase>
      </properties>
    </profile>
```

## Esecuzione

```bash
$ mvn -Prelease release:prepare
$ git push --tags
$ mvn -Prelease release:perform
```

