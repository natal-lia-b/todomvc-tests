package todomvctest.testconfigs;

import com.codeborne.selenide.Configuration;

import static com.codeborne.selenide.Selenide.open;

public class BaseTest {

    {
        Configuration.fastSetValue = true;

        open("http://todomvc4tasj.herokuapp.com/");
    }
}
