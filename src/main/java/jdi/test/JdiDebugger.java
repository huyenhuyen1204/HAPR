package jdi.test;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author bonnie
 */
public class JdiDebugger {

    /**
     * @param options
     * @param main
     * @param classPattern
     * @param methodName
     * @param lineNumber
     * @throws IOException
     * @throws IllegalConnectorArgumentsException
     * @throws VMStartException
     * @throws InterruptedException
     * @throws AbsentInformationException
     * @throws IncompatibleThreadStateException
     */
    public static void onMethodExit(String options, String main, String classPattern, String methodName, int lineNumber) throws IOException, IllegalConnectorArgumentsException, VMStartException, InterruptedException, AbsentInformationException, IncompatibleThreadStateException {

        // create and launch a virtual machine
        VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
        LaunchingConnector lc = vmm.defaultConnector();
        Map<String, Connector.Argument> env = lc.defaultArguments();
//        env.get("options").setValue(options);
        env.get("main").setValue(main);
//        env.get("test").setValue(main);
//        env.get("suspend").setValue("true");

        VirtualMachine vm = lc.launch(env);

        // create a class prepare request
        EventRequestManager erm = vm.eventRequestManager();
        ClassPrepareRequest r = erm.createClassPrepareRequest();
        r.addClassFilter(classPattern);
        r.enable();

        EventQueue queue = vm.eventQueue();
        while (true) {
            EventSet eventSet = queue.remove();
            EventIterator it = eventSet.eventIterator();
            while (it.hasNext()) {
                Event event = it.nextEvent();
                if (event instanceof ClassPrepareEvent) {
                    ClassPrepareEvent evt = (ClassPrepareEvent) event;
                    ClassType classType = (ClassType) evt.referenceType();

                    classType.methodsByName(methodName).forEach(new Consumer<Method>() {
                        @Override
                        public void accept(Method m) {
                            Location location = null;
                            try {
                                location = classType.locationsOfLine(lineNumber).get(0);
                            } catch (AbsentInformationException e) {
                                e.printStackTrace();
                            }
//                            try {
//                                locations = m.allLineLocations();
//                            } catch (AbsentInformationException ex) {
////                                Logger.getLogger(JdiDebuggerOld.class.getName()).log(Level.SEVERE, null, ex);
//                            }
                            // get the last line location of the function and enable the 
                            // break point
//                            Location location = locations.get(locations.size() - 1);
                            BreakpointRequest bpReq = erm.createBreakpointRequest(location);
                            bpReq.enable();
                        }
                    });

                }
                if (event instanceof BreakpointEvent) {
                    // disable the breakpoint event
                    event.request().disable();

                    ThreadReference thread = ((BreakpointEvent) event).thread();
                    StackFrame stackFrame = thread.frame(0);

                    // print all the visible variables with the respective values
                    Map<LocalVariable, Value> visibleVariables = (Map<LocalVariable, Value>) stackFrame.getValues(stackFrame.visibleVariables());
                    for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                        System.out.println(entry.getKey() + ":" + entry.getValue());
                    }
                }
                vm.resume();
            }
        }
    }

    public static void main(String[] args) throws IncompatibleThreadStateException, IllegalConnectorArgumentsException, IOException, VMStartException, AbsentInformationException, InterruptedException {
        JdiDebugger.onMethodExit("ABC", "jdi.test.MyTest", "jdi.test.MyTest", "test_getAt_method_in_board", 113);
    }
}