import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.config.spring.SpringRegistry;
import org.mule.module.client.MuleClient;
import org.mule.tck.functional.EventCallback;
import org.mule.tck.functional.FunctionalTestComponent;
import org.mule.tck.junit4.FunctionalTestCase;
import org.springframework.context.ApplicationContext;

public class MyFunctionalTest extends FunctionalTestCase {

	protected String getConfigResources()
	{
	    return "mule-conf.xml,test-demo.xml";
	}
	
	@Test
	public void testEventCallback() throws Exception
	{
		final CountDownLatch latch = new CountDownLatch(1);
		
		EventCallback callback = new EventCallback()
	    {
	        public void eventReceived(MuleEventContext context, Object component)
	            throws Exception
	        {
	            System.out.println("Thanks for calling me back");
	            latch.countDown();
	        }
	    };
	  
	    ApplicationContext ac =
	(ApplicationContext)muleContext.getRegistry().lookupObject(SpringRegistry.SPRING_APPLICATION_CONTEXT);
	    FunctionalTestComponent testComponent = (FunctionalTestComponent) ac.getBean("FTC");
	    testComponent.setEventCallback(callback);
	        
	    MuleClient client = new MuleClient(muleContext);
	          
	    client.send("vm://test2", "foo" ,null);
	    Assert.assertTrue(latch.await(5, TimeUnit.SECONDS));
	 }
	
	@Test
	public void testSend() throws Exception
	{
	    MuleClient client = new MuleClient(muleContext);
	    String payload = "foo";
	    Map<String, Object> properties = null;
	    MuleMessage result = client.send("vm://test", payload, properties);
	    Assert.assertEquals("foo Received", result.getPayloadAsString());
	}
	
	@Test
	public void testReceiveFromOut() throws Exception {
		MuleClient client = new MuleClient(muleContext);
	    String payload = "{}";
	    Map<String, Object> properties = null;
	    client.send("vm://in", payload, properties);
		MuleMessage dispatched = client.request("vm://out", 5000L);
	    Assert.assertNotNull(dispatched);
	    Assert.assertTrue(dispatched.getPayloadAsString().contains("ABCD"));
	}
	
}
