public class SubTask extends Task{

    Epic newEpic;

    public SubTask(String name, String description, Progress progress/*, Epic newEpic*/) {
        super(name, description, progress);
        // this.newEpic = newEpic;
    }


    @Override
    public String toString() {
        return "SubTask{" +
                /*"newEpic=" + newEpic +*/
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", progress=" + progress +
                '}';
    }
}
