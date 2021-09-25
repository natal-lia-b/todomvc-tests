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

        Configuration.fastSetValue = true;

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
        open("http://todomvc4tasj.herokuapp.com/");

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
        $$("#todo-list>li").shouldHave(exactTexts(texts));
    }

    private void edit(String text, String newText) {
        $$("#todo-list>li").findBy(exactText(text)).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing"))
                .find(".edit").setValue(newText).pressEnter();
    }

    private void cancelEdit(String text, String newText) {
        $$("#todo-list>li").findBy(exactText(text)).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing"))
                .find(".edit").setValue(newText).pressEscape();
    }

    private void delete(String text) {
        $$("#todo-list>li").findBy(exactText(text)).hover()
                .find(".destroy").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void toggle(String text) {
        $$("#todo-list>li").findBy(exactText(text))
                .find(".toggle").click();
    }
}