package todomvctest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import todomvctest.testconfigs.BaseTest;

public class OperationsOnCompletedFilterTest extends BaseTest {

    private TodoMvcPage todoMvc = new TodoMvcPage();

    @Test
    void editsTodoWithEnter() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("a");
        todoMvc.toggle("c");
        todoMvc.filterCompleted();

        todoMvc.editWith(Keys.ENTER, "a", "a edited");

        todoMvc.completedTodosShouldBe("a edited", "c");
        todoMvc.itemsLeftShouldBe(1);
    }

    @Test
    void editsTodoByFocusChange() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("a");
        todoMvc.filterCompleted();

        todoMvc.editWith(Keys.TAB, "a", "a edited");

        todoMvc.completedTodosShouldBe("a edited");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void cancelsEditingTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("c");
        todoMvc.filterCompleted();

        todoMvc.cancelEditing("c", "c edited");

        todoMvc.completedTodosShouldBe("c");
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void completeAllTodosWithSomeAlreadyCompleted() {
        todoMvc.givenAppOpenedWith("a", "b", "c", "d", "e");
        todoMvc.toggle("a");
        todoMvc.toggle("c");
        todoMvc.toggle("e");
        todoMvc.filterCompleted();

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBe("a", "b", "c", "d", "e");
        todoMvc.itemsLeftShouldBe(0);
    }

    @Test
    void unCompletesTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");
        todoMvc.filterCompleted();

        todoMvc.toggle("b");

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void unCompletesAllTodos() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggleAll();
        todoMvc.filterCompleted();

        todoMvc.toggleAll();

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(3);
    }

    @Test
    void deletesCompletedTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("a");
        todoMvc.toggle("c");
        todoMvc.filterCompleted();

        todoMvc.delete("a");

        todoMvc.completedTodosShouldBe("c");
        todoMvc.itemsLeftShouldBe(1);
    }

    @Test
    void deletesLastCompletedTodo() {
        todoMvc.givenAppOpenedWith("a");
        todoMvc.toggle("a");
        todoMvc.filterCompleted();

        todoMvc.delete("a");

        todoMvc.todosShouldBeEmpty();
    }

    @Test
    void clearCompletedTodo() {
        todoMvc.givenAppOpenedWith("a", "b", "c", "d", "e");
        todoMvc.toggle("a");
        todoMvc.toggle("c");
        todoMvc.toggle("e");
        todoMvc.filterCompleted();

        todoMvc.clearCompleted();

        todoMvc.completedTodosShouldBeEmpty();
        todoMvc.itemsLeftShouldBe(2);
    }

    @Test
    void clearCompletedAllTodos() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggleAll();
        todoMvc.filterCompleted();

        todoMvc.clearCompleted();

        todoMvc.todosShouldBeEmpty();
    }
}
