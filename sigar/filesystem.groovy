import org.hyperic.sigar.FileSystem
import org.hyperic.sigar.Sigar

@Grapes(
        @Grab(group = 'org.fusesource', module = 'sigar', version = '1.6.4')
)


Sigar sigar = new Sigar()

def fs = sigar.getFileSystemList()
println fs

fs.eachWithIndex { FileSystem entry, int i ->
    println entry.getDirName() + ": " + entry.getType()
    if (entry.getType() == FileSystem.TYPE_LOCAL_DISK) {
        def du = sigar.getDiskUsage(entry.getDirName())
        println du
    }
}

sigar.close();
