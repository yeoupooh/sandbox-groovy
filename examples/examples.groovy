#!/usr/bin/env groovy

def testFileListing = { dir ->
    dir.eachFileRecurse { file ->                      
        println file.name
    }
}

def testReadFile = {
    def is = new File('.', 'hello.html').newInputStream()
    // do something
    println 'do something'
    is.close()
}

def select = System.console().readLine '''
Menu:

    1) testFileListing
    2) testReadFile
    
What is your favorite? '''

println "Your choice: $select"

switch (select.toInteger()) {
    case 1: testFileListing(new File('.')); break;
    case 2: testReadFile(); break;
}
