import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class Sample {

    public static void main(String[] args) {
        try {
            MongoClient mongo = new MongoClient("192.168.99.100", 32824);
            System.out.println(mongo.getDatabase("test"));
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}
