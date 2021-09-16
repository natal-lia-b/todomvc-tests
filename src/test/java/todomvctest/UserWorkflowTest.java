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

    private void todosShouldBe(String... todosTexts) {
        todos.shouldHave(exactTexts(todosTexts));
    }

    private void cancelEdit(String todo, String newText) {
        todoSetValue(todo, newText).pressEscape();
    }

    private void delete(String todo) {
        findByText(todo).hover().find(".destroy").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void toggle(String todo) {
        findByText(todo).find(".toggle").click();
    }

    private SelenideElement todoSetValue(String todo, String newText) {
        findByText(todo).doubleClick();
        return todos.findBy(cssClass("editing")).find(".edit").setValue(newText);
    }

    private SelenideElement findByText(String todo) {
        return todos.findBy(exactText(todo));
    }

    private void edit(String todo, String newText) {
        todoSetValue(todo, newText).pressEnter();
    }

    private void add(String... todoTexts) {
        for(String todo : todoTexts) {
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

    private final ElementsCollection todos = $$("#todo-list>li");
}