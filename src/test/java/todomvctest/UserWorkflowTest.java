package todomvctest;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.Test;
import todomvctest.testconfigs.BaseTest;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class UserWorkflowTest extends BaseTest {

    @Test
    public void todoCrudManagement() {
        openApp();

        add("a", "b", "c");
        todosShouldBe("a", "b", "c");

        edit("b", "b edited");

        toggle("b edited");
        clearCompleted();
        todosShouldBe("a", "c");

        cancelEdit("c", "c to be canceled");

        delete("c");
        todosShouldBe("a");
    }

    private void openApp() {
        String getObjectKeysLengthScript =
                "return (Object.keys(require.s.contexts._.defined).length === 39";
        String clearComplitedIsClickableScript =
                "$._data($('#clear-completed').get(0), 'events').hasOwnProperty('click'))";
        Selenide.Wait().until(jsReturnsValue(
                getObjectKeysLengthScript + " && " +
                        clearComplitedIsClickableScript));
    }

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    private void todosShouldBe(String... texts) {
        todos.shouldHave(exactTexts(texts));
    }

    private SelenideElement startEditing(String text, String newText) {
        todos.findBy(exactText(text)).doubleClick();
        return todos.findBy(cssClass("editing"))
                .find(".edit").setValue(newText);
    }

    private void edit(String text, String newText) {
        startEditing(text, newText).pressEnter();
    }

    private void cancelEdit(String text, String newText) {
        startEditing(text, newText).pressEscape();
    }

    private void delete(String text) {
        todos.findBy(exactText(text)).hover().find(".destroy").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();
    }

    private final ElementsCollection todos = $$("#todo-list>li");
}