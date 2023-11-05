package manager;
import task.Task;

public class CustomLinkedListNode {
    Task task;
    CustomLinkedListNode next;
    CustomLinkedListNode prev;

    public CustomLinkedListNode(Task task){
        this.task = task;
    }
}
