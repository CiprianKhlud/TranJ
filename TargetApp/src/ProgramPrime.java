/**
 * Created by Ciprian on 10/23/2016.
 */
public class ProgramPrime {

    public boolean isPrime(int value){
        if(value<4){
            return true;
        }
        if(value%2==0){
            return false;
        }
        for(int divisor = 3;divisor*divisor<=value; divisor+=2){
            if(value%divisor==0){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        ProgramPrime p = new ProgramPrime();
        for(int val = 0; val<1000000; val++){
            if(p.isPrime(val)){
                System.out.print(val);
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}
