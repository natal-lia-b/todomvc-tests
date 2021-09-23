package todomvctest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
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
        todosShouldBe("a", "b", "c");

        editWithText("b", " edited").pressEnter();
        findByText("b edited").find(".toggle").click();
        $("#clear-completed").click();
        todosShouldBe("a", "c");

        editWithText("c", "to be canceled").pressEscape();
        findByText("c").hover().find(".destroy").click();
        todosShouldBe("a");
    }

    private SelenideElement findByText(String text) {
        return todos.findBy(exactText(text));
    }

    private SelenideElement editWithText(String task, String text) {
        findByText(task).doubleClick();
        return todos.findBy(cssClass("editing")).find(".edit")
                .append(text);
    }

    private void todosShouldBe(String... tasks) {
        todos.shouldHave(exactTexts(tasks));
    }

    private void add(String... tasks) {
        for (String task: tasks) {
            $("#new-todo").append(task).pressEnter();
        }
    }

    private final ElementsCollection todos = $$("#todo-list>li");
}