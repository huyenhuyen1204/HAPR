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
import com.sun.jdi.request.StepRequest;
import com.sun.tools.jdi.StringReferenceImpl;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class JDIExampleDebugger<T> {

    private Class debugClass;
    private int[] breakPointLines;

    public Class getDebugClass() {
        return debugClass;
    }

    public void setDebugClass(Class debugClass) {
        this.debugClass = debugClass;
    }

    public int[] getBreakPointLines() {
        return breakPointLines;
    }

    public void setBreakPointLines(int[] breakPointLines) {
        this.breakPointLines = breakPointLines;
    }

    /**
     * Sets the debug class as the main argument in the connector and launches the VM
     * @return VirtualMachine
     * @throws IOException
     * @throws IllegalConnectorArgumentsException
     * @throws VMStartException
     */
    public VirtualMachine connectAndLaunchVM() throws IOException, IllegalConnectorArgumentsException, VMStartException {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
//        arguments.get("main").setValue(debugClass.getName());
        arguments.get("main").setValue(TestRunner.class.getName());
//        arguments.get("suspend").setValue("true");
        VirtualMachine vm = launchingConnector.launch(arguments);
        return vm;
    }

    /**
     * Creates a request to prepare the debug class, add filter as the debug class and enables it
     * @param vm
     */
    public void enableClassPrepareRequest(VirtualMachine vm) {
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest.addClassFilter(debugClass.getName());
        classPrepareRequest.enable();
    }

    /**
     * Sets the break points at the line numbers mentioned in breakPointLines array
     * @param vm
     * @param event
     * @throws AbsentInformationException
     */
    public void setBreakPoints(VirtualMachine vm, ClassPrepareEvent event) throws AbsentInformationException {
        ClassType classType = (ClassType) event.referenceType();
        for(int lineNumber: breakPointLines) {
            Location location = classType.locationsOfLine(lineNumber).get(0);
            BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
            bpReq.enable();
        }
    }


    /**
     * Sets the break points at the line numbers mentioned in breakPointLines array
     * @param vm
     * @param event
     * @throws AbsentInformationException
     */
    public void setMethodBreakPoints(VirtualMachine vm, ClassPrepareEvent event, String methodName, String classPattern) throws AbsentInformationException {
        // create a class prepare request
        EventRequestManager erm = vm.eventRequestManager();
        ClassPrepareRequest r = erm.createClassPrepareRequest();
        r.addClassFilter(classPattern);
        r.enable();
        ClassType classType = (ClassType) event.referenceType();
//        for(int lineNumber: breakPointLines) {
//            Location location = classType.locationsOfLine(lineNumber).get(0);
//            BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
//            bpReq.enable();
//        }

        classType.methodsByName(methodName).forEach(new Consumer<Method>() {
            @Override
            public void accept(Method m) {
//                List<Location> locations = null;
//                try {
//                    locations = m.allLineLocations();
//                } catch (AbsentInformationException ex) {
////                                Logger.getLogger(JdiDebuggerOld.class.getName()).log(Level.SEVERE, null, ex);
//                }
                // get the last line location of the function and enable the
                // break point
//                Location location = locations.get(locations.size() - 1);
//                BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
//                bpReq.enable();
                for(int lineNumber: breakPointLines) {
                    Location location = null;
                    try {
                        location = classType.locationsOfLine(lineNumber).get(0);
                    } catch (AbsentInformationException e) {
                        e.printStackTrace();
                    }
                    BreakpointRequest bpReq = erm.createBreakpointRequest(location);
                    bpReq.enable();
                }
            }
        });
    }

    /**
     * Displays the visible variables
     * @param event
     * @throws IncompatibleThreadStateException
     * @throws AbsentInformationException
     */
    public void displayVariables(LocatableEvent event) throws IncompatibleThreadStateException, AbsentInformationException {
        StackFrame stackFrame = event.thread().frame(0);
        if(stackFrame.location().toString().contains(debugClass.getName())) {
            Map<LocalVariable, Value> visibleVariables = stackFrame.getValues(stackFrame.visibleVariables());
            System.out.println("Variables at " +stackFrame.location().toString() +  " > ");
            for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                System.out.println(entry.getKey().name() + " = " + entry.getValue());
            }
        }
    }
    /**
     * Displays the visible variable
     * @param event
     * @throws IncompatibleThreadStateException
     * @throws AbsentInformationException
     */
    public void displayVariable(LocatableEvent event) throws IncompatibleThreadStateException, AbsentInformationException, NoSuchFieldException, IllegalAccessException {
        StackFrame stackFrame = event.thread().frame(0);
        if(stackFrame.location().toString().contains(debugClass.getName())) {
                Map<LocalVariable, Value> visibleVariables = stackFrame.getValues(stackFrame.visibleVariables());

            System.out.println("Variables at " +stackFrame.location().toString() +  " > ");
            List<Field> fields = event.location().declaringType().allFields();
            event.location().declaringType().getValues(fields);
            //TODO: NEED this
//            for (Field field : fields) {
//                Value value = event.location().declaringType().getValue(field);
//                System.out.println("Field "+ field.name() + " = " +value);
////                java.lang.reflect.Field sample = LocalVariableImpl.class.getDeclaredField("a");
////                java.lang.reflect.Field sample = LocalVariableImpl.class.getDeclaredField("scopeStart");
////                sample.setAccessible(true);
//            }

            for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                String name = entry.getKey().name();
                Value value = entry.getValue();
                if (value instanceof StringReferenceImpl) {
//                        System.out.println(entry.getValue().toString());
                    }
                 else if (value instanceof ObjectReference) {
                    ObjectReference ref = (ObjectReference) value;
                    try {
//                        List<Field> fields = ref.referenceType().allFields();
//                        List<Method> methods = ref.referenceType().allMethods();
//                        value = ref.invokeMethod(event.thread(), toString, Collections.emptyList(), 0);
                    } catch (Exception e) {
                        // Handle error
                    }
                }
                System.out.println(name + " : " + value);

            }
        }
    }

