package todomvctest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class UserWorkflowTest extends TodoMvcBeforeEachTest {

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

        todosByFilterShouldHaveTexts("Active", "a", "c");
        todosByFilterShouldHaveTexts("Completed", "b");
    }

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    private void todosShouldBe(String... texts) {
        todos.shouldHave(exactTexts(texts));
    }

    private void todosByFilterShouldBe(String filter, String... texts) {
        todos.filterBy(Condition.cssClass(filter.toLowerCase()))
                .shouldHave(exactTexts(texts));
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

    private void clearCompleted() {
        $("#clear-completed").click();
    }

    private void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();
    }

    private void clickFilter(String filter) {
        $$("#filters>li").findBy(exactText(filter)).click();
    }

    private void todosByFilterShouldHaveTexts(String filter, String... texts) {
        clickFilter(filter);
        todosByFilterShouldBe(filter, texts);
    }
}