import com.mongodb.*
import groovy.json.JsonSlurper

@Grapes(
        @Grab(group = 'org.mongodb', module = 'mongo-java-driver', version = '2.13.2')
)

// http://mongodb.github.io/mongo-java-driver/2.13/getting-started/quick-tour/

// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
// if it's a member of a replica set:
//MongoClient mongoClient = new MongoClient();
// or
//MongoClient mongoClient = new MongoClient( "localhost" );
// or
//MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
//MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
//        new ServerAddress("localhost", 27018),
//        new ServerAddress("localhost", 27019)));

def confRes = this.getClass().getResource('/mongo.config.json')
if (confRes == null) {
    return null
}

def configJsons = new JsonSlurper().parseText(confRes.text)

def userName = configJsons.username
def database = configJsons.database
char[] password = configJsons.password

MongoCredential credential = MongoCredential.createCredential(userName, database, password);
MongoClient mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(credential));

DB db = mongoClient.getDB(database);
println db

DBCollection coll = db.getCollection(configJsons.collection);
println coll
println coll.getCount()

DBObject myDoc = coll.findOne();
println myDoc

