package applicationPackage;

public class Process {
    private int processId;
    private String name;
    private int creatorId;


    public Process(
        int processId,
        String name,
        int creatorId
    ) {
        this.processId = processId;
        this.name = name;
        this.creatorId = creatorId;
    }

    /**
     * Get the value of processId.
     * @return the processId
     */
    public int getProcessId() {
        return this.processId;
    }

    /**
     * Get the value of name.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the value of creatorId.
     * @return the creatorId
     */
    public int getCreatorId() {
        return this.creatorId;
    }
}
