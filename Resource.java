class Resource {
// assigns every resource a unique ID
    private final int id;
    private boolean isAvailable = true;

    public Resource(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
