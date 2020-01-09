package org.acme;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.test.Mock;

@ApplicationScoped
@Mock
public class MockExpensiveService implements ExpensiveService{

    @Override
    public int calculate() {
        // TODO Auto-generated method stub
        return 20;
    }

    
    
}