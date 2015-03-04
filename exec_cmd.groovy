def command = """sleep 3"""// Create the String
println "before execute"
def proc = command.execute()                 // Call *execute* on the string
println "after execute"
// if not wait the process, it causes "Caught: java.lang.IllegalThreadStateException: process hasn't exited" in the end
//proc.waitFor()                               // Wait for the command to finish
//println "after waitfor"

// Obtain status and output
try {
println "return code: ${ proc.exitValue()}"
println "stderr: ${proc.err.text}"
println "stdout: ${proc.in.text}" // *out* from the external program is *in* for groovy
} catch(e) {
println e
}

