import java.util.regex.*

String line;

//Executable file name of the application to check. 
final String applicationToCheck = "run"
boolean applicationIsOk = false;

//Running command that will get all the working processes.
Process proc = Runtime.getRuntime().exec("ps -ef");
InputStream stream = proc.getInputStream();
BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

//Parsing the input stream.
def ps = []
while ((line = reader.readLine()) != null) {
    //println line
    def items = line.tokenize()
    //println items
    print "UID="+items[0]
    print "PID="+items[1]
    print "PPID="+items[2]
    print "C="+items[3]
    print "STIME="+items[4]
    print "TTY="+items[5]
    print "TIME="+items[6]
    print "CMD="+items[7]
    print "size="+items.size()
    println "CMD="+items[7..items.size()-1].join(" ")
    def psItem = [:]
    psItem.put("pid",items[1])
    psItem.put("cmd",items[7..items.size()-1].join(" "))
    ps << psItem
    Pattern pattern = Pattern.compile(applicationToCheck);
    Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
        applicationIsOk = true;
        println "found"
   //     break;
    }
}

println ps

