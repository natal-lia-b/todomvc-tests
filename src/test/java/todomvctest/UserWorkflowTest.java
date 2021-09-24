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

    private SelenideElement todoBy(Condition condition) {
        return todos.findBy(condition);
    }

    private SelenideElement findByText(String text) {
        return todoBy(exactText(text));
    }

    private SelenideElement startEditing(String text, String textToAdd) {
        findByText(text).doubleClick();
        return todoBy(cssClass("editing")).find(".edit")
                .append(textToAdd);
    }

    private void todosShouldBe(String... texts) {
        todos.shouldHave(exactTexts(texts));
    }

    private void add(String... texts) {
        for (String text: texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    private final ElementsCollection todos = $$("#todo-list>li");
}