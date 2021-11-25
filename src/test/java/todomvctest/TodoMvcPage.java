package todomvctest;

import com.codeborne.selenide.*;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class TodoMvcPage {

    protected final ElementsCollection todos = $$("#todo-list>li");
    protected final SelenideElement clearCompleted = $("#clear-completed");

    protected final String completed = "completed";
    protected final String active = "active";

    protected void givenAppOpened() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserLocalStorage();
        }

        openApp();
    }

    protected void givenAppOpenedWith(String... texts) {
        givenAppOpened();
        add(texts);
    }

    protected static void openApp() {
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

    protected void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    protected void todosShouldBe(String... texts) {
        todos.filterBy(visible).shouldHave(exactTexts(texts));
    }

    protected void todosShouldBeEmpty() {
        todos.shouldHave(size(0));
    }

    protected SelenideElement startEditing(String text, String newText) {
        todos.findBy(exactText(text)).doubleClick();
        return todos.findBy(cssClass("editing"))
                .find(".edit").setValue(newText);
    }

    protected void editWith(CharSequence key, String text, String newText) {
        startEditing(text, newText).sendKeys(key);
    }

    protected void edit(String text, String newText) {
        editWith(Keys.ENTER, text, newText);
    }

    protected void cancelEditing(String text, String newText) {
        editWith(Keys.ESCAPE, text, newText);
    }

    protected void delete(String text) {
        todos.findBy(exactText(text)).hover().find(".destroy").click();
    }

    protected void clearCompleted() {
        clearCompleted.click();

        clearCompletedShouldBe(hidden);
    }

    protected void clearCompletedShouldBe(Condition condition) {
        clearCompleted.shouldBe(condition);
    }

    protected void activeTodosShouldBe(String... texts) {
        todos.filterBy(cssClass(active)).shouldBe(exactTexts(texts));
    }

    protected void activeTodosShouldBeEmpty(){
        todos.filterBy(cssClass(active)).filterBy(visible).shouldBe(empty);
    }

    protected void completedTodosShouldBe(String... texts) {
        todos.filterBy(cssClass(completed)).shouldBe(exactTexts(texts));
    }

    protected void completedTodosShouldBeEmpty(){
        todos.filterBy(cssClass(completed)).filterBy(visible).shouldBe(empty);
    }

    protected void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();
    }

    protected void toggleAll() {
        $("#toggle-all").click();
    }

    protected void filterAll() {
        $("[href='#/']").click();
    }

    protected void filterActive() {
        $("[href='#/active']").click();
    }

    protected void filterCompleted() {
        $("[href='#/completed']").click();
    }

    protected void itemsLeftShouldBe(int number) {
        $("#todo-count>strong").shouldBe(exactText(String.valueOf(number)));
    }

}
