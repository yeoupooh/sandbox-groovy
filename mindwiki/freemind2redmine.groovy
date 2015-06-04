println "Loading...[" + args[0] + "]"

File file = new File(args[0]);
if (file.exists() == false) {
    println args[0] + " does not exist."
    return
}

def root = new XmlSlurper().parseText(file.text)

class Visitor {
    Integer depth = 0
    StringBuilder sbWiki = new StringBuilder()
}

def traverse
def visitor = new Visitor()

traverse = { node, v ->
    println "[traverse] depth=" + v.depth + ",node:name=" + node.name() + ",size=" + node.size() + ",text=" + node
            .'@TEXT' + "," +
            "id=" +
            node.'@ID' + "," +
            "modified=" + node.'@MODIFIED' + ",link=" + node.'@LINK' + ",folded=" + node.'@FOLDED'
    if (node.name() == "node") {
        def prefix = ""
        if (v.depth >= 1 && v.depth <= 3) {
            prefix = "h" + v.depth + ". "
        }
        v.sbWiki.append(prefix + node.'@TEXT'.toString() + "\n\n")
    }
    node.children().each { it ->
        v.depth++
        traverse(it, v)
        v.depth--
    }
}

traverse(root, visitor)

println visitor.sbWiki.toString()