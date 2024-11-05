package ga.ganma.foofledrive;

public enum FoofleDrivePlan {
    FREE(0), LIGHT(1), MIDDLE(2), LARGE(3);

    private final int id;

    private FoofleDrivePlan(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
