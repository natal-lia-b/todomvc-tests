package todomvctest;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import todomvctest.testconfigs.BaseTest;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class TodoMvcBeforeEachTest extends BaseTest {

    @BeforeEach
    public void beforeTests() {
        openApp();

        clearData();
    }

    private void openApp() {
        open("/");

        String getObjectKeysLengthScript =
                "return (Object.keys(require.s.contexts._.defined).length === 39";
        String clearComplitedIsClickableScript =
                "$._data($('#clear-completed').get(0), 'events').hasOwnProperty('click'))";
        Selenide.Wait().until(jsReturnsValue(
                getObjectKeysLengthScript + " && " +
                        clearComplitedIsClickableScript));
    }


    public void delete(String text) {
        todos.findBy(exactText(text)).hover().find(".destroy").click();
    }

    private void clearData() {
        while (!todos.isEmpty()) {
            delete(todos.first().text());
        }
    }

}
