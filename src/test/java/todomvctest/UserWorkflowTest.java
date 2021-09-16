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
        Configuration.fastSetValue = true;

        openUrl();

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

    private void todosShouldBe(String... todos) {
        $$("#todo-list>li").shouldHave(exactTexts(todos));
    }

    private void cancelEdit(String todo, String newText) {
        elements("#todo-list>li").findBy(exactText(todo)).doubleClick();
        elements("#todo-list>li").findBy(cssClass("editing"))
                .find(".edit").setValue(newText).pressEscape();
    }

    private void delete(String todo) {
        $$("#todo-list>li").findBy(exactText(todo)).hover().find(".destroy").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void toggle(String todo) {
        $$("#todo-list>li").findBy(exactText(todo)).find(".toggle").click();
    }

    private void edit(String todo, String newText) {
        $$("#todo-list>li").findBy(exactText(todo)).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit")
                .setValue(newText).pressEnter();
    }

    private void add(String... todos) {
        for(String todo : todos) {
            $("#new-todo").append(todo).pressEnter();
        }
    }

    private void openUrl() {
        open("http://todomvc4tasj.herokuapp.com/");

        String getObjectKeysLengthScript =
                "return (Object.keys(require.s.contexts._.defined).length === 39";
        String clearComplitedIsClickableScript =
                "$._data($('#clear-completed').get(0), 'events').hasOwnProperty('click'))";
        Selenide.Wait().until(jsReturnsValue(
                getObjectKeysLengthScript + " && " +
                        clearComplitedIsClickableScript));
    }
}