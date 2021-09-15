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
        todoList_li.shouldHave(exactTexts("a", "b", "c"));

        editAndComplete( "b", " edited");
        todoList_li.shouldHave(exactTexts("a", "c"));

        cancelEditAndDelete("c", "to be canceled");
        todoList_li.shouldHave(exactTexts("a"));
    }

    private void cancelEditAndDelete(String text, String toBeCanceledText) {
        todoList_li.findBy(exactText(text)).doubleClick();
        todoList_li.findBy(cssClass("editing"))
                .find(".edit").append(toBeCanceledText).pressEscape();

        todoList_li.findBy(exactText(text)).hover().find(".destroy").click();
    }

    private void editAndComplete(String task, String addedText) {
        todoList_li.findBy(exactText(task)).doubleClick();
        todoList_li.findBy(cssClass("editing")).find(".edit")
                .append(addedText).pressEnter();

        todoList_li.findBy(exactText(task + addedText)).find(".toggle").click();
        $("#clear-completed").click();
    }

    private void add(String... tasks) {
        for (String task: tasks) {
            newTodo.append(task).pressEnter();
        }
    }

    private final SelenideElement newTodo = $("#new-todo");
    private final ElementsCollection todoList_li = $$("#todo-list>li");
}
