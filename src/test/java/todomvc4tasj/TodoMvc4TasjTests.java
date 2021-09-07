package todomvc4tasj;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class TodoMvc4TasjTests {

    @Test
    public void todoMvcSmokeTests() throws InterruptedException {
        Configuration.timeout = 6000;

        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(jsReturnsValue(
                "return (Object.keys(require.s.contexts._.defined).length === 39 &&" +
                        " $._data($('#clear-completed').get(0), 'events').hasOwnProperty('click'))"));

        $("#new-todo").shouldBe(enabled).append("a").pressEnter();
        $("#new-todo").shouldBe(enabled).append("b").pressEnter();
        $("#new-todo").shouldBe(enabled).append("c").pressEnter();

        // todoShouldCreated
        $$("#todo-list>li").shouldHave(exactTexts("a", "b", "c"));

        // todoShouldEdited
        $$("#todo-list>li").findBy(exactText("b")).doubleClick();
        $$("#todo-list>li").findBy(cssClass("editing")).find(".edit").append("D").pressEnter();
        $$("#todo-list>li").shouldHave(exactTexts("a", "bD", "c"));

        // todoShouldDeleted
        $$("#todo-list>li").findBy(exactText("a")).hover().find(".destroy").click();
        $$("#todo-list>li").shouldHave(exactTexts("bD", "c"));

        // todoShouldCompleted
        $$("#todo-list>li").findBy(exactText("c")).find(".toggle").click();
        $$("#todo-list>li").filterBy(cssClass("active")).shouldHave(exactTexts("bD"));
        $$("#todo-list>li").filterBy(cssClass("completed")).shouldHave(exactTexts("c"));

        // todoShouldReActive
        $$("#todo-list>li").findBy(exactText("c")).find(".toggle").click();
        $$("#todo-list>li").filterBy(cssClass("active")).shouldHave(exactTexts("bD", "c"));

        // shouldfilterActive
        $$("#todo-list>li").findBy(exactText("c")).find(".toggle").click();
        $$("#filters>li").findBy(exactText("Active")).click();
        $$("#todo-list>li").filterBy(cssClass("active")).shouldHave(exactTexts("bD"));

        // shouldfilterCompleted
        $$("#filters>li").findBy(exactText("Completed")).click();
        $$("#todo-list>li").filterBy(cssClass("completed")).shouldHave(exactTexts("c"));

        // shouldfilterAll
        $$("#filters>li").findBy(exactText("All")).click();
        $$("#todo-list>li").shouldHave(exactTexts("bD", "c"));

        // todoShouldClearComleted
        $("#clear-completed").click();
        $$("#todo-list>li").shouldHave(exactTexts("bD"));

        // todoShouldBeCounted
        $("#new-todo").shouldBe(enabled).append("f").pressEnter();
        $("#todo-count").shouldHave(text("2"));

        // todoShouldAllCompleted
        $("#toggle-all").click();
        $$("#todo-list>li").filterBy(cssClass("completed")).shouldHave(exactTexts("bD", "f"));

        // todoShouldAllActive
        $("#toggle-all").click();
        $$("#todo-list>li").filterBy(cssClass("active")).shouldHave(exactTexts("bD", "f"));
    }
}
