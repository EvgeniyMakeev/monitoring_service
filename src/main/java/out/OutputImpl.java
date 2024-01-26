package out;

public class OutputImpl implements Output<String> {
    @Override
    public void output(String s){
        System.out.println(s);
    }
}