//    private static void

    /**
     * Enables step request for a break point
     * @param vm
     * @param event
     */
    public void enableStepRequest(VirtualMachine vm, BreakpointEvent event) {
        //enable step request for last break point
        if(event.location().toString().contains(debugClass.getName()+":"+breakPointLines[breakPointLines.length-1])) {
            StepRequest stepRequest = vm.eventRequestManager().createStepRequest(event.thread(), StepRequest.STEP_LINE, StepRequest.STEP_OVER);
            stepRequest.enable();
        }

    }
    public void enableStepINTORequest(VirtualMachine vm, BreakpointEvent event) {
        //enable step request for last break point
        if(event.location().toString().contains(debugClass.getName()+":"+breakPointLines[breakPointLines.length-1])) {
            StepRequest stepRequest = vm.eventRequestManager().createStepRequest(event.thread(), StepRequest.STEP_LINE, StepRequest.STEP_INTO);
            stepRequest.enable();
        }

    }

//    public void  getClass(String path) {
//        Class clazz;
//        clazz = new Class<>("C:\\Users\\Dell\\Desktop\\APR_Test\\data_test\\85713\\Account.class");
//    }

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().exec(
                new String[] {"javac", "-g", "-cp",
                        "\"C:\\Program Files\\Java\\jdk1.8.0_281\\lib\\tools.jar\"",
                        "C:\\Users\\Dell\\IdeaProjects\\HAPR\\src\\main\\java\\AST\\jdi\\*.java"
                });

        JDIExampleDebugger debuggerInstance = new JDIExampleDebugger();

        debuggerInstance.setDebugClass(TestRunner.class);
        int[] breakPoints = {76, 77, 80, 82, 83};
//        int[] breakPoints = {58};
        debuggerInstance.setBreakPointLines(breakPoints);
        VirtualMachine vm = null;
//        for (int i = 0; i < 2; i++) {
//
//        }
        try {
            vm = debuggerInstance.connectAndLaunchVM();
            debuggerInstance.enableClassPrepareRequest(vm);

            EventSet eventSet = null;
            while ((eventSet = vm.eventQueue().remove(100)) != null) {
                for (Event event : eventSet) {
                    System.out.println(event.toString());
                    if (event instanceof ClassPrepareEvent) {
                        debuggerInstance.setBreakPoints(vm, (ClassPrepareEvent)event);
//                        debuggerInstance.setMethodBreakPoints(vm, (ClassPrepareEvent) event, "test_getAt_method_in_board", "MyTest");
                     }

                    if (event instanceof BreakpointEvent) {
                        event.request().disable();
                        debuggerInstance.displayVariable((BreakpointEvent) event);
//                        debuggerInstance.enableStepINTORequest(vm, (BreakpointEvent) event);
//                        debuggerInstance.displayVariables((BreakpointEvent) event);
//                        debuggerInstance.enableStepRequest(vm, (BreakpointEvent)event);
                    }

                    if (event instanceof StepEvent) {
                        debuggerInstance.displayVariables((StepEvent) event);
                    }
                    vm.resume();
                }
            }
        } catch (VMDisconnectedException e) {
//            e.printStackTrace();
            System.out.println("Virtual Machine is disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            InputStreamReader reader = new InputStreamReader(vm.process().getInputStream());
            OutputStreamWriter writer = new OutputStreamWriter(System.out);
            char[] buf = new char[512];

            reader.read(buf);
            writer.write(buf);
            writer.flush();
        }
    }

    public static Object ReadObjectFromFile(String filepath) {

        try {

            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();

            System.out.println("The Object has been read from the file");
            objectIn.close();
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

//    public static void main(String[] args) {
//        VirtualMachine virtualMachine = null;
//        EventQueue evtQueue = virtualMachine.eventQueue();
//        EventSet evtSet = null;
//        while (!stopRequested)
//        {
//            try
//            {
//                evtSet = evtQueue.remove();
//                EventIterator evtIter = evtSet.eventIterator();
//                while (evtIter.hasNext())
//                {
//          // ...
//                    for (Event event : evtSet) {
//                        StackFrame stackFrame = (LocatableEvent) event.thread().frame(0);
//                        LocalVariable localVar = stackFrame.visibleVariableByName(varName);
//                        Value val = stackFrame.getValue(localVar);
//                    }
//                }
//            }
//            catch (Exception exc)
//            {
//          ...
//            }
//            finally
//            {
//                evtSet.resume();
//            }
//        }
//    }

//}