// https://gist.github.com/bradkarels/7cff6b3e2b45eea30c21

@GrabConfig(systemClassLoader = true)
@Grab(group = 'com.h2database', module = 'h2', version = '1.3.176')

import java.sql.*
import groovy.sql.Sql
import org.h2.jdbcx.JdbcConnectionPool

println("More groovy...")

def sql = Sql.newInstance("jdbc:h2:things", "sa", "sa", "org.h2.Driver")  // DB files for 'things' in current directory (./hello.h2.db)
//def sql = Sql.newInstance("jdbc:h2:file:things", "sa", "sa", "org.h2.Driver")  // 'file' keyword is optional
//def sql = Sql.newInstance("jdbc:h2:~/things", "sa", "sa", "org.h2.Driver")  // DB files for 'things' in current user home directory (~/things.h2.db)
//def sql = Sql.newInstance("jdbc:h2:/var/h2/things", "sa", "sa", "org.h2.Driver")  // DB files for 'things' in specified directory (/var/h2/things.h2.db)

sql.execute("DROP TABLE IF EXISTS things")

def createTbl = '''
CREATE TABLE things (
  id INT PRIMARY KEY,
  thing1 VARCHAR(50),
  thing2 VARCHAR(100)
)
'''

sql.execute(createTbl)
sql.execute("INSERT INTO things VALUES(:id, :thing1, :thing2)", [id: 0, thing1: 'I am thing1', thing2: 'I am thing2'])
sql.execute("INSERT INTO things VALUES(:id, :thing1, :thing2)", [id: 1, thing1: 'foo', thing2: 'bar'])
sql.execute("INSERT INTO things VALUES(:id, :thing1, :thing2)", [id: 2, thing1: 'Alisa', thing2: 'Yeoh'])

def query = "SELECT * FROM things"

sql.eachRow(query) { row ->
    println "$row.id - ${row.thing1}::$row.thing2"
}

//Housekeeping
sql.close()

println("More java-ish...")

def dropTblTester = "DROP TABLE IF EXISTS tester"

def createTestTbl = '''
CREATE TABLE tester (
  id INT PRIMARY KEY,
  thing1 VARCHAR(50),
  thing2 VARCHAR(100)
)'''

JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:test", "sa", "sa")
Connection conn = cp.getConnection()
conn.createStatement().execute(dropTblTester)
conn.createStatement().execute(createTestTbl)

conn.createStatement().execute("INSERT INTO tester (id, thing1, thing2) VALUES (0, 'x', 'y')")
conn.createStatement().execute("INSERT INTO tester (id, thing1, thing2) VALUES (1, 'a', 'b')")
conn.createStatement().execute("INSERT INTO tester (id, thing1, thing2) VALUES (2, 'e', 'f')")
Statement stmt = conn.createStatement()
ResultSet rs = stmt.executeQuery("SELECT * FROM tester")

while (rs.next()) {
    int id = rs.getInt("id")
    String t1 = rs.getString("thing1")
    String t2 = rs.getString("thing2")
    println("$id::$t1::$t2")
}
//Housekeeping
conn.close()
cp.dispose()

// Expect:

/*
More groovy...
0 - I am thing1::I am thing2
1 - foo::bar
2 - Alisa::Yeoh
More java-ish...
0::x::y
1::a::b
2::e::f
*/