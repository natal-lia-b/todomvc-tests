package todomvc4tasj;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selenide.*;

public class TodoMvc4TasjTests {

    @Test
    public void allTests() {
        Configuration.timeout = 6000;

        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return Object.keys(require.s.contexts._.defined).length === 39"));

        $("#new-todo").shouldBe(enabled).setValue("a").pressEnter();
        $("#new-todo").shouldBe(enabled).setValue("b").pressEnter();
        $("#new-todo").shouldBe(enabled).setValue("c").pressEnter();

        // todoShouldCreated
        $$("#todo-list>li").shouldHave(CollectionCondition.exactTexts("a", "b", "c"));

        // todoShouldEdited
        $("#todo-list>li:nth-of-type(2)").doubleClick();
        Selenide.executeJavaScript("$(\"#todo-list>li:nth-of-type(2) .edit\").select()");
        $("#todo-list>li:nth-of-type(2) .edit").sendKeys("D" + Keys.ENTER);

        $$("#todo-list>li").shouldHave(CollectionCondition.exactTexts("a", "D", "c"));
    }
}
