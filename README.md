Relaxe
=======================================================================

What it is?
-----------------------------------------------------------------------

Relaxe is a Java library which provides an object-oriented 
API for SQL language and database metadata.

Built on top of those, it additionally provides
a code generator and an API for access relational data.

Data Access API nicely combines generality and type-safety.


Basic usage
-----------------------------------------------------------------------

Generate sources:

  java -cp relaxe.jar:slf4j-api-1.7.5.jar com.appspot.relaxe.build.Builder ...



Samples
-----------------------------------------------------------------------

Database metadata:

  [ColumnListSample.java] (src/samples/java/com/appspot/relaxe/samples/ColumnListSample.java)
  [ForeignKeyListSample.java] (src/samples/java/com/appspot/relaxe/samples/ForeignKeyListSample.java)

Data Access API:

  [PagilaEntityQuerySample.java] (src/samples/java/com/appspot/relaxe/samples/pagila/PagilaEntityQuerySample.java)
  [PagilaEntitySessionSample.java] (src/samples/java/com/appspot/relaxe/samples/pagila/PagilaEntitySessionSample.java)


Dependencies
-----------------------------------------------------------------------

<dl>
  <dt>Compilation</dt>
  <dd>SLF4J API</dd>
  <dt>Runtime</dt>
  <dd>
    * JDBC Driver (for the RDBMS of your choice) 
    * SLF4J -backend
  </dd>
</dl>

Relaxe can currently be used with the following RDBMSs:

- PostgreSQL
- MySQL 5.5
- HSQLDB 2.3
- MariaDB *)


*) some of the most recent versions (namely 1.1.3 - 1.1.5) of 
   MariaDB Client Library for Java Applications are known
   to not work with Relaxe


License
-----------------------------------------------------------------------

Relaxe is currently licensed under 
[GNU Affero General Public License, version 3](http://www.gnu.org/licenses/agpl-3.0.html)


