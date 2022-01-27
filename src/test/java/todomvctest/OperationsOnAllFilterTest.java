package todomvctest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import todomvctest.model.TodoMvc;
import todomvctest.testconfigs.BaseTest;

public class OperationsOnAllFilterTest extends BaseTest {

    private TodoMvc app = new TodoMvc();

    @Test
    void addingTodo() {
        app.givenOpened();

        /* WHEN nothing */

        app.todosShouldBeEmpty();

        /* WHEN */
        app.add("a");

        app.todosShouldBe("a");
        app.itemsLeftShouldBe(1);
    }

    @Test
    void addingTodos() {
        app.givenOpened();

        app.add("a", "b", "c");

        app.todosShouldBe("a", "b", "c");
        app.itemsLeftShouldBe(3);
    }

    @Test
    void editingTodoWithEnter() {
        app.givenOpenedWith("a", "b", "c");

        app.getLabel("b").editWith(Keys.ENTER, "b edited");

        app.todosShouldBe("a", "b edited", "c");
        app.itemsLeftShouldBe(3);
    }

    @Test
    void editingTodoByFocusChange() {
        app.givenOpenedWith("a", "b", "c");

        app.getLabel("b").editWith(Keys.TAB, "b edited");

        app.todosShouldBe("a", "b edited", "c");
        app.itemsLeftShouldBe(3);
    }

    @Test
    void cancelingEditingTodo() {
        app.givenOpenedWith("a", "b", "c");

        app.getLabel("b").cancelEditing("b edited");

        app.todosShouldBe("a", "b", "c");
        app.itemsLeftShouldBe(3);
    }

    @Test
    void completingTodo() {
        app.givenOpenedWith("a", "b", "c");

        app.toggle("b");

        app.completedTodosShouldBe("b");
        app.activeTodosShouldBe("a", "c");
        app.itemsLeftShouldBe(2);
    }

    @Test
    void completingAllTodos() {
        app.givenOpenedWith("a", "b", "c");

        app.toggleAll();

        app.completedTodosShouldBe("a", "b", "c");
        app.activeTodosShouldBeEmpty();
        app.itemsLeftShouldBe(0);
    }

    @Test
    void completingAllTodosWithSomeAlreadyCompleted() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("b");

        app.toggleAll();

        app.completedTodosShouldBe("a", "b", "c");
        app.activeTodosShouldBeEmpty();
        app.itemsLeftShouldBe(0);
    }

    @Test
    void unCompletingTodo() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("b");

        app.toggle("b");

        app.completedTodosShouldBeEmpty();
        app.activeTodosShouldBe("a", "b", "c");
        app.itemsLeftShouldBe(3);
    }

    @Test
    void unCompletingAllTodos() {
        app.givenOpenedWith("a", "b", "c");
        app.toggleAll();

        app.toggleAll();

        app.completedTodosShouldBeEmpty();
        app.activeTodosShouldBe("a", "b", "c");
        app.itemsLeftShouldBe(3);
    }

    @Test
    void deletingTodo() {
        app.givenOpenedWith("a", "b", "c");

        app.delete("b");

        app.todosShouldBe("a", "c");
        app.itemsLeftShouldBe(2);
    }

    @Test
    void deletingTodoByEditingToBlank() {
        app.givenOpenedWith("a", "b", "c");

        app.getLabel("b").editWith(Keys.ENTER, " ");

        app.todosShouldBe("a", "c");
        app.itemsLeftShouldBe(2);
    }

    @Test
    void deletingCompletedTodo() {
        app.givenOpenedWith("a", "b", "c");
        app.toggle("b");

        app.delete("b");

        app.todosShouldBe("a", "c");
        app.itemsLeftShouldBe(2);
    }

    @Test
    void deletingLastTodo() {
        app.givenOpenedWith("a");

        app.delete("a");

        app.todosShouldBeEmpty();
    }

    @Test
    void clearCompletedAllTodos() {
        app.givenOpenedWith("a", "b", "c");
        app.toggleAll();

        app.clearCompleted();

        app.todosShouldBeEmpty();
    }
}
