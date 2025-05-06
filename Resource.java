class Resource {
// assigns every resource a unique ID
    private final int id;
    private boolean isAvailable = true;
    private String thread;
    private String owner = "";

    public Resource(int id) {
        this.id = id;
        this.thread = null;
    }
    public int getId() {
        return id;
    }
    
    public String getThread(){
        return thread;
    }
    

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }   


    public void setThread(String thread){
        this.thread = thread;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
}
