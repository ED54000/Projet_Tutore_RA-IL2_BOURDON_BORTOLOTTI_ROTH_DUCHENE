package laby;

public interface Subject {
    public void registerObserver(Observer obs);
    public void deleteObserver(Observer obs);
    public void notifyObserver();
}
