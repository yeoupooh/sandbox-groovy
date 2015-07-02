import org.hyperic.sigar.*

@Grapes(
        @Grab(group = 'org.fusesource', module = 'sigar', version = '1.6.4')
)

// https://github.com/hyperic/sigar/blob/master/bindings/java/examples/ProcessState.java
public class ProcessState {

    private static String getStateString(char state) {
        switch (state) {
            case ProcState.SLEEP:
                return "Sleeping";
            case ProcState.RUN:
                return "Running";
            case ProcState.STOP:
                return "Suspended";
            case ProcState.ZOMBIE:
                return "Zombie";
            case ProcState.IDLE:
                return "Idle";
            default:
                return String.valueOf(state);
        }
    }
}

String pid;
if (args.length == 0) {
    pid = "\$\$"; //default to this process
} else {
    pid = args[0];
}

print pid

Sigar sigar = new Sigar();

ProcState procState = sigar.getProcState(pid);
String state;

System.out.println(procState.getName() + ": " + ProcessState.getStateString(procState.getState()));

sigar.close();
