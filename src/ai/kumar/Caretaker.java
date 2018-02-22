
package ai.kumar;

import org.eclipse.jetty.util.log.Log;

import ai.kumar.tools.TimeoutMatcher;


/**
 * The caretaker class is a concurrent thread which does peer-to-peer operations
 * and data transmission asynchronously.
 */
public class Caretaker extends Thread {
    
    private boolean shallRun = true;
    
    public  final static long startupTime = System.currentTimeMillis();
    
    /**
     * ask the thread to shut down
     */
    public void shutdown() {
        this.shallRun = false;
        this.interrupt();
        Log.getLog().info("catched caretaker termination signal");
    }
    
    @Override
    public void run() {
        
        boolean busy = false;
        // work loop
        beat: while (this.shallRun) try {
            
            
            // sleep a bit to prevent that the DoS limit fires at backend server
            try {Thread.sleep(busy ? 1000 : 5000);} catch (InterruptedException e) {}
            TimeoutMatcher.terminateAll();
            
            if (!this.shallRun) break beat;
            busy = false;
            
        } catch (Throwable e) {
            Log.getLog().warn("CARETAKER THREAD", e);
        }

        Log.getLog().info("caretaker terminated");
    }
    
}
