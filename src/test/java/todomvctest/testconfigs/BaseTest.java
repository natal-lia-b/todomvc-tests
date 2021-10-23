package todomvctest.testconfigs;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Selenide.$$;

public class BaseTest {

    public final ElementsCollection todos = $$("#todo-list>li");

    {
        Configuration.baseUrl = System.getProperty(
                "selenide.baseUrl", "http://todomvc4tasj.herokuapp.com/");
        Configuration.timeout = Long.parseLong(System.getProperty(
                "selenide.timeout", "6000"));
        Configuration.fastSetValue = true;
    }
}
