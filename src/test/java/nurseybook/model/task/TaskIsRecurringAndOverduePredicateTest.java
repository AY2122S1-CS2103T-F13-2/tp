package nurseybook.model.task;

import static nurseybook.testutil.TypicalTasks.APPLY_LEAVE_LATE_TIME;
import static nurseybook.testutil.TypicalTasks.KEITH_INSULIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import nurseybook.testutil.TaskBuilder;

public class TaskIsRecurringAndOverduePredicateTest {
    private Task keithInsulin = new TaskBuilder(KEITH_INSULIN).build();
    private TaskIsRecurringAndOverduePredicate predicate = new TaskIsRecurringAndOverduePredicate();

    @Test
    public void test_notRecurringTasks_returnsFalse() {
        assertFalse(predicate.test(keithInsulin));
    }

    /**
     * Tests tasks that are recurring but not yet overdue
     */
    @Test
    public void test_recurringTasks_returnsFalse() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        // with current time -> recurring, not yet overdue -> returns false
        String[] dateTime = now.toString().split("T");
        String date = dateTime[0];
        String time = dateTime[1].substring(0, 5);

        Task applyLeaveRecurringDay = new TaskBuilder(APPLY_LEAVE_LATE_TIME).withDateTime(date, time).build();
        assertFalse(predicate.test(applyLeaveRecurringDay));

    }

    /**
     * Tests tasks that are both recurring and overdue
     */
    @Test
    public void test_recurringTasks_returnsTrue() {
        LocalDateTime past = LocalDateTime.now().withSecond(0).withNano(0)
                .minusMinutes(1);

        // one minute before -> recurring, overdue -> returns true
        String[] dateTime = past.toString().split("T");
        String date = dateTime[0];
        String time = dateTime[1].substring(0, 5);

        Task applyLeaveRecurringDay = new TaskBuilder(APPLY_LEAVE_LATE_TIME).withDateTime(date, time).build();
        assertTrue(predicate.test(applyLeaveRecurringDay));
    }
}
