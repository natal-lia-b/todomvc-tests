package todomvctest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
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

        add("a", "b", "c");
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c"));

        editCompleteAndClear("b", " edited");
        $$("#todo-list>li").shouldHave(exactTexts("a", "c"));

        cancelEditAndDelete("c", "to be canceled");
        $$("#todo-list>li").shouldHave(exactTexts("a"));
    }

    private void cancelEditAndDelete(String todo, String text) {
        elements("#todo-list>li").findBy(exactText(todo)).doubleClick();
        elements("#todo-list>li").findBy(cssClass("editing"))
                .find(".editCompleteAndClear").append(text).pressEscape();

        $$("#todo-list>li").findBy(exactText(todo)).hover().find(".destroy").click();
    }

    private void editCompleteAndClear(String todo, String text) {
        $$("#todo-list>li").findBy(exactText(todo)).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".editCompleteAndClear")
                .append(text).pressEnter();

        $$("#todo-list>li").findBy(exactText(todo + text)).find(".toggle").click();
        $("#clear-completed").click();
    }

    private void add(String... texts) {
        for(String text: texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }
}