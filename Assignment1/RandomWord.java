/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        int count = 1;
        String champaign = "";
        while (!StdIn.isEmpty()) {
            double prob = 1.0 / count;
            String word = StdIn.readString();
            if (StdRandom.bernoulli(prob)) {
                champaign = word;
            }
            count += 1;
        }
        StdOut.println(champaign);
    }
}
