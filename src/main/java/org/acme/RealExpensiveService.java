package org.acme;

/**
 * RealExpensiveService
 */
public class RealExpensiveService implements ExpensiveService {

    @Override
    public int calculate() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 100;
    }

    
    
}