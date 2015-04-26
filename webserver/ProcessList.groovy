import java.util.regex.*

String line;

def getCurrent() {
	//Running command that will get all the working processes.
	Process proc = Runtime.getRuntime().exec("ps -ef");
	InputStream stream = proc.getInputStream();
	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

	//Parsing the input stream.
	def ps = []
	while ((line = reader.readLine()) != null) {
		def items = line.tokenize()
		
		def psItem = [:]
		psItem.put("uid",items[0])
		psItem.put("pid",items[1])
		psItem.put("ppid",items[2])
		psItem.put("c",items[3])
		psItem.put("stime",items[4])
		psItem.put("tty",items[5])
		psItem.put("time",items[6])
		psItem.put("cmd",items[7..items.size()-1].join(" "))
		
		ps << psItem
	}

	return ps
}

def findCommand(ps, patternToCompile) { 
	def found = false
	ps.any { process ->
		Pattern pattern = Pattern.compile(patternToCompile);
		Matcher matcher = pattern.matcher(process["cmd"]);
		if (matcher.find()) {
			found = true
			return true // break in any
		}
	}
	return found
}
