package todomvctest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMvcTest extends TodoMvcBeforeEachTest {

    public final ElementsCollection todos = $$("#todo-list>li");

    @Test
    public void todoCrudManagement() {
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

    @Test
    void filtersTasks() {
        add("a", "b", "c");

        toggle("b");

        filterActive();
        todosShouldBe("a", "c");

        filterCompleted();
        todosShouldBe("b");

        filterAll();
        todosShouldBe("a", "b", "c");
    }

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    private void todosShouldBe(String... texts) {
        todos.filterBy(visible).shouldHave(exactTexts(texts));
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

    public void delete(String text) {
        todos.findBy(exactText(text)).hover().find(".destroy").click();
    }

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();
    }

    private void filterAll() {
        $("[href='#/']").click();
    }

    private void filterActive() {
        $("[href='#/active']").click();
    }

    private void filterCompleted() {
        $("[href='#/completed']").click();
    }
}