import java.util.regex.*

String line;

//Executable file name of the application to check. 
final String applicationToCheck = "application.jar"
boolean applicationIsOk = false;

//Running command that will get all the working processes.
Process proc = Runtime.getRuntime().exec("ps -ef");
InputStream stream = proc.getInputStream();
BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

//Parsing the input stream.
while ((line = reader.readLine()) != null) {
    println line
    Pattern pattern = Pattern.compile(applicationToCheck);
    Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
        applicationIsOk = true;
        break;
    }
}
