package todomvctest;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import todomvctest.testconfigs.BaseTest;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class TodoMvcTest extends BaseTest {

    private final ElementsCollection todos = $$("#todo-list>li");
    private final SelenideElement clearCompleted = $("#clear-completed");
    private final String completedClass = "completed";
    private final String activeClass = "active";

    @Test
    public void todoCrudManagement() {
        givenAppOpenedWith("a", "b", "c");
        todosShouldBe("a", "b", "c");

        editWith(Keys.ENTER, "b", "b edited");

        toggle("b edited");
        clearCompleted();
        todosShouldBe("a", "c");

        cancelEditing("c", "c to be canceled");

        delete("c");
        todosShouldBe("a");
    }

    @Test
    void filtersTodos() {
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
    void addsTodo() {
        givenAppOpened();

        /* WHEN nothing */

        todosShouldBeEmpty();

        /* WHEN */
        add("a");

        todosShouldBe("a");
        itemsLeftShouldBe(1);
    }

    @Test
    void addsTodos() {
        givenAppOpened();

        add("a", "b", "c");

        todosShouldBe("a", "b", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    void editsTodoWithEnter() {
        givenAppOpenedWith("a", "b", "c");

        editWith(Keys.ENTER, "b", "b edited");

        todosShouldBe("a", "b edited", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    void editsTodoByFocusChange() {
        givenAppOpenedWith("a", "b", "c");

        editWith(Keys.TAB, "b", "b edited");

        todosShouldBe("a", "b edited", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    void cancelsEditingTodo() {
        givenAppOpenedWith("a", "b", "c");

        cancelEditing("b", "b edited");

        todosShouldBe("a", "b", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    void completesTodo() {
        givenAppOpenedWith("a", "b", "c");

        toggle("b");

        todosOfClassShouldBe(completedClass, "b");
        todosOfClassShouldBe(activeClass, "a", "c");
        itemsLeftShouldBe(2);
    }

    @Test
    void completesAllTodos() {
        givenAppOpenedWith("a", "b", "c");

        toggleAll();

        todosOfClassShouldBe(completedClass, "a", "b", "c");
        todosOfClassShouldBeEmpty(activeClass);
        itemsLeftShouldBe(0);
    }

    @Test
    void completeAllTodosWithSomeAlreadyCompleted() {
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        toggleAll();

        todosOfClassShouldBe(completedClass, "a", "b", "c");
        todosOfClassShouldBeEmpty(activeClass);
        itemsLeftShouldBe(0);
    }

    @Test
    void unCompletesTodo() {
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        toggle("b");

        todosOfClassShouldBeEmpty(completedClass);
        todosOfClassShouldBe(activeClass, "a", "b", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    void unCompletesAllTodos() {
        givenAppOpenedWith("a", "b", "c");
        toggleAll();

        toggleAll();

        todosOfClassShouldBeEmpty(completedClass);
        todosOfClassShouldBe(activeClass, "a", "b", "c");
        itemsLeftShouldBe(3);
    }

    @Test
    void deletesTodo() {
        givenAppOpenedWith("a", "b", "c");

        delete("b");

        todosShouldBe("a", "c");
        itemsLeftShouldBe(2);
    }

    @Test
    void deletesTodoByEditingToBlank() {
        givenAppOpenedWith("a", "b", "c");

        editWith(Keys.ENTER, "b", " ");

        todosShouldBe("a", "c");
        itemsLeftShouldBe(2);
    }

    @Test
    void deletesCompletedTodo() {
        givenAppOpenedWith("a", "b", "c");
        toggle("b");

        delete("b");

        todosShouldBe("a", "c");
        itemsLeftShouldBe(2);
    }

    @Test
    void deletesLastTodo() {
        givenAppOpenedWith("a");

        delete("a");

        todosShouldBeEmpty();
    }

    @Test
    void clearCompletedTodo() {
        givenAppOpenedWith("a", "b", "c", "d", "e");
        toggle("a");
        toggle("c");
        toggle("e");

        clearCompleted();

        todosShouldBe("b", "d");
        todosOfClassShouldBeEmpty(completedClass);
        todosOfClassShouldBe(activeClass, "b", "d");
        itemsLeftShouldBe(2);
    }

    @Test
    void clearCompletedAllTodos() {
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
        todos.shouldHave(size(0));
    }

    private SelenideElement startEditing(String text, String newText) {
        todos.findBy(exactText(text)).doubleClick();
        return todos.findBy(cssClass("editing"))
                .find(".edit").setValue(newText);
    }

    private void editWith(CharSequence key, String text, String newText) {
        startEditing(text, newText).sendKeys(key);
    }

    private void cancelEditing(String text, String newText) {
        editWith(Keys.ESCAPE, text, newText);
    }

    private void delete(String text) {
        todos.findBy(exactText(text)).hover().find(".destroy").click();
    }

    private void clearCompleted() {
        clearCompleted.click();

        clearCompletedShouldBe(hidden);
    }

    private void clearCompletedShouldBe(Condition condition) {
        clearCompleted.shouldBe(condition);
    }

    private void todosOfClassShouldBe(String cssClass, String... texts) {
        todos.filterBy(cssClass(cssClass)).shouldBe(exactTexts(texts));
    }

    private void todosOfClassShouldBeEmpty(String cssClass){
        todos.filterBy(cssClass(cssClass)).shouldBe(empty);
    }

    private void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();
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

    private void itemsLeftShouldBe(int number) {
        $("#todo-count>strong").shouldBe(exactText(String.valueOf(number)));
    }
}