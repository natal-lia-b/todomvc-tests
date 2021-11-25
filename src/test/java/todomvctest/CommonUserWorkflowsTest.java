package todomvctest;

import org.junit.jupiter.api.Test;
import todomvctest.testconfigs.BaseTest;

public class CommonUserWorkflowsTest extends BaseTest {

    private TodoMvcPage todoMvc = new TodoMvcPage();

    @Test
    public void todoCrudManagement() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.todosShouldBe("a", "b", "c");

        todoMvc.edit("b", "b edited");

        todoMvc.toggle("b edited");
        todoMvc.clearCompleted();
        todoMvc.todosShouldBe("a", "c");

        todoMvc.cancelEditing("c", "c to be canceled");

        todoMvc.delete("c");
        todoMvc.todosShouldBe("a");
    }

    @Test
    void filtersTodos() {
        todoMvc.givenAppOpenedWith("a", "b", "c");
        todoMvc.toggle("b");

        todoMvc.filterActive();
        todoMvc.todosShouldBe("a", "c");

        todoMvc.filterCompleted();
        todoMvc.todosShouldBe("b");

        todoMvc.filterAll();
        todoMvc.todosShouldBe("a", "b", "c");
    }
}