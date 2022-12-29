package com.campusdiscovery.app;
import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    @Test
    public void nullNameCheck() {
        SignUp value = new SignUp();
        Assert.assertNotNull("Username should not be null", value.nameInput);
    }
    @Test
    public void emptyNameCheck() {
        SignUp value = new SignUp();
        Assert.assertEquals("", value.nameInput);
    }
    @Test
    public void nullUserIDCheck() {
        SignUp value = new SignUp();
        Assert.assertNotNull("UserID should not be null", value.passwordInput);
    }
    @Test
    public void emptyUserIDCheck() {
        SignUp value = new SignUp();
        Assert.assertEquals("", value.passwordInput);
    }
    @Test
    public void nullPassCheck() {
        SignUp value = new SignUp();
        Assert.assertNotNull("Password should not be null", value.userIdInput);
    }
    @Test
    public void emptyPassCheck() {
        SignUp value = new SignUp();
        Assert.assertEquals("", value.userIdInput);
    }



    @Test
    public void nullEventNameCheck() {
        CreateEvent value = new CreateEvent();
        Assert.assertNotNull("Event Name should not be null", value.eventNameInput);
    }
    @Test
    public void emptyEventNameCheck() {
        CreateEvent value = new CreateEvent();
        Assert.assertEquals("", value.eventNameInput);
    }
    @Test
    public void nullEventIDCheck() {
        CreateEvent value = new CreateEvent();
        Assert.assertNotNull("Event ID should not be null", value.eventIDInput);
    }
    @Test
    public void emptyEventIDCheck() {
        CreateEvent value = new CreateEvent();
        Assert.assertEquals("", value.eventIDInput);
    }
    @Test
    public void nullEventDescCheck() {
        CreateEvent value = new CreateEvent();
        Assert.assertNotNull("Event Description should not be null", value.eventDescInput);
    }
    @Test
    public void emptyEventDescCheck() {
        CreateEvent value = new CreateEvent();
        Assert.assertEquals("", value.eventDescInput);
    }
    @Test
    public void nullEventCapacityCheck() {
        CreateEvent value = new CreateEvent();
        Assert.assertNotNull("Event Capacity should not be null", value.eventCapacity);
    }
}