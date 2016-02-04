/**
 * MIT License

Copyright (c) 2015  Rob Terpilowski

Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
and associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.limituptrading.broker;

import com.limituptrading.broker.order.TradeDirection;
import com.limituptrading.data.StockTicker;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Rob Terpilowski
 */
public class BrokerSerializationTest {

    public BrokerSerializationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    
    @Test
    public void testBatchOrder() throws Exception {
        Order order = new Order();
        
        BatchOrder batchOrder = new BatchOrder();
        batchOrder.addOrder(123, "ABC", order);
        
        test( batchOrder );
        
    }
    
    @Test
    public void testOrder() throws Exception {
        Order order = new Order();
        test( order );
    }
    
    @Test
    public void testPortfolioPosition() throws Exception {
        PortfolioPosition position = new PortfolioPosition(new StockTicker("ABC"), BigDecimal.ZERO);
        test( position );
    }
    
    @Test
    public void testBrokerError() throws Exception {
        BrokerError error = new BrokerError("123");
        
        test( error );
    }
    
    @Test
    public void testPosition() throws Exception {
        Transaction t1 = new Transaction();
        t1.setTicker( new StockTicker("ABC"));
        Transaction t2 = new Transaction();
        t2.setTicker( new StockTicker("123"));
        Position position = new Position();
        position.setCloseTransaction(t2);
        position.setOpenTransaction(t1);
        position.setStatus(Position.Status.OPEN);
        
        test( position );
    }
    
    @Test
    public void testTransaction() throws Exception {
        Transaction t = new Transaction();
        t.setCommission(1.0);
        t.setPositionId("123");
        t.setTicker(new StockTicker("123"));
        t.setTradeDirection(TradeDirection.BUY);
        t.setTransactionDate(new Date());
        t.setTransactionPrice(100.0);
        t.setTransactionSize(100);
        
        test(t);
        
    }

    public void test(Object object) throws Exception {
        byte[] serialized = serialize(object);
        assertTrue(serialized.length > 0);

        Object object2 = deserialize(object.getClass(), serialized);

        assertEquals(object, object2);
    }

    protected byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(output);

        objectOut.writeObject(object);
        return output.toByteArray();

    }

    protected <T> T deserialize(Class<T> clazz, byte[] bytes) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ObjectInputStream objectIn = new ObjectInputStream(input);
        return (T) objectIn.readObject();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
