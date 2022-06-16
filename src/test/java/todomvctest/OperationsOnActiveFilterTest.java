package todomvctest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import todomvctest.model.TodoMvc;
import todomvctest.testconfigs.BaseTest;

public class OperationsOnActiveFilterTest extends BaseTest {

    private TodoMvc app = new TodoMvc();

    @Test
    void addingTodos() {
        app.givenOpened();
        app.add("a");
        app.filterActive();

        app.add("b", "c");

        app.todosShouldBe("a", "b", "c");
        app.itemsLeftShouldBe(3);
    }

    @Test
    void editingTodoByFocusChange() {
        app.givenOpenedWith("a", "b", "c");
        app.filterActive();

        app.todo("b").editWith(Keys.TAB, "b edited");

        app.todosShouldBe("a", "b edited", "c");
        app.itemsLeftShouldBe(3);
    }

    @Test
    void completingTodo() {
        app.givenOpenedWith("a", "b", "c");
        app.filterActive();

        app.toggle("b");

        app.todosShouldBe("a", "c");
        app.itemsLeftShouldBe(2);
    }

    @Test
    void deletingLastActiveTodo() {
        app.givenOpenedWith("a", "b");
        app.toggle("a");
        app.filterActive();

        app.delete("b");

        app.todosShouldBeEmpty();
        app.itemsLeftShouldBe(0);
    }
}
