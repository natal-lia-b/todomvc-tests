package todomvc4tasj;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selenide.*;

public class TodoMvc4TasjTests {

    {
        Configuration.timeout = 4000;

        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39"));
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39"));

        $("#new-todo").shouldBe(enabled).setValue("a").pressEnter();
        $("#new-todo").shouldBe(enabled).setValue("b").pressEnter();
        $("#new-todo").shouldBe(enabled).setValue("c").pressEnter();
    }

    @Test
    public void todoShouldCreated() {
        $$("#todo-list>li").shouldHave(CollectionCondition.exactTexts("a", "b", "c"));
    }

}
