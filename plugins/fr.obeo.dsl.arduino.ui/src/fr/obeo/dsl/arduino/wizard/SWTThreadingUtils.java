package fr.obeo.dsl.arduino.wizard;

import org.eclipse.swt.widgets.Display;

public class SWTThreadingUtils
{

    public static void waitForAsyncExecsToFinish(Display display)
    {
        Object waitObj = new Object();

        display.asyncExec(new DummyRunnable(waitObj));
        synchronized (waitObj) 
        {
            try {
                waitObj.wait();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static class DummyRunnable implements Runnable
    {
        private Object waitObj;

        public DummyRunnable(Object waitObj)
        {
            this.waitObj = waitObj;
        }

        @Override
        public void run()
        {
            synchronized (waitObj)
            {
                waitObj.notify();
            }
        }    
    }

}