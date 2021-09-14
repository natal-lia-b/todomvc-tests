package todomvctest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class UserWorkflowTest {

    @Test
    public void todoCrudManagement() {
        Configuration.timeout = 6000;

        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(jsReturnsValue(
                "return (Object.keys(require.s.contexts._.defined).length === 39 &&" +
                        " $._data($('#clear-completed').get(0), 'events')" +
                        ".hasOwnProperty('click'))"));
        // Create
        $("#new-todo").append("a").pressEnter();
        $("#new-todo").append("b").pressEnter();
        $("#new-todo").append("c").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c"));

        // Edit
        $$("#todo-list>li").findBy(exactText("b")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit")
                .append(" edited").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("a", "b edited", "c"));

        // Delete
        $$("#todo-list>li").findBy(exactText("a")).hover().find(".destroy").click();

        // Complete
        $$("#todo-list>li").findBy(exactText("b edited")).find(".toggle").click();

        // Clear-completed
        $("#clear-completed").click();
        $$("#todo-list>li").shouldHave(exactTexts("c"));
    }
}
