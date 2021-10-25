package todomvctest;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.BeforeEach;
import todomvctest.testconfigs.BaseTest;

import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.support.ui.ExpectedConditions.jsReturnsValue;

public class TodoMvcBeforeEachTest extends BaseTest {

    @BeforeEach
    public void setupApp() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserLocalStorage();
        }

        openApp();
    }

    private static void openApp() {
        open("/");

        String getObjectKeysLengthScript =
                "return (Object.keys(require.s.contexts._.defined).length === 39";
        String clearComplitedIsClickableScript =
                "$._data($('#clear-completed').get(0), 'events').hasOwnProperty('click'))";
        Selenide.Wait().until(jsReturnsValue(
                getObjectKeysLengthScript + " && " +
                        clearComplitedIsClickableScript));
    }
}
