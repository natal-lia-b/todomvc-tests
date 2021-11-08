package todomvctest;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.Test;
import todomvctest.testconfigs.BaseTest;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class TodoMvcTest extends BaseTest {

    public final ElementsCollection todos = $$("#todo-list>li");

    @Test
    public void todoCrudManagement() {
        givenAppOpenedWith("a", "b", "c");
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
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        filterActive();
        todosShouldBe("a", "c");

        filterCompleted();
        todosShouldBe("b");

        filterAll();
        todosShouldBe("a", "b", "c");
    }

    @Test
    void tasksCountTest() {
        givenAppOpenedWith("a", "b", "c");
        tasksCountShouldBe(3);

        toggle("b");
        tasksCountShouldBe(2);

        toggleAll();
        tasksCountShouldBe(0);

        toggleAll();
        tasksCountShouldBe(3);
    }

    @Test
    void completeTasks() {
        givenAppOpenedWith("a", "b", "c");

        toggle("b");
        completedTasksShouldBe("b");

        toggleAll();
        completedTasksShouldBe("a", "b", "c");
    }

    @Test
    void unCompleteAllTasks() {
        givenAppOpenedWith("a", "b", "c");
        toggleAll();

        toggleAll();
        completedTasksShouldBeEmpty();
    }

    @Test
    void clearCompletedTest() {
        givenAppOpenedWith("a", "b", "c");
        clearCompletedShouldBe(not(visible));

        toggleAll();
        clearCompletedShouldBe(visible);

        clearCompleted();
        clearCompletedShouldBe(not(visible));
    }

    @Test
    void clearCompleteAllTasks() {
        givenAppOpenedWith("a", "b", "c");
        toggleAll();

        clearCompleted();
        todosShouldBeEmpty();
    }

    private void givenAppOpened() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserLocalStorage();
        }

        openApp();
    }

    private void givenAppOpenedWith(String... texts) {
        givenAppOpened();
        add(texts);
    }

    private static void openApp() {
        open("/");

        String areRequireJsContextsLoaded =
                "return (Object.keys(require.s.contexts._.defined).length === 39";
        String isClearCompletedClickable =
                "$._data($('#clear-completed').get(0), 'events').hasOwnProperty('click'))";
        Selenide.Wait().until(jsReturnsValue(
                areRequireJsContextsLoaded
                + " && " + isClearCompletedClickable
        ));
    }

    private void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    private void todosShouldBe(String... texts) {
        todos.filterBy(visible).shouldHave(exactTexts(texts));
    }

    private void todosShouldBeEmpty() {
        todos.shouldHave(CollectionCondition.size(0));
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

    private void clearCompletedShouldBe(Condition isVisible) {
        $("#clear-completed").shouldBe(isVisible);
    }

    private void completedTasksShouldBe(String... texts) {
        todos.filterBy(cssClass("completed")).shouldBe(exactTexts(texts));
    }

    private void completedTasksShouldBeEmpty(){
        todos.filterBy(cssClass("completed")).shouldBe(empty);
    }

    private void toggle(String text) {
        todos.findBy(exactText(text)).$(".toggle").click();
    }

    private void toggleAll() {
        $("#toggle-all").click();
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

    private void tasksCountShouldBe(int tasksCount) {
        $("#todo-count>strong").shouldBe(exactText(String.valueOf(tasksCount)));
    }
}