package todomvctest.testconfigs;

import com.codeborne.selenide.Configuration;

public class BaseTest {

    {
        Configuration.baseUrl = System.getProperty(
                "selenide.baseUrl","http://todomvc4tasj.herokuapp.com/");
        Configuration.timeout = Long.parseLong(System.getProperty(
          "selenide.timeout", "6000"));
        Configuration.fastSetValue = true;
    }
}
