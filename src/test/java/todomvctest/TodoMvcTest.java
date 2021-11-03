package todomvctest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Test;
import todomvctest.testconfigs.BaseTest;

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