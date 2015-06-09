// Refer to http://www.programcreek.com/java-api-examples/index.php?api=groovy.lang.GroovyShell
def shell = new GroovyShell()
def code = new File("melon.groovy").text
def closure = shell.evaluate(code)
def output = closure("tracks", null)
println output
