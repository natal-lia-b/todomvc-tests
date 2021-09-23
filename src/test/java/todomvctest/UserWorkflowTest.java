package todomvctest;

import com.codeborne.selenide.*;
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

        startEditing("b", " edited").pressEnter();
        findByText("b edited").find(".toggle").click();
        $("#clear-completed").click();
        todosShouldBe("a", "c");

        startEditing("c", "to be canceled").pressEscape();
        findByText("c").hover().find(".destroy").click();
        todosShouldBe("a");
    }

    private SelenideElement findByCondition(Condition condition) {
        return todos.findBy(condition);
    }

    private SelenideElement findByText(String todoText) {
        return findByCondition(exactText(todoText));
    }

    private SelenideElement startEditing(String todoText, String textToAdd) {
        findByText(todoText).doubleClick();
        return findByCondition(cssClass("editing")).find(".edit")
                .append(textToAdd);
    }

    private void todosShouldBe(String... todoTexts) {
        todos.shouldHave(exactTexts(todoTexts));
    }

    private void add(String... todoTexts) {
        for (String task: todoTexts) {
            $("#new-todo").append(task).pressEnter();
        }
    }

    private final ElementsCollection todos = $$("#todo-list>li");
}