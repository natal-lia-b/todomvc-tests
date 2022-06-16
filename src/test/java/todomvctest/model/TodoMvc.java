package todomvctest.model;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class TodoMvc {

    public final ElementsCollection todos = $$("#todo-list>li");
    public final SelenideElement clearCompleted = $("#clear-completed");

    public final String completed = "completed";
    public final String active = "active";

    public void givenOpened() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserLocalStorage();
        }

        openApp();
    }

    public void givenOpenedWith(String... texts) {
        givenOpened();
        add(texts);
    }

    public static void openApp() {
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

    public Label todo(String text) {
        return new Label(findBy(text));
    }

    public SelenideElement findBy(String text) {
        return $$("#todo-list>li").findBy(exactText(text));
    }

    public void add(String... texts) {
        for (String text : texts) {
            $("#new-todo").append(text).pressEnter();
        }
    }

    public void todosShouldBe(String... texts) {
        todos.filterBy(visible).shouldHave(exactTexts(texts));
    }

    public void todosShouldBeEmpty() {
        todos.filterBy(visible).shouldHave(size(0));
    }

    public void delete(String text) {
        todos.findBy(exactText(text)).hover().find(".destroy").click();
    }

    public void clearCompleted() {
        clearCompleted.click();

        clearCompletedShouldBe(hidden);
    }

    public void clearCompletedShouldBe(Condition condition) {
        clearCompleted.shouldBe(condition);
    }

    public void activeTodosShouldBe(String... texts) {
        todos.filterBy(cssClass(active)).shouldBe(exactTexts(texts));
    }

    public void activeTodosShouldBeEmpty(){
        todos.filterBy(cssClass(active)).filterBy(visible).shouldBe(empty);
    }

    public void completedTodosShouldBe(String... texts) {
        todos.filterBy(cssClass(completed)).shouldBe(exactTexts(texts));
    }

    public void completedTodosShouldBeEmpty(){
        todos.filterBy(cssClass(completed)).filterBy(visible).shouldBe(empty);
    }

    public void toggle(String text) {
        todos.findBy(exactText(text)).find(".toggle").click();
    }

    public void toggleAll() {
        $("#toggle-all").click();
    }

    public void filterAll() {
        $("[href='#/']").click();
    }

    public void filterActive() {
        $("[href='#/active']").click();
    }

    public void filterCompleted() {
        $("[href='#/completed']").click();
    }

    public void itemsLeftShouldBe(int number) {
        $("#todo-count>strong").shouldBe(exactText(String.valueOf(number)));
    }

}
